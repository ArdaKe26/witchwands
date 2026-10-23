package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.TeleportWandConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TeleportWandItem extends WandItem {
    private static final Map<UUID, Long> TELEPORT_READY_TIMES = new HashMap<>();

    public TeleportWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world instanceof ServerWorld serverWorld) {
            long time = serverWorld.getTime();
            UUID userUuid = user.getUuid();

            if (!isAbilityReady(user, time, userUuid, TELEPORT_READY_TIMES, "Teleport")) {
                return TypedActionResult.fail(itemStack);
            }

            HitResult hitResult = user.raycast(TELEPORT_RANGE, 1.0f, false);

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

                user.requestTeleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
                user.fallDistance = 0;

                TELEPORT_READY_TIMES.put(userUuid, time + TELEPORT_COOLDOWN_TICKS);

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
            }
        }
    }

    private static boolean isPassable(World world, BlockPos pos) {
        return world.getBlockState(pos).getCollisionShape(world, pos).isEmpty();
    }
}
