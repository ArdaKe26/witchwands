package com.admin.witchwands.item;

import java.util.List;

import static com.admin.witchwands.ModConstants.FireWandConstants.*;
import com.admin.witchwands.entity.FireWandFireballEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireWandItem extends Item {

    public FireWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            if (user.isSneaking()) {
                List<LivingEntity> mobsNearby = world.getEntitiesByClass(LivingEntity.class,
                        user.getBoundingBox().expand(IGNITE_RADIUS),
                        entity -> entity != user
                                && entity.distanceTo(user) <= IGNITE_RADIUS);

                for (LivingEntity entity : mobsNearby) {
                    entity.setOnFireFor(FIRE_DURATION_SECONDS);
                }
            } else {
                Vec3d playerLookDirection = user.getRotationVec(1.0f);

                FireWandFireballEntity fireWandFireball = new FireWandFireballEntity(world, user, playerLookDirection);
                fireWandFireball.setPos(user.getX(), user.getEyeY(), user.getZ());
                world.spawnEntity(fireWandFireball);
            }
        }
        return TypedActionResult.success(itemStack);
    }
}
