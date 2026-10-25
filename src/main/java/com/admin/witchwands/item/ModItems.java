package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.WandConstants.*;
import static com.admin.witchwands.ModConstants.WitchConstants.SPAWN_EGG_NAME;
import static com.admin.witchwands.ModConstants.WitchConstants.SPAWN_EGG_PRIMARY_COLOR;
import static com.admin.witchwands.ModConstants.WitchConstants.SPAWN_EGG_SECONDARY_COLOR;

import org.slf4j.Logger;

import com.admin.witchwands.ModConstants.FireWandConstants;
import com.admin.witchwands.ModConstants.IceWandConstants;
import com.admin.witchwands.ModConstants.TeleportWandConstants;
import com.admin.witchwands.entity.ModEntities;
import com.admin.witchwands.ModConstants.SoilWandConstants;
import com.admin.witchwands.WitchWands;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SpawnEggItem;

public class ModItems {
        public static final Logger LOGGER = WitchWands.LOGGER;

        public static final Item FIRE_WAND = WitchWands.registerItem(FireWandConstants.NAME,
                        new FireWandItem(new Item.Settings().maxCount(FireWandConstants.MAX_STACK_SIZE)
                                        .attributeModifiers(
                                                        ShovelItem.createAttributeModifiers(MELEE_MATERIAL,
                                                                        MELEE_BASE_DAMAGE, ATTACK_SPEED))));

        public static final Item ICE_WAND = WitchWands.registerItem(IceWandConstants.NAME,
                        new IceWandItem(new Item.Settings().maxCount(IceWandConstants.MAX_STACK_SIZE)
                                        .attributeModifiers(
                                                        ShovelItem.createAttributeModifiers(MELEE_MATERIAL,
                                                                        MELEE_BASE_DAMAGE, ATTACK_SPEED))));

        public static final Item TELEPORT_WAND = WitchWands.registerItem(TeleportWandConstants.NAME,
                        new TeleportWandItem(new Item.Settings().maxCount(TeleportWandConstants.MAX_STACK_SIZE)
                                        .attributeModifiers(
                                                        ShovelItem.createAttributeModifiers(MELEE_MATERIAL,
                                                                        MELEE_BASE_DAMAGE, ATTACK_SPEED))));
        public static final Item SOIL_WAND = WitchWands.registerItem(SoilWandConstants.NAME,
                        new SoilWandItem(new Item.Settings().maxCount(SoilWandConstants.MAX_STACK_SIZE)
                                        .attributeModifiers(
                                                        HoeItem.createAttributeModifiers(
                                                                        SoilWandConstants.MELEE_MATERIAL,
                                                                        SoilWandConstants.MELEE_BASE_DAMAGE,
                                                                        SoilWandConstants.ATTACK_SPEED))));

        public static final Item WAND_WITCH_SPAWN_EGG = WitchWands.registerItem(SPAWN_EGG_NAME,
                        new SpawnEggItem(ModEntities.WAND_WITCH, SPAWN_EGG_PRIMARY_COLOR, SPAWN_EGG_SECONDARY_COLOR,
                                        new Item.Settings()));

        public static void initialize() {
                LOGGER.info("Registering items!");

                ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(x -> x.add(FIRE_WAND));
                ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(x -> x.add(ICE_WAND));
                ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(x -> x.add(TELEPORT_WAND));
        }
}
