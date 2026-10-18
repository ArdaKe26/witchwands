package com.admin.witchwands.item;

import static com.admin.witchwands.ModConstants.IceWandConstants.*;
import com.admin.witchwands.entity.IceWandSnowballEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class IceWandItem extends WandItem {
    public IceWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world instanceof ServerWorld serverWorld) {
            IceWandSnowballEntity iceWandSnowballEntity = new IceWandSnowballEntity(serverWorld, user);

            iceWandSnowballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, SNOWBALL_SPEED,
                    SNOWBALL_DIVERGENCE);
            serverWorld.spawnEntity(iceWandSnowballEntity);
        }

        return TypedActionResult.success(itemStack);
    }
}
