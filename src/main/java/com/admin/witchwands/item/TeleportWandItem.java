package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.TeleportWandConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class TeleportWandItem extends WandItem {
    private static final Map<UUID, Long> TELEPORT_READY_TIMES = new HashMap<>();
    private static final Map<UUID, Long> PULL_READY_TIMES = new HashMap<>();

    public TeleportWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world instanceof ServerWorld serverWorld) {
            long time = serverWorld.getTime();
            UUID userUuid = user.getUuid();
            if (user.isSneaking()) {
                if (!isAbilityReady(user, time, userUuid, PULL_READY_TIMES, "Pull")) {
                    return TypedActionResult.fail(itemStack);
                }

                PULL_READY_TIMES.put(userUuid, time + PULL_COOLDOWN_TICKS);

                Vec3d playerEyePos = user.getEyePos();
                Vec3d playerLookDirection = user.getRotationVec(1.0f);
                Vec3d playerPullLook = playerEyePos.add(playerLookDirection.multiply(PULL_RANGE));

                HitResult hitResult = serverWorld.raycast(new RaycastContext(playerEyePos, playerPullLook,
                        RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user));

                Box pullSearchBox = user.getBoundingBox().stretch(playerLookDirection.multiply(PULL_RANGE))
                        .expand(1.0);

                EntityHitResult entityHitResult = ProjectileUtil.raycast(user, playerEyePos, playerPullLook,
                        pullSearchBox, entity -> entity != user && entity.isLiving(), PULL_RANGE * PULL_RANGE);

                if (entityHitResult == null) {
                    user.sendMessage(Text.literal("No Target Found").formatted(Formatting.GRAY), true);
                    return TypedActionResult.fail(itemStack);
                } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                    if (entityHitResult.squaredDistanceTo(user) > hitResult.squaredDistanceTo(user)) {
                        user.sendMessage(Text.literal("No Target Found").formatted(Formatting.GRAY), true);
                        return TypedActionResult.fail(itemStack);
                    }
                }

                Vec3d pullLook = new Vec3d(playerLookDirection.getX(), 0, playerLookDirection.getZ()).normalize();
                Vec3d playerFeetPos = user.getPos();
                Vec3d landingVec = playerFeetPos.add(pullLook.multiply(PULL_LANDING_DISTANCE));
                BlockPos landingPos = BlockPos.ofFloored(landingVec);

                if (!isPassable(serverWorld, landingPos) || !isPassable(serverWorld, landingPos.up())) {
                    user.sendMessage(Text.literal("Not Enough Space").formatted(Formatting.GRAY), true);
                } else {
                    Entity hitEntity = entityHitResult.getEntity();

                    Vec3d pulledEntityOldPos = hitEntity.getPos();
                    double pulledEntityOldY = hitEntity.getBodyY(0.5);

                    hitEntity.requestTeleport(landingVec.getX(), landingVec.getY(), landingVec.getZ());
                    hitEntity.fallDistance = 0;

                    serverWorld.playSound(null, pulledEntityOldPos.getX(), pulledEntityOldPos.getY(),
                            pulledEntityOldPos.getZ(),
                            SoundEvents.ENTITY_PLAYER_TELEPORT,
                            SoundCategory.PLAYERS, TELEPORT_SOUND_VOLUME, TELEPORT_DEPART_SOUND_PITCH);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, pulledEntityOldPos.getX(), pulledEntityOldY,
                            pulledEntityOldPos.getZ(), TELEPORT_PARTICLE_COUNT, TELEPORT_PARTICLE_SPREAD,
                            TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPEED);

                    serverWorld.playSound(null, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(),
                            SoundEvents.ENTITY_PLAYER_TELEPORT,
                            SoundCategory.PLAYERS, TELEPORT_SOUND_VOLUME, TELEPORT_ARRIVE_SOUND_PITCH);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, hitEntity.getX(), hitEntity.getBodyY(0.5),
                            hitEntity.getZ(), TELEPORT_PARTICLE_COUNT, TELEPORT_PARTICLE_SPREAD,
                            TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPEED);

                    if (hitEntity instanceof PlayerEntity playerEntity) {
                        playerEntity.getItemCooldownManager().set(Items.ENDER_PEARL, PEARL_LOCK_TICKS);

                        playerEntity.sendMessage(Text.literal("Pearls are Blocked").formatted(Formatting.GREEN), true);
                    }
                }
            } else {
                if (!isAbilityReady(user, time, userUuid, TELEPORT_READY_TIMES, "Teleport")) {
                    return TypedActionResult.fail(itemStack);
                }

                Vec3d playerEyePos = user.getEyePos();
                Vec3d playerLookDirection = user.getRotationVec(1.0f);
                Vec3d playerTeleportLook = playerEyePos.add(playerLookDirection.multiply(TELEPORT_RANGE));

                HitResult hitResult = serverWorld.raycast(new RaycastContext(playerEyePos, playerTeleportLook,
                        RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user));

                if (hitResult.getType() == HitResult.Type.MISS) {
                    user.sendMessage(Text.literal("No Blocks in Range").formatted(Formatting.GRAY), true);
                    return TypedActionResult.fail(itemStack);
                } else if (hitResult instanceof BlockHitResult blockHit) {
                    BlockPos targetPos = blockHit.getBlockPos().offset(blockHit.getSide());

                    if (blockHit.getSide() == Direction.DOWN) {
                        targetPos = targetPos.down();
                    }
                    if (!isPassable(serverWorld, targetPos)
                            || !isPassable(serverWorld, targetPos.up())) {

                        user.sendMessage(Text.literal("Not Enough Space There").formatted(Formatting.GRAY), true);

                        return TypedActionResult.fail(itemStack);
                    }
                    Vec3d playerOldPos = user.getPos();
                    double playerOldBodyY = user.getBodyY(0.5);

                    user.requestTeleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
                    user.fallDistance = 0;

                    serverWorld.playSound(null, playerOldPos.getX(), playerOldPos.getY(), playerOldPos.getZ(),
                            SoundEvents.ENTITY_PLAYER_TELEPORT,
                            SoundCategory.PLAYERS, TELEPORT_SOUND_VOLUME, TELEPORT_DEPART_SOUND_PITCH);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, playerOldPos.getX(), playerOldBodyY,
                            playerOldPos.getZ(), TELEPORT_PARTICLE_COUNT, TELEPORT_PARTICLE_SPREAD,
                            TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPEED);

                    serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(),
                            SoundEvents.ENTITY_PLAYER_TELEPORT,
                            SoundCategory.PLAYERS, TELEPORT_SOUND_VOLUME, TELEPORT_ARRIVE_SOUND_PITCH);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getBodyY(0.5),
                            user.getZ(), TELEPORT_PARTICLE_COUNT, TELEPORT_PARTICLE_SPREAD,
                            TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPREAD, TELEPORT_PARTICLE_SPEED);

                    TELEPORT_READY_TIMES.put(userUuid, time + TELEPORT_COOLDOWN_TICKS);
                }

            }
        }

        return TypedActionResult.success(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            long time = world.getTime();
            if (entity instanceof PlayerEntity playerEntity) {
                notifyIfReady(TELEPORT_READY_TIMES, "Teleport", time, playerEntity);
                notifyIfReady(PULL_READY_TIMES, "Pull", time, playerEntity);
            }
        }
    }

    private static boolean isPassable(World world, BlockPos pos) {
        return world.getBlockState(pos).getCollisionShape(world, pos).isEmpty();
    }
}
