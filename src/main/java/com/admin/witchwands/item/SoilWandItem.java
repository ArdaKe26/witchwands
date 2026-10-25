package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.SoilWandConstants.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoilWandItem extends WandItem {
    private static final Map<UUID, Long> GROW_READY_TIMES = new HashMap<>();
    private static final Map<UUID, Long> FOOD_GIVE_READY_TIMES = new HashMap<>();
    private static final Map<Block, Item> BLOCK_TO_CROP_ITEMS = Map.of(Blocks.WHEAT, Items.WHEAT, Blocks.CARROTS,
            Items.CARROT, Blocks.POTATOES, Items.POTATO, Blocks.BEETROOTS, Items.BEETROOT);

    public SoilWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world instanceof ServerWorld serverWorld) {
            long time = serverWorld.getTime();
            UUID userUuid = user.getUuid();

            if (user.isSneaking()) {
                if (!isAbilityReady(user, time, userUuid, GROW_READY_TIMES, "Grow")) {
                    return TypedActionResult.fail(itemStack);
                }

                BlockState standingBlockState = user.getSteppingBlockState();
                BlockPos standingBlockPos = user.getSteppingPos();

                if (standingBlockState.getBlock() != Blocks.FARMLAND) {
                    user.sendMessage(Text.literal("Stand on a Farm Block").formatted(Formatting.GRAY), true);
                    return TypedActionResult.fail(itemStack);
                } else {
                    Item offhandItem = user.getOffHandStack().getItem();

                    if (offhandItem instanceof BlockItem blockItem) {
                        if (blockItem.getBlock() instanceof CropBlock crop) {
                            Queue<BlockPos> toCheck = new ArrayDeque<>();
                            Set<BlockPos> checked = new HashSet<>();
                            List<BlockPos> farmBlocks = new ArrayList<>();

                            toCheck.add(standingBlockPos);

                            while (!toCheck.isEmpty()) {
                                BlockPos pos = toCheck.poll();

                                if (checked.contains(pos)) {
                                    continue;
                                }
                                checked.add(pos);

                                if (pos.getSquaredDistance(standingBlockPos) > GROW_RADIUS * GROW_RADIUS) {
                                    continue;
                                }

                                if (serverWorld.getBlockState(pos).getBlock() != Blocks.FARMLAND) {
                                    continue;
                                }

                                farmBlocks.add(pos);
                                toCheck.add(pos.east());
                                toCheck.add(pos.west());
                                toCheck.add(pos.north());
                                toCheck.add(pos.south());
                            }

                            int plantedCount = 0;
                            int grewCount = 0;
                            int ticketCount = 0;

                            for (BlockPos farmBlock : farmBlocks) {
                                BlockPos abovePos = farmBlock.up();
                                BlockState aboveState = serverWorld.getBlockState(abovePos);

                                if (aboveState.getBlock() instanceof CropBlock cropBlock) {
                                    int cropMaxAge = cropBlock.getMaxAge();
                                    BlockState fullGrownCropState = cropBlock.withAge(cropMaxAge);
                                    serverWorld.setBlockState(abovePos, fullGrownCropState);

                                    serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, abovePos.getX() + 0.5,
                                            abovePos.getY() + 0.5, abovePos.getZ() + 0.5, GROW_PARTICLE_COUNT,
                                            GROW_PARTICLE_SPREAD,
                                            GROW_PARTICLE_SPREAD, GROW_PARTICLE_SPREAD, GROW_PARTICLE_SPEED);

                                    grewCount++;
                                } else if (aboveState.isAir()) {
                                    if (ticketCount == 0) {
                                        if (takeSeed(user, offhandItem)) {
                                            ticketCount = ticketCount + 2;
                                        } else {
                                            continue;
                                        }
                                    }

                                    int cropMaxAge = crop.getMaxAge();
                                    BlockState fullGrownCropState = crop.withAge(cropMaxAge);
                                    serverWorld.setBlockState(abovePos, fullGrownCropState);

                                    serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, abovePos.getX() + 0.5,
                                            abovePos.getY() + 0.5, abovePos.getZ() + 0.5, GROW_PARTICLE_COUNT,
                                            GROW_PARTICLE_SPREAD,
                                            GROW_PARTICLE_SPREAD, GROW_PARTICLE_SPREAD, GROW_PARTICLE_SPEED);

                                    grewCount++;
                                    plantedCount++;
                                    ticketCount--;
                                }

                            }

                            serverWorld.playSound(null, user.getX(), user.getY(), user.getZ(),
                                    SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, GROW_SOUND_VOLUME,
                                    GROW_SOUND_PITCH);

                            user.sendMessage(Text.literal("Planted: " + plantedCount + " / Grew: " + grewCount)
                                    .formatted(Formatting.GREEN), true);

                        } else {
                            user.sendMessage(Text.literal("Hold Seeds in Your Offhand").formatted(Formatting.GRAY),
                                    true);
                            return TypedActionResult.fail(itemStack);
                        }
                    } else {
                        user.sendMessage(Text.literal("Hold Seeds in Your Offhand").formatted(Formatting.GRAY),
                                true);
                        return TypedActionResult.fail(itemStack);
                    }
                }
            } else {
                if (!isAbilityReady(user, time, userUuid, FOOD_GIVE_READY_TIMES, "Food Give")) {
                    return TypedActionResult.fail(itemStack);
                }

                HitResult hitResult = user.raycast(FOOD_GIVE_RANGE, 1.0f, false);

                if (hitResult.getType() != HitResult.Type.BLOCK) {
                    user.sendMessage(Text.literal("Look at a Crop!").formatted(Formatting.GRAY), true);
                    return TypedActionResult.fail(itemStack);
                } else {
                    if (hitResult instanceof BlockHitResult blockHitResult) {
                        BlockPos blockPos = blockHitResult.getBlockPos();

                        BlockState blockState = serverWorld.getBlockState(blockPos);

                        if (blockState.getBlock() instanceof CropBlock cropBlock) {
                            Item crop = BLOCK_TO_CROP_ITEMS.get(cropBlock);

                            if (crop == null) {
                                user.sendMessage(
                                        Text.literal("This Crop Does Not Give Food").formatted(Formatting.GRAY), true);
                                return TypedActionResult.fail(itemStack);
                            }

                            ItemStack foodToGive = new ItemStack(crop, FOOD_GIVE_AMOUNT);

                            user.giveItemStack(foodToGive);

                            if (!foodToGive.isEmpty()) {
                                user.dropItem(foodToGive, false);
                            }

                            serverWorld.playSound(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5,
                                    blockPos.getZ() + 0.5,
                                    SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, FOOD_GIVE_SOUND_VOLUME,
                                    FOOD_GIVE_SOUND_PITCH);
                            serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, user.getX(), user.getBodyY(0.5),
                                    user.getZ(), FOOD_GIVE_PARTICLE_COUNT, FOOD_GIVE_PARTICLE_SPREAD,
                                    FOOD_GIVE_PARTICLE_SPREAD, FOOD_GIVE_PARTICLE_SPREAD, FOOD_GIVE_PARTICLE_SPEED);

                        } else {
                            user.sendMessage(Text.literal("This is not a Crop").formatted(Formatting.GRAY), true);
                            return TypedActionResult.fail(itemStack);
                        }
                        FOOD_GIVE_READY_TIMES.put(userUuid, time + FOOD_GIVE_COOLDOWN_TICKS);
                    }
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
                notifyIfReady(FOOD_GIVE_READY_TIMES, "Food Give", time, playerEntity);
                notifyIfReady(GROW_READY_TIMES, "Grow", time, playerEntity);
            }
        }
    }

    private static boolean takeSeed(PlayerEntity player, Item seedItem) {
        ItemStack offhandItemStack = player.getOffHandStack();

        if (offhandItemStack.isOf(seedItem)) {
            offhandItemStack.decrement(1);
            return true;
        } else {
            for (int i = 0; i < player.getInventory().size(); i++) {
                if (player.getInventory().getStack(i).isOf(seedItem)) {
                    player.getInventory().getStack(i).decrement(1);
                    return true;
                }
            }
        }

        return false;
    }
}
