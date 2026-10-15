package com.admin.witchwands;

import net.minecraft.item.ToolMaterials;

public class ModConstants {
    public static final class FireWandConstants {
        public static final int MAX_STACK_SIZE = 1;
        public static final String NAME = "fire_wand";
        public static final float FIREBALL_POWER = 2.0f;
        public static final double IGNITE_RADIUS = 25;
        public static final float FIRE_DURATION_SECONDS = 5.0f;
        public static final ToolMaterials MELEE_MATERIAL = ToolMaterials.IRON;
        public static final float MELEE_BASE_DAMAGE = 1.5f;
        public static final float ATTACK_SPEED = -3.0f;
        public static final int FIREBALL_COOLDOWN_TICKS = 5 * 20;
        public static final int IGNITE_COOLDOWN_TICKS = 20 * 20;

    }
}
