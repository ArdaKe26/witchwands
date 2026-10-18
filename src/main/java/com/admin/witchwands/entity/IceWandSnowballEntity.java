package com.admin.witchwands.entity;

import static com.admin.witchwands.ModConstants.IceWandConstants.*;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class IceWandSnowballEntity extends SnowballEntity {
    public IceWandSnowballEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!getWorld().isClient) {
            entityHitResult.getEntity().damage(getDamageSources().thrown(this, getOwner()), SNOWBALL_DAMAGE);
            if (entityHitResult.getEntity() instanceof LivingEntity entity) {
                entity.addStatusEffect(
                        new StatusEffectInstance(StatusEffects.SLOWNESS, SLOWNESS_DURATION_TICKS, SLOWNESS_AMPLIFIER));
            }
        }
    }
}
