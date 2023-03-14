package com.github.erdragh.jet_suit_additions.mixin;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.registry.ModFluids;
import com.github.alexnijjar.ad_astra.util.ModUtils;
import com.github.alexnijjar.ad_astra.util.entity.OxygenUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item implements DispensibleContainerItem {
    @Shadow
    @Final
    private Fluid content;

    @Shadow
    public static ItemStack getEmptySuccessItem(ItemStack bucketStack, Player player) {
        return null;
    }

    @Shadow protected abstract void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos);

    public BucketItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void perAspera$use(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, this.content == Fluids.EMPTY ? net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY : net.minecraft.world.level.ClipContext.Fluid.NONE);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            cir.setReturnValue(InteractionResultHolder.pass(itemStack));
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            cir.setReturnValue(InteractionResultHolder.pass(itemStack));
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos2, direction, itemStack)) {
                BlockState blockState;
                if (this.content == Fluids.EMPTY) {
                    blockState = level.getBlockState(blockPos);
                    if (blockState.getBlock() instanceof BucketPickup) {
                        BucketPickup bucketPickup = (BucketPickup) blockState.getBlock();
                        ItemStack itemStack2 = bucketPickup.pickupBlock(level, blockPos, blockState);
                        if (!itemStack2.isEmpty()) {
                            player.awardStat(Stats.ITEM_USED.get(this));
                            bucketPickup.getPickupSound().ifPresent((sound) -> {
                                player.playSound(sound, 1.0F, 1.0F);
                            });
                            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2);
                            if (!level.isClientSide) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemStack2);
                            }

                            cir.setReturnValue(InteractionResultHolder.sidedSuccess(itemStack3, level.isClientSide()));
                        }
                    }

                    cir.setReturnValue(InteractionResultHolder.fail(itemStack));
                } else {
                    blockState = level.getBlockState(blockPos);
                    BlockPos blockPos3 = blockState.getBlock() instanceof LiquidBlockContainer && this.content == Fluids.WATER ? blockPos : blockPos2;



                    if (placeFluid(player, level, blockPos3, blockHitResult)) {
                        this.checkExtraContent(player, level, itemStack, blockPos3);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos3, itemStack);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        cir.setReturnValue(InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemStack, player), level.isClientSide()));
                        cir.cancel();
                    } else {
                        cir.setReturnValue(InteractionResultHolder.fail(itemStack));
                    }
                }
            } else {
                cir.setReturnValue(InteractionResultHolder.fail(itemStack));
            }
        }
    }
    private boolean placeFluid(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result) {
        if (AdAstra.CONFIG.general.doOxygen && ModUtils.isSpaceWorld(level)) {
            if (!OxygenUtils.posHasOxygen(level, pos) && (this.content.equals(Fluids.WATER) || this.content.equals(Fluids.FLOWING_WATER))) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                if (ModUtils.getWorldTemperature(level) < 0) {
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir()) {
                        level.setBlock(pos, Blocks.ICE.defaultBlockState(), level.isClientSide ? 11 : 3);
                    }

                }
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (level.random.nextFloat() - level.random.nextFloat()) * 0.8f);
                for (int l = 0; l < 8; ++l) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
                }
                return true;
            }
        }
        if (!(this.content instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState blockState = level.getBlockState(pos);
            Block block = blockState.getBlock();
            Material material = blockState.getMaterial();
            boolean bl = blockState.canBeReplaced(this.content);
            boolean bl2 = blockState.isAir() || bl || block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(level, pos, blockState, this.content);
            if (!bl2) {
                return result != null && placeFluid(player, level, result.getBlockPos().relative(result.getDirection()), (BlockHitResult)null);
            } else if (level.dimensionType().ultraWarm() && this.content.is(FluidTags.WATER)) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0, 0.0, 0.0);
                }

                return true;
            } else if (block instanceof LiquidBlockContainer && this.content == Fluids.WATER) {
                ((LiquidBlockContainer)block).placeLiquid(level, pos, blockState, ((FlowingFluid)this.content).getSource(false));
                playEmptySound(player, level, pos);
                return true;
            } else {
                if (!level.isClientSide && bl && !material.isLiquid()) {
                    level.destroyBlock(pos, true);
                }

                if (!level.setBlock(pos, this.content.defaultFluidState().createLegacyBlock(), 11) && !blockState.getFluidState().isSource()) {
                    return false;
                } else {
                    playEmptySound(player, level, pos);
                    return true;
                }
            }
        }
    }

}
