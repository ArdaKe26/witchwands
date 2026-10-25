package com.admin.witchwands.entity;

import static com.admin.witchwands.ModConstants.WitchConstants.*;

import java.util.List;

import com.admin.witchwands.item.ModItems;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.structure.StructureKeys;

public class WitchSpawner {

    public static void tick(ServerWorld world) {
        if (world.getTime() % SPAWN_ATTEMPT_INTERVAL_TICKS == 0) {
            for (PlayerEntity player : world.getPlayers()) {
                double randomX = player.getX() + (world.getRandom().nextDouble() - 0.5) * 2 * SPAWN_MAX_DISTANCE;
                double randomY = player.getY() + (world.getRandom().nextDouble() - 0.5) * 2 * SPAWN_VERTICAL_RANGE;
                double randomZ = player.getZ() + (world.getRandom().nextDouble() - 0.5) * 2 * SPAWN_MAX_DISTANCE;

                if (player.squaredDistanceTo(randomX, randomY, randomZ) < SPAWN_MIN_DISTANCE * SPAWN_MIN_DISTANCE) {
                    continue;
                }

                BlockPos blockPos = BlockPos.ofFloored(randomX, randomY, randomZ);
                BlockPos groundPos = findGround(world, blockPos);

                if (groundPos == null) {
                    continue;
                }

                Item wand = pickWand(world, groundPos);

                if (wand == null) {
                    continue;
                }

                if (world.getRandom().nextFloat() > SPAWN_CHANCE) {
                    continue;
                }

                WandWitchEntity witch = ModEntities.WAND_WITCH.create(world);
                if (witch == null) {
                    continue;
                }
                witch.refreshPositionAndAngles(groundPos.getX() + 0.5, groundPos.getY(), groundPos.getZ() + 0.5, 0, 0);
                witch.initialize(world, world.getLocalDifficulty(groundPos), SpawnReason.NATURAL, null);
                witch.setWand(wand);
                world.spawnEntity(witch);
            }
        }
    }

    private static BlockPos findGround(ServerWorld world, BlockPos start) {
        for (int i = 0; i < SPAWN_VERTICAL_RANGE * 2; i++) {
            BlockPos pos = start.down(i);

            boolean groundIsSolid = world.getBlockState(pos.down()).isSolidBlock(world, pos.down());

            boolean feetAreEmpty = world.getBlockState(pos).getCollisionShape(world, pos).isEmpty();

            boolean haedIsEmpty = world.getBlockState(pos.up()).getCollisionShape(world, pos.up()).isEmpty();

            boolean noWater = world.getFluidState(pos).isEmpty();

            if (groundIsSolid && feetAreEmpty && haedIsEmpty && noWater) {
                return pos;
            }
        }

        return null;
    }

    private static Item pickWand(ServerWorld world, BlockPos blockPos) {
        if (world.getBiome(blockPos).matchesKey(BiomeKeys.FROZEN_PEAKS)
                || world.getBiome(blockPos).matchesKey(BiomeKeys.JAGGED_PEAKS)) {
            return ModItems.ICE_WAND;
        } else if (world.getStructureAccessor().getStructureContaining(blockPos,
                entry -> entry.matchesKey(StructureKeys.FORTRESS)).hasChildren()) {
            return ModItems.FIRE_WAND;
        } else if (world.getStructureAccessor().getStructureContaining(blockPos,
                entry -> entry.matchesKey(StructureKeys.END_CITY)).hasChildren()) {
            return ModItems.TELEPORT_WAND;
        } else if (world.getStructureAccessor().getStructureContaining(blockPos,
                StructureTags.VILLAGE).hasChildren()) {
            Box villageBox = new Box(blockPos).expand(VILLAGER_CHECK_RADIUS);
            List<VillagerEntity> villagers = world.getEntitiesByClass(VillagerEntity.class, villageBox, entity -> true);
            if (villagers.isEmpty()) {
                return ModItems.SOIL_WAND;
            }
        }

        return null;
    }

}
