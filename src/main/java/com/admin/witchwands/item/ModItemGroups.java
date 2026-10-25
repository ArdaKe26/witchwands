package com.admin.witchwands.item;

import com.admin.witchwands.WitchWands;
import static com.admin.witchwands.ModConstants.*;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ModItemGroups {
    public static final ItemGroup CREATIVE_TAB = WitchWands.registerItemGroup(CREATIVE_TAB_ID,
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.FIRE_WAND))
                    .displayName(Text.translatable("itemGroup.witchwands.witch_wands"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.FIRE_WAND);
                        entries.add(ModItems.ICE_WAND);
                        entries.add(ModItems.TELEPORT_WAND);
                        entries.add(ModItems.SOIL_WAND);
                        entries.add(ModItems.WAND_WITCH_SPAWN_EGG);
                    }).build());

    public static void initialize() {
        WitchWands.LOGGER.info("Registering Item Groups");
    }
}
