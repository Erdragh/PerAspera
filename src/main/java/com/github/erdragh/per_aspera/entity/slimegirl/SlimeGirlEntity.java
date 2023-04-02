package com.github.erdragh.per_aspera.entity.slimegirl;

import me.alphamode.forgetags.Tags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SlimeGirlEntity extends PathfinderMob {

    public static final float BASE_SIZE = .25f;
    public static final int MAX_SIZE = 8;
    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(SlimeGirlEntity.class, EntityDataSerializers.INT);

    public SlimeGirlEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super (entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_SIZE, 1);
    }

    protected void setSize(int size, boolean resetHealth) {
        int i = Mth.clamp(size, 1, MAX_SIZE);
        this.entityData.set(ID_SIZE, i);
        this.reapplyPosition();
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(i * 5);

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.05f + Math.log(i+1) * 0.1f);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }
        this.xpReward = i;
    }

    public int getSize() {
        return this.entityData.get(ID_SIZE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Size", this.getSize() - 1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setSize(compound.getInt("Size") + 1, false);
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void refreshDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.refreshDimensions();
        this.setPos(d, e, f);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ID_SIZE.equals(key)) {
            this.refreshDimensions();
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(BASE_SIZE * (float)this.getSize());
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f * (float)this.getSize();
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.9f * dimensions.height;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1, Ingredient.of(Tags.Items.SLIMEBALLS), false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.SLIME_SQUISH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 1.5f, this.getBbWidth() * 0.2f);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SLIME_JUMP_SMALL, .15f, 1.0f);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (stack.is(Tags.Items.SLIMEBALLS) && this.getSize() < MAX_SIZE) {
            player.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
            stack.shrink(1);
            this.heal(1);
            player.setItemInHand(hand, stack);
            float percentageHealed = this.getHealth() / this.getMaxHealth();
            this.setSize(this.getSize() + 1, false);
            this.setHealth(percentageHealed * this.getMaxHealth());
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }
}
