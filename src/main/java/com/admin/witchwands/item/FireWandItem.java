package com.admin.witchwands.item;

import com.admin.witchwands.ModConstants;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
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
            Vec3d playerLookDirection = user.getRotationVec(1.0f);

            FireballEntity fireWandFireball = new FireballEntity(world, user, playerLookDirection,
                    ModConstants.FireWandConstants.FIREBALL_POWER);
            fireWandFireball.setPos(user.getX(), user.getEyeY(), user.getZ());
            world.spawnEntity(fireWandFireball);
        }
        return TypedActionResult.success(itemStack);
    }
}
