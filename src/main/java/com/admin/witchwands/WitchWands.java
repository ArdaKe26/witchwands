package com.admin.witchwands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.admin.witchwands.entity.ModEntities;
import com.admin.witchwands.item.ModItemGroups;
import com.admin.witchwands.item.ModItems;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WitchWands implements ModInitializer {
	public static final String MOD_ID = "witchwands";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, id(name), item);
	}

	public static ItemGroup registerItemGroup(String name, ItemGroup itemGroup) {
		return Registry.register(Registries.ITEM_GROUP, id(name), itemGroup);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("WitchWands mod is Starting!!");

		ModItems.initialize();
		ModItemGroups.initialize();
		ModEntities.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
