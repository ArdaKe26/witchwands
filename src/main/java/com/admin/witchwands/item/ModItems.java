package com.admin.witchwands.item;

import org.slf4j.Logger;

import com.admin.witchwands.ModConstants;
import com.admin.witchwands.WitchWands;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

public class ModItems {
    public static final Logger LOGGER = WitchWands.LOGGER;

    public static final Item FIRE_WAND = WitchWands.registerItem(ModConstants.FireWandConstants.NAME,
            new FireWandItem(new Item.Settings().maxCount(ModConstants.FireWandConstants.MAX_STACK_SIZE)));

    public static void initialize() {
        LOGGER.info("Registering items!");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(x -> x.add(FIRE_WAND));
    }
}
