package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.IceWandConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.admin.witchwands.entity.IceWandSnowballEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class IceWandItem extends WandItem {
    private static final Map<UUID, Long> SNOWBALL_RELOAD_READY_TIMES = new HashMap<>();
    private static final Map<UUID, Long> FREEZE_READY_TIMES = new HashMap<>();
    private static final Map<UUID, Integer> SNOWBALL_AMMO = new HashMap<>();

    public IceWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world instanceof ServerWorld serverWorld) {

            long time = serverWorld.getTime();
            UUID userUuid = user.getUuid();

            if (user.isSneaking()) {
                if (!isAbilityReady(user, time, userUuid, FREEZE_READY_TIMES, "Freeze")) {
                    return TypedActionResult.fail(itemStack);
                }

                List<LivingEntity> mobsNearby = findEntitiesInRadius(serverWorld, user, FREEZE_RADIUS);

                for (LivingEntity entity : mobsNearby) {
                    entity.addStatusEffect(
                            new StatusEffectInstance(StatusEffects.SLOWNESS, FREEZE_DURATION_TICKS,
                                    FREEZE_SLOWNESS_AMPLIFIER));
                    entity.setFrozenTicks(entity.getMinFreezeDamageTicks() + 2 * FREEZE_DURATION_TICKS);
                }

                if (!mobsNearby.isEmpty()) {
                    user.sendMessage(
                            Text.literal(
                                    mobsNearby.size()
                                            + (mobsNearby.size() == 1 ? " entity frozen" : " entities frozen"))
                                    .formatted(Formatting.AQUA),
                            true);
                } else {
                    user.sendMessage(
                            Text.literal("No entities in range of the Attack").formatted(Formatting.GRAY), true);
                }

                FREEZE_READY_TIMES.put(userUuid, time + FREEZE_COOLDOWN_TICKS);

            } else {
                if (!isAbilityReady(user, time, userUuid, SNOWBALL_RELOAD_READY_TIMES, "Snowball")) {
                    return TypedActionResult.fail(itemStack);
                }

                int ammoCount = SNOWBALL_AMMO.getOrDefault(userUuid, SNOWBALL_MAX_AMMO);

                IceWandSnowballEntity iceWandSnowballEntity = new IceWandSnowballEntity(serverWorld, user);

                iceWandSnowballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, SNOWBALL_SPEED,
                        SNOWBALL_DIVERGENCE);
                serverWorld.spawnEntity(iceWandSnowballEntity);

                ammoCount -= 1;

                if (ammoCount <= 0) {
                    SNOWBALL_RELOAD_READY_TIMES.put(userUuid, time + SNOWBALL_RELOAD_COOLDOWN_TICKS);
                    SNOWBALL_AMMO.put(userUuid, SNOWBALL_MAX_AMMO);
                } else {
                    SNOWBALL_AMMO.put(userUuid, ammoCount);
                }

                user.sendMessage(
                        Text.literal("Snowballs: " + ammoCount + "/" + SNOWBALL_MAX_AMMO).formatted(Formatting.AQUA),
                        true);
            }
        }

        return TypedActionResult.success(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            long time = world.getTime();
            if (entity instanceof PlayerEntity player) {
                notifyIfReady(SNOWBALL_RELOAD_READY_TIMES, "Snowball", time, player);
                notifyIfReady(FREEZE_READY_TIMES, "Freeze", time, player);
            }
        }
    }
}
