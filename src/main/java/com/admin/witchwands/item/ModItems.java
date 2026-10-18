package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.IceWandConstants;
import static com.admin.witchwands.ModConstants.WandConstants.*;

import org.slf4j.Logger;

import com.admin.witchwands.ModConstants.FireWandConstants;
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
                            ShovelItem.createAttributeModifiers(MELEE_MATERIAL,
                                    MELEE_BASE_DAMAGE, ATTACK_SPEED))));

    public static final Item ICE_WAND = WitchWands.registerItem(IceWandConstants.NAME,
            new IceWandItem(new Item.Settings().maxCount(IceWandConstants.MAX_STACK_SIZE)
                    .attributeModifiers(
                            ShovelItem.createAttributeModifiers(MELEE_MATERIAL,
                                    MELEE_BASE_DAMAGE, ATTACK_SPEED))));

    public static void initialize() {
        LOGGER.info("Registering items!");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(x -> x.add(FIRE_WAND));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(x -> x.add(ICE_WAND));
    }
}
