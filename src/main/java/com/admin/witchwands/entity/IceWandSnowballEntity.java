package com.admin.witchwands.entity;

import static com.admin.witchwands.ModConstants.IceWandConstants.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class IceWandSnowballEntity extends SnowballEntity {
    public IceWandSnowballEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (getWorld() instanceof ServerWorld serverWorld) {
            Entity target = entityHitResult.getEntity();

            serverWorld.playSound(null, target.getX(), target.getBodyY(0.5), target.getZ(), SoundEvents.BLOCK_SNOW_HIT,
                    SoundCategory.PLAYERS, SNOWBALL_HIT_VOLUME,
                    SNOWBALL_HIT_PITCH_MIN + serverWorld.getRandom().nextFloat() * SNOWBALL_HIT_PITCH_RANGE);

            serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getY(), target.getZ(),
                    SNOWBALL_HIT_PARTICLE_COUNT, SNOWBALL_HIT_PARTICLE_SPREAD, SNOWBALL_HIT_PARTICLE_SPREAD,
                    SNOWBALL_HIT_PARTICLE_SPREAD, SNOWBALL_HIT_PARTICLE_SPEED);

            target.damage(getDamageSources().thrown(this, getOwner()), SNOWBALL_DAMAGE);
            if (target instanceof LivingEntity entity) {
                entity.addStatusEffect(
                        new StatusEffectInstance(StatusEffects.SLOWNESS, SLOWNESS_DURATION_TICKS, SLOWNESS_AMPLIFIER));
            }
        }
    }
}
