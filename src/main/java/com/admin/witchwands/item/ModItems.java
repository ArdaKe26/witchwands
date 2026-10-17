package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.FireWandConstants;

import org.slf4j.Logger;

import com.admin.witchwands.WitchWands;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ShovelItem;

public class ModItems {
    public static final Logger LOGGER = WitchWands.LOGGER;

    public static final Item FIRE_WAND = WitchWands.registerItem(FireWandConstants.NAME,
            new FireWandItem(new Item.Settings().maxCount(FireWandConstants.MAX_STACK_SIZE)
                    .attributeModifiers(
                            ShovelItem.createAttributeModifiers(FireWandConstants.MELEE_MATERIAL,
                                    FireWandConstants.MELEE_BASE_DAMAGE, FireWandConstants.ATTACK_SPEED))));

    public static void initialize() {
        LOGGER.info("Registering items!");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(x -> x.add(FIRE_WAND));
    }
}
