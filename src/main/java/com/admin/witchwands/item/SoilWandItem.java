package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.SoilWandConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
            }
        }
    }
}
