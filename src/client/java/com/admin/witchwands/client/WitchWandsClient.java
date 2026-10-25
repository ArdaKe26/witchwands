package com.admin.witchwands.client;

import com.admin.witchwands.entity.ModEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.WitchEntityRenderer;

public class WitchWandsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.WAND_WITCH, WitchEntityRenderer::new);
	}
}