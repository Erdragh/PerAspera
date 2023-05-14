package com.github.erdragh.per_aspera.mixin;

import com.github.alexnijjar.ad_astra.AdAstra;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item implements FluidModificationItem {

    @Shadow @Final private Fluid fluid;
    private boolean hasTempDisabledOxygen = false, previousValue = false;

    public BucketItemMixin(Settings properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At("HEAD"))
    public void perAspera$use_HEAD(World level, PlayerEntity player, Hand usedHand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!hasTempDisabledOxygen) previousValue = AdAstra.CONFIG.general.doOxygen;
        if (this.fluid != Fluids.WATER && this.fluid != Fluids.FLOWING_WATER) {
            AdAstra.CONFIG.general.doOxygen = false;
        }
        this.hasTempDisabledOxygen = true;
    }

    @Inject(method = "use", at = @At("RETURN"))
    public void perAspera$use_AFTER(World level, PlayerEntity player, Hand usedHand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        AdAstra.CONFIG.general.doOxygen = previousValue;
        hasTempDisabledOxygen = false;
    }

}
