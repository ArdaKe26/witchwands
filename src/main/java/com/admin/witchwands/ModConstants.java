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
        public static final int SNOWBALL_MAX_AMMO = 30;
        public static final int SNOWBALL_RELOAD_COOLDOWN_TICKS = 15 * 20;
        public static final int FREEZE_RADIUS = 25;
        public static final int FREEZE_DURATION_TICKS = 5 * 20;
        public static final int FREEZE_SLOWNESS_AMPLIFIER = 10;
        public static final int FREEZE_COOLDOWN_TICKS = 30 * 20;
        public static final float FREEZE_CASTING_VOLUME = 1.0f;
        public static final float FREEZE_CASTING_PITCH = 0.6f;
        public static final int FROZEN_CASTING_PARTICLE_COUNT = 40;
        public static final double FROZEN_CASTING_PARTICLE_SPREAD = 1.5;
        public static final double FROZEN_CASTING_PARTICLE_SPEED = 0.01;
        public static final float FROZEN_MOB_VOLUME = 0.5f;
        public static final float FROZEN_MOB_PITCH = 1.0f;
        public static final int FROZEN_MOB_PARTICLE_COUNT = 15;
        public static final double FROZEN_MOB_PARTICLE_SPEED = 0.02;
        public static final float SNOWBALL_THROW_VOLUME = 0.4f;
        public static final float SNOWBALL_THROW_PITCH_MIN = 0.8f;
        public static final float SNOWBALL_THROW_PITCH_RANGE = 0.4f;
        public static final float SNOWBALL_HIT_VOLUME = 0.5f;
        public static final float SNOWBALL_HIT_PITCH_MIN = 0.9f;
        public static final float SNOWBALL_HIT_PITCH_RANGE = 0.4f;
        public static final int SNOWBALL_HIT_PARTICLE_COUNT = 8;
        public static final double SNOWBALL_HIT_PARTICLE_SPREAD = 0.3;
        public static final double SNOWBALL_HIT_PARTICLE_SPEED = 0.05;
    }

    public static final class TeleportWandConstants {
        public static final int MAX_STACK_SIZE = 1;
        public static final String NAME = "teleport_wand";
        public static final int TELEPORT_RANGE = 30;
        public static final int TELEPORT_COOLDOWN_TICKS = 7 * 20;
        public static final int TELEPORT_PARTICLE_COUNT = 40;
        public static final double TELEPORT_PARTICLE_SPREAD = 0.5;
        public static final double TELEPORT_PARTICLE_SPEED = 0.5;
        public static final float TELEPORT_SOUND_VOLUME = 1.0f;
        public static final float TELEPORT_DEPART_SOUND_PITCH = 0.8f;
        public static final float TELEPORT_ARRIVE_SOUND_PITCH = 1.2f;
        public static final int PULL_RANGE = 30;
        public static final int PULL_COOLDOWN_TICKS = 35 * 20;
        public static final int PEARL_LOCK_TICKS = 30 * 20;
        public static final double PULL_LANDING_DISTANCE = 2.0;
    }

    public static final class SoilWandConstants {
        public static final int MAX_STACK_SIZE = 1;
        public static final String NAME = "soil_wand";
        public static final ToolMaterials MELEE_MATERIAL = ToolMaterials.DIAMOND;
        public static final float MELEE_BASE_DAMAGE = -3.0f;
        public static final float ATTACK_SPEED = 0.0f;
        public static final int FOOD_GIVE_COOLDOWN_TICKS = 10 * 20;
        public static final int FOOD_GIVE_AMOUNT = 10;
        public static final int FOOD_GIVE_RANGE = 10;
        public static final float FOOD_GIVE_SOUND_VOLUME = 1.0f;
        public static final float FOOD_GIVE_SOUND_PITCH = 1.0f;
        public static final int FOOD_GIVE_PARTICLE_COUNT = 15;
        public static final double FOOD_GIVE_PARTICLE_SPREAD = 0.3;
        public static final double FOOD_GIVE_PARTICLE_SPEED = 0.0;
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
