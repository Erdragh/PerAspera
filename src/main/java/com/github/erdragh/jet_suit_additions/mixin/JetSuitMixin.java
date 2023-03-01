package com.github.erdragh.jet_suit_additions.mixin;

import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JetSuit.class)
public class JetSuitMixin {
    @Inject(at = @At("HEAD"), method = "fly", cancellable = true)
    public void fly(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        System.out.println("Jet Suit Fly");
        ci.cancel();
    }
}
