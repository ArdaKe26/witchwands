package com.admin.witchwands.entity;

import java.util.List;
import java.util.Map;

import static com.admin.witchwands.ModConstants.WitchConstants.*;

import com.admin.witchwands.ModConstants.IceWandConstants;
import com.admin.witchwands.ModConstants.TeleportWandConstants;
import com.admin.witchwands.item.ModItems;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class WandWitchEntity extends WitchEntity {
    private static final Map<Item, String> ITEM_TO_NAME = Map.of(ModItems.FIRE_WAND, FIRE_WITCH_NAME, ModItems.ICE_WAND,
            ICE_WITCH_NAME,
            ModItems.TELEPORT_WAND, TELEPORT_WITCH_NAME, ModItems.SOIL_WAND, SOIL_WITCH_NAME);
    private static final List<Item> WAND_LIST = List.of(ModItems.FIRE_WAND, ModItems.ICE_WAND,
            ModItems.TELEPORT_WAND, ModItems.SOIL_WAND);

    private Item wieldingWand;
    private long nextPullTime;
    private long nextBlinkTime;
    private long nextHealTime;

    public WandWitchEntity(EntityType<? extends WitchEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setWand(Item wand) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(wand));

        this.setCustomName(Text.literal(ITEM_TO_NAME.get(wand)));

        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, WAND_DROP_CHANCE);

        wieldingWand = wand;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
            EntityData entityData) {
        int randomWand = world.getRandom().nextInt(WAND_LIST.size());

        setWand(WAND_LIST.get(randomWand));

        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (wieldingWand != null) {
            if (!this.isDrinking()) {
                if (!this.getMainHandStack().isOf(wieldingWand)) {
                    setWand(wieldingWand);
                }
            }

            if (!this.getWorld().isClient) {
                if (wieldingWand == ModItems.TELEPORT_WAND) {
                    if (this.getTarget() != null) {
                        if (this.getWorld().getTime() >= nextPullTime) {
                            if (this.squaredDistanceTo(this.getTarget()) < WITCH_PULL_MAX_DISTANCE
                                    * WITCH_PULL_MAX_DISTANCE
                                    && this.squaredDistanceTo(this.getTarget()) > WITCH_PULL_MIN_DISTANCE
                                            * WITCH_PULL_MIN_DISTANCE) {
                                if (this.canSee(this.getTarget())) {
                                    Vec3d targetPos = this.getTarget().getEyePos();
                                    Vec3d witchPos = this.getEyePos();

                                    Vec3d shootDirection = targetPos.subtract(witchPos);

                                    Vec3d pullVec = new Vec3d(shootDirection.getX(), 0, shootDirection.getZ())
                                            .normalize();

                                    Vec3d landingSpot = this.getPos()
                                            .add(pullVec.multiply(WITCH_PULL_LANDING_DISTANCE));

                                    if (this.getTarget().teleport(landingSpot.getX(), landingSpot.getY(),
                                            landingSpot.getZ(), true)) {
                                        nextPullTime = this.getWorld().getTime() + WITCH_PULL_COOLDOWN_TICKS;

                                        if (this.getTarget() instanceof PlayerEntity player) {
                                            player.getItemCooldownManager().set(Items.ENDER_PEARL,
                                                    TeleportWandConstants.PEARL_LOCK_TICKS);
                                        }

                                        if (this.getWorld() instanceof ServerWorld serverWorld) {
                                            serverWorld.spawnParticles(ParticleTypes.PORTAL, this.getX(),
                                                    this.getY(), this.getZ(),
                                                    WITCH_TELEPORT_PARTICLE_COUNT, WITCH_TELEPORT_PARTICLE_SPREAD,
                                                    WITCH_TELEPORT_PARTICLE_SPREAD, WITCH_TELEPORT_PARTICLE_SPREAD,
                                                    WITCH_TELEPORT_PARTICLE_SPEED);
                                            serverWorld.playSound(null, this.getX(), this.getY(), this.getZ(),
                                                    SoundEvents.ENTITY_PLAYER_TELEPORT,
                                                    SoundCategory.HOSTILE, WITCH_TELEPORT_SOUND_VOLUME,
                                                    WITCH_TELEPORT_SOUND_PITCH);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (wieldingWand == ModItems.SOIL_WAND) {
                    if (this.getWorld().getTime() >= nextHealTime) {
                        List<HostileEntity> damagedHostileEntities = this.getWorld().getEntitiesByClass(
                                HostileEntity.class, this.getBoundingBox().expand(WITCH_HEAL_RADIUS),
                                entity -> entity.getHealth() < entity.getMaxHealth());

                        for (HostileEntity damagedEntity : damagedHostileEntities) {
                            damagedEntity.heal(WITCH_HEAL_AMOUNT);

                            if (this.getWorld() instanceof ServerWorld serverWorld) {
                                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, damagedEntity.getX(),
                                        damagedEntity.getBodyY(0.5), damagedEntity.getZ(),
                                        WITCH_HEAL_PARTICLE_COUNT, WITCH_HEAL_PARTICLE_SPREAD,
                                        WITCH_HEAL_PARTICLE_SPREAD, WITCH_HEAL_PARTICLE_SPREAD,
                                        WITCH_HEAL_PARTICLE_SPEED);
                            }
                        }
                        if (!damagedHostileEntities.isEmpty()) {
                            if (this.getWorld() instanceof ServerWorld serverWorld) {
                                serverWorld.playSound(null, this.getX(),
                                        this.getY(), this.getZ(), SoundEvents.ITEM_BONE_MEAL_USE,
                                        SoundCategory.HOSTILE, WITCH_HEAL_SOUND_VOLUME, WITCH_HEAL_SOUND_PITCH);
                            }
                        }
                        nextHealTime = this.getWorld().getTime() + WITCH_HEAL_COOLDOWN_TICKS;
                    }
                }
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (wieldingWand != null) {
            String wieldingWandID = Registries.ITEM.getId(wieldingWand).toString();

            nbt.putString(WAND_NBT_KEY, wieldingWandID);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains(WAND_NBT_KEY)) {
            String wieldingWandID = nbt.getString(WAND_NBT_KEY);

            Identifier identifier = Identifier.of(wieldingWandID);

            wieldingWand = Registries.ITEM.get(identifier);
        }
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        if (!this.isDrinking()) {
            if (wieldingWand == ModItems.FIRE_WAND) {
                Vec3d targetPos = target.getEyePos();
                Vec3d witchPos = this.getEyePos();

                Vec3d shootDirection = targetPos.subtract(witchPos).normalize();

                FireWandFireballEntity fireWandFireball = new FireWandFireballEntity(this.getWorld(), this,
                        shootDirection);
                fireWandFireball.setPos(this.getX(), this.getEyeY(), this.getZ());
                this.getWorld().spawnEntity(fireWandFireball);

                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.FLAME, witchPos.getX(), witchPos.getY(), witchPos.getZ(),
                            WITCH_FIREBALL_PARTICLE_COUNT, WITCH_FIREBALL_PARTICLE_SPREAD,
                            WITCH_FIREBALL_PARTICLE_SPREAD, WITCH_FIREBALL_PARTICLE_SPREAD,
                            WITCH_FIREBALL_PARTICLE_SPEED);
                    serverWorld.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_FIRECHARGE_USE,
                            SoundCategory.HOSTILE, WITCH_FIREBALL_SOUND_VOLUME, WITCH_FIREBALL_SOUND_PITCH);
                }

            } else if (wieldingWand == ModItems.ICE_WAND) {
                for (int i = 0; i < SNOWBALLS_PER_ATTACK; i++) {
                    Vec3d targetPos = target.getEyePos();
                    Vec3d witchPos = this.getEyePos();

                    Vec3d shootDirection = targetPos.subtract(witchPos);

                    double raisedY = shootDirection.getY() + Math.sqrt(shootDirection.getX() * shootDirection.getX()
                            + shootDirection.getZ() * shootDirection.getZ()) * SNOWBALLS_ARC;

                    IceWandSnowballEntity iceWandSnowball = new IceWandSnowballEntity(this.getWorld(), this);
                    iceWandSnowball.setPos(this.getX(), this.getEyeY(), this.getZ());
                    iceWandSnowball.setVelocity(shootDirection.getX(), raisedY, shootDirection.getZ(),
                            IceWandConstants.SNOWBALL_SPEED, WITCH_SNOWBALLS_DIVERGENCE);
                    this.getWorld().spawnEntity(iceWandSnowball);
                }
                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getEyeY(), this.getZ(),
                            WITCH_SNOWBALL_PARTICLE_COUNT, WITCH_SNOWBALL_PARTICLE_SPREAD,
                            WITCH_SNOWBALL_PARTICLE_SPREAD, WITCH_SNOWBALL_PARTICLE_SPREAD,
                            WITCH_SNOWBALL_PARTICLE_SPEED);
                    serverWorld.playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.ENTITY_SNOWBALL_THROW,
                            SoundCategory.HOSTILE, WITCH_SNOWBALL_SOUND_VOLUME, WITCH_SNOWBALL_SOUND_PITCH);
                }

            } else if (wieldingWand == ModItems.SOIL_WAND || wieldingWand == ModItems.TELEPORT_WAND) {
                super.shootAt(target, pullProgress);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean isDamaged = super.damage(source, amount);

        if (!this.getWorld().isClient) {
            if (wieldingWand == ModItems.TELEPORT_WAND) {
                if (isDamaged) {
                    if (this.getWorld().getTime() >= nextBlinkTime) {
                        if (source.getAttacker() != null) {
                            if (this.squaredDistanceTo(source.getAttacker()) < WITCH_BLINK_TRIGGER_DISTANCE
                                    * WITCH_BLINK_TRIGGER_DISTANCE) {

                                for (int i = 0; i < WITCH_BLINK_TRIES; i++) {
                                    double randomX = this.getX()
                                            + (this.getRandom().nextDouble() - 0.5) * 2 * WITCH_BLINK_RANGE;
                                    double randomY = this.getY()
                                            + (this.getRandom().nextDouble() - 0.5) * 2 * WITCH_BLINK_EXTRA_HEIGHT;
                                    double randomZ = this.getZ()
                                            + (this.getRandom().nextDouble() - 0.5) * 2 * WITCH_BLINK_RANGE;

                                    if (this.teleport(randomX, randomY, randomZ, true)) {
                                        nextBlinkTime = this.getWorld().getTime() + WITCH_BLINK_COOLDOWN_TICKS;

                                        if (this.getWorld() instanceof ServerWorld serverWorld) {
                                            serverWorld.playSound(null, this.getTarget().getX(),
                                                    this.getTarget().getY(), this.getTarget().getZ(),
                                                    SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                                                    SoundCategory.HOSTILE, WITCH_BLINK_SOUND_VOLUME,
                                                    WITCH_BLINK_SOUND_PITCH);
                                        }
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

        return isDamaged;
    }
}
