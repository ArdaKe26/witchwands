package com.admin.witchwands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.admin.witchwands.item.ModItems;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WitchWands implements ModInitializer {
	public static final String MOD_ID = "witchwands";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, id(name), item);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("WitchWands mod is Starting!!");

		ModItems.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
