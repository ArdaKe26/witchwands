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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireWandItem extends Item {
    private static final Map<UUID, Long> FIREBALL_READY_TIMES = new HashMap<>();
    private static final Map<UUID, Long> IGNITE_READY_TIMES = new HashMap<>();

    public FireWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            long time = world.getTime();
            UUID userUuid = user.getUuid();

            if (user.isSneaking()) {
                if (!isAbilityReady(user, time, userUuid, IGNITE_READY_TIMES, "Ignite")) {
                    return TypedActionResult.fail(itemStack);
                }

                List<LivingEntity> mobsNearby = world.getEntitiesByClass(LivingEntity.class,
                        user.getBoundingBox().expand(IGNITE_RADIUS),
                        entity -> entity != user
                                && entity.distanceTo(user) <= IGNITE_RADIUS);

                for (LivingEntity entity : mobsNearby) {
                    entity.setOnFireFor(FIRE_DURATION_SECONDS);

                    world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_BLAZE_BURN,
                            SoundCategory.PLAYERS, 0.4f, 1.0f);
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

                FireWandFireballEntity fireWandFireball = new FireWandFireballEntity(world, user, playerLookDirection);
                fireWandFireball.setPos(user.getX(), user.getEyeY(), user.getZ());
                world.spawnEntity(fireWandFireball);

                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);

                FIREBALL_READY_TIMES.put(userUuid, time + FIREBALL_COOLDOWN_TICKS);
            }
        }
        return TypedActionResult.success(itemStack);
    }

    private static boolean isAbilityReady(PlayerEntity user, long time, UUID playerUUID, Map<UUID, Long> readyTimes,
            String abilityName) {
        long readyTime = readyTimes.getOrDefault(playerUUID, 0L);

        if (time < readyTime) {
            user.sendMessage(
                    Text.literal(abilityName + " Cooldown: " + (readyTime - time) / 20 + "s").formatted(Formatting.RED),
                    true);
            user.playSoundToPlayer(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, FIZZLE_SOUND_VOLUME,
                    FIZZLE_SOUND_PITCH);
            return false;
        } else {
            return true;
        }
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

    private static void notifyIfReady(Map<UUID, Long> readyTimes, String name, long time,
            PlayerEntity user) {
        long cooldownReadyTime = readyTimes.getOrDefault(user.getUuid(), 0L);

        if (time == cooldownReadyTime) {
            user.sendMessage(Text.literal(name + " is ready to use").formatted(Formatting.GREEN), true);
            user.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, READY_SOUND_VOLUME,
                    READY_SOUND_PITCH);
        }
    }
}
