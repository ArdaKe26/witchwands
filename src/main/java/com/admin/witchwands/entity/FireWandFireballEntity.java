package com.admin.witchwands.entity;

import static com.admin.witchwands.ModConstants.FireWandConstants.*;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;

public class FireWandFireballEntity extends FireballEntity {
    public FireWandFireballEntity(World world, LivingEntity owner, Vec3d velocity) {
        super(world, owner, velocity, 0);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!getWorld().isClient) {
            getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(),
                    FIREBALL_POWER, true, ExplosionSourceType.NONE);
            this.discard();
        }
    }
}
