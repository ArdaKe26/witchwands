package com.admin.witchwands.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.admin.witchwands.ModConstants.FireWandConstants.*;
import com.admin.witchwands.entity.FireWandFireballEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireWandItem extends WandItem {
    private static final Map<UUID, Long> FIREBALL_READY_TIMES = new HashMap<>();
    private static final Map<UUID, Long> IGNITE_READY_TIMES = new HashMap<>();

    public FireWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world instanceof ServerWorld serverWorld) {
            long time = serverWorld.getTime();
            UUID userUuid = user.getUuid();

            if (user.isSneaking()) {
                if (!isAbilityReady(user, time, userUuid, IGNITE_READY_TIMES, "Ignite")) {
                    return TypedActionResult.fail(itemStack);
                }

                List<LivingEntity> mobsNearby = findEntitiesInRadius(serverWorld, user, IGNITE_RADIUS);

                for (LivingEntity entity : mobsNearby) {
                    entity.setOnFireFor(FIRE_DURATION_SECONDS);

                    serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.ENTITY_BLAZE_BURN,
                            SoundCategory.PLAYERS, 0.4f, 1.0f);

                    serverWorld.spawnParticles(
                            ParticleTypes.FLAME,
                            entity.getX(),
                            entity.getBodyY(0.5),
                            entity.getZ(),
                            IGNITE_PARTICLE_COUNT,
                            entity.getWidth() / 2,
                            entity.getHeight() / 2,
                            entity.getWidth() / 2,
                            IGNITE_PARTICLE_SPEED);
                }

                if (!mobsNearby.isEmpty()) {
                    user.sendMessage(
                            Text.literal(
                                    mobsNearby.size()
                                            + (mobsNearby.size() == 1 ? " entity ignited" : " entities ignited"))
                                    .formatted(Formatting.GOLD),
                            true);
                } else {
                    user.sendMessage(
                            Text.literal("No entities in range of the Attack").formatted(Formatting.GRAY), true);
                }

                IGNITE_READY_TIMES.put(userUuid, time + IGNITE_COOLDOWN_TICKS);
            } else {
                if (!isAbilityReady(user, time, userUuid, FIREBALL_READY_TIMES, "Fireball")) {
                    return TypedActionResult.fail(itemStack);
                }

                Vec3d playerLookDirection = user.getRotationVec(1.0f);
                Vec3d fireballBurstPos = user.getEyePos().add(playerLookDirection);

                FireWandFireballEntity fireWandFireball = new FireWandFireballEntity(serverWorld, user,
                        playerLookDirection);
                fireWandFireball.setPos(user.getX(), user.getEyeY(), user.getZ());
                serverWorld.spawnEntity(fireWandFireball);

                serverWorld.spawnParticles(
                        ParticleTypes.FLAME,
                        fireballBurstPos.getX(),
                        fireballBurstPos.getY(),
                        fireballBurstPos.getZ(),
                        FIREBALL_FLAME_PARTICLE_COUNT,
                        FIREBALL_FLAME_PARTICLE_SPREAD,
                        FIREBALL_FLAME_PARTICLE_SPREAD,
                        FIREBALL_FLAME_PARTICLE_SPREAD,
                        FIREBALL_FLAME_PARTICLE_SPEED);

                serverWorld.spawnParticles(
                        ParticleTypes.SMOKE,
                        fireballBurstPos.getX(),
                        fireballBurstPos.getY(),
                        fireballBurstPos.getZ(),
                        FIREBALL_SMOKE_PARTICLE_COUNT,
                        FIREBALL_SMOKE_PARTICLE_SPREAD,
                        FIREBALL_SMOKE_PARTICLE_SPREAD,
                        FIREBALL_SMOKE_PARTICLE_SPREAD,
                        FIREBALL_SMOKE_PARTICLE_SPEED);

                serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);

                FIREBALL_READY_TIMES.put(userUuid, time + FIREBALL_COOLDOWN_TICKS);
            }
        }
        return TypedActionResult.success(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            long time = world.getTime();
            if (entity instanceof PlayerEntity player) {
                notifyIfReady(FIREBALL_READY_TIMES, "Fireball", time, player);
                notifyIfReady(IGNITE_READY_TIMES, "Ignite", time, player);
            }
        }
    }
}
