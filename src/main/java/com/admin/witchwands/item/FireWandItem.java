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
                if (!isAbilityRead(user, time, userUuid, IGNITE_READY_TIMES, "Ignite")) {
                    return TypedActionResult.fail(itemStack);
                }

                List<LivingEntity> mobsNearby = world.getEntitiesByClass(LivingEntity.class,
                        user.getBoundingBox().expand(IGNITE_RADIUS),
                        entity -> entity != user
                                && entity.distanceTo(user) <= IGNITE_RADIUS);

                for (LivingEntity entity : mobsNearby) {
                    entity.setOnFireFor(FIRE_DURATION_SECONDS);
                }

                IGNITE_READY_TIMES.put(userUuid, time + IGNITE_COOLDOWN_TICKS);
            } else {
                if (!isAbilityRead(user, time, userUuid, FIREBALL_READY_TIMES, "Fireball")) {
                    return TypedActionResult.fail(itemStack);
                }

                Vec3d playerLookDirection = user.getRotationVec(1.0f);

                FireWandFireballEntity fireWandFireball = new FireWandFireballEntity(world, user, playerLookDirection);
                fireWandFireball.setPos(user.getX(), user.getEyeY(), user.getZ());
                world.spawnEntity(fireWandFireball);

                FIREBALL_READY_TIMES.put(userUuid, time + FIREBALL_COOLDOWN_TICKS);
            }
        }
        return TypedActionResult.success(itemStack);
    }

    private static boolean isAbilityRead(PlayerEntity user, long time, UUID playerUUID, Map<UUID, Long> READY_TIMES,
            String abilityName) {
        long readyTime = READY_TIMES.getOrDefault(playerUUID, 0L);

        if (time < readyTime) {
            user.sendMessage(
                    Text.literal(abilityName + " Cooldown: " + (readyTime - time) / 20 + "s").formatted(Formatting.RED),
                    true);

            return false;
        } else {
            return true;
        }
    }
}
