package com.admin.witchwands.item;

import org.slf4j.Logger;

import com.admin.witchwands.ModConstants;
import com.admin.witchwands.WitchWands;

import net.minecraft.item.Item;

public class ModItems {
    public static final Logger LOGGER = WitchWands.LOGGER;

    public static final Item FIRE_WAND = WitchWands.registerItem(ModConstants.FireWandConstants.NAME,
            new FireWandItem(new Item.Settings().maxCount(ModConstants.FireWandConstants.MAX_STACK_SIZE)));

    public static void initialize() {
        LOGGER.info("Registering items!");
    }
}
