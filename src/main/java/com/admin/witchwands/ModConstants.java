package com.admin.witchwands;

import net.minecraft.item.ToolMaterials;

public class ModConstants {
    public static final class FireWandConstants {
        public static final int MAX_STACK_SIZE = 1;
        public static final String NAME = "fire_wand";
        public static final float FIREBALL_POWER = 2.0f;
        public static final double IGNITE_RADIUS = 25;
        public static final float FIRE_DURATION_SECONDS = 5.0f;
        public static final int FIREBALL_COOLDOWN_TICKS = 5 * 20;
        public static final int IGNITE_COOLDOWN_TICKS = 20 * 20;
        public static final int IGNITE_PARTICLE_COUNT = 15;
        public static final int FIREBALL_FLAME_PARTICLE_COUNT = 10;
        public static final int FIREBALL_SMOKE_PARTICLE_COUNT = 8;
        public static final double IGNITE_PARTICLE_SPEED = 0.02;
        public static final double FIREBALL_FLAME_PARTICLE_SPEED = 0.05;
        public static final double FIREBALL_SMOKE_PARTICLE_SPEED = 0.02;
        public static final double FIREBALL_FLAME_PARTICLE_SPREAD = 0.2;
        public static final double FIREBALL_SMOKE_PARTICLE_SPREAD = 0.3;
    }

    public static final class IceWandConstants {
        public static final int MAX_STACK_SIZE = 1;
        public static final String NAME = "ice_wand";
        public static final float SNOWBALL_DAMAGE = 1.0f;
        public static final int SLOWNESS_DURATION_TICKS = 3 * 20;
        public static final int SLOWNESS_AMPLIFIER = 0;
        public static final float SNOWBALL_SPEED = 1.5f;
        public static final float SNOWBALL_DIVERGENCE = 1.0f;
    }

    public static final class WandConstants {
        public static final float READY_SOUND_VOLUME = 0.5f;
        public static final float READY_SOUND_PITCH = 1.4f;
        public static final float FIZZLE_SOUND_VOLUME = 0.4f;
        public static final float FIZZLE_SOUND_PITCH = 1.8f;

        public static final ToolMaterials MELEE_MATERIAL = ToolMaterials.IRON;
        public static final float MELEE_BASE_DAMAGE = 1.5f;
        public static final float ATTACK_SPEED = -3.0f;
    }

    public static final String CREATIVE_TAB_ID = "witch_wands";
}
