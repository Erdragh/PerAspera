package com.github.erdragh.per_aspera.mixin;

import com.github.alexnijjar.ad_astra.AdAstra;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item implements DispensibleContainerItem {

    @Shadow @Final private Fluid content;
    private boolean hasTempDisabledOxygen = false, previousValue = false;

    public BucketItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At("HEAD"))
    public void perAspera$use_HEAD(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!hasTempDisabledOxygen) previousValue = AdAstra.CONFIG.general.doOxygen;
        if (this.content != Fluids.WATER && this.content != Fluids.FLOWING_WATER) {
            AdAstra.CONFIG.general.doOxygen = false;
        }
        this.hasTempDisabledOxygen = true;
    }

    @Inject(method = "use", at = @At("RETURN"))
    public void perAspera$use_AFTER(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        AdAstra.CONFIG.general.doOxygen = previousValue;
        hasTempDisabledOxygen = false;
    }

}
