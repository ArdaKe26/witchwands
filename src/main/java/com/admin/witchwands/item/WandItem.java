package com.admin.witchwands.item;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.admin.witchwands.ModConstants.WandConstants.*;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public abstract class WandItem extends Item {
    public WandItem(Settings settings) {
        super(settings);
    }

    protected static boolean isAbilityReady(PlayerEntity user, long time, UUID playerUUID, Map<UUID, Long> readyTimes,
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

    protected static void notifyIfReady(Map<UUID, Long> readyTimes, String name, long time,
            PlayerEntity user) {
        long cooldownReadyTime = readyTimes.getOrDefault(user.getUuid(), 0L);

        if (time == cooldownReadyTime) {
            user.sendMessage(Text.literal(name + " is ready to use").formatted(Formatting.GREEN), true);
            user.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, READY_SOUND_VOLUME,
                    READY_SOUND_PITCH);
        }
    }

    protected static List<LivingEntity> findEntitiesInRadius(World world, PlayerEntity user, double radius) {
        return world.getEntitiesByClass(LivingEntity.class,
                user.getBoundingBox().expand(radius),
                entity -> entity != user
                        && entity.distanceTo(user) <= radius);
    }
}
