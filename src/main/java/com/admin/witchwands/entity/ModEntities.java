package com.admin.witchwands.entity;

import net.minecraft.registry.Registry;

import com.admin.witchwands.WitchWands;
import static com.admin.witchwands.ModConstants.WitchConstants.*;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.registry.Registries;

public class ModEntities {
    public static final EntityType<WandWitchEntity> WAND_WITCH = Registry.register(Registries.ENTITY_TYPE,
            WitchWands.id(NAME),
            EntityType.Builder.create(WandWitchEntity::new, SpawnGroup.MONSTER)
                    .dimensions(DIMENSIONS_WIDTH, DIMENSIONS_HEIGHT)
                    .build(NAME));

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(WAND_WITCH,
                WitchEntity.createWitchAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, FOLLOW_RANGE));
    }
}
