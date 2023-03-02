package com.github.erdragh.jet_suit_additions.mixin;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.util.ModKeyBindings;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void jet_suit_additions_tick(CallbackInfo ci) {
        if (AdAstra.CONFIG.spaceSuit.enableJetSuitFlight) {
            PlayerEntity player = ((PlayerEntity) (Object) this);
            if (!player.hasVehicle() && !ModKeyBindings.jumpKeyDown(player)) {
                ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
                if (chest.getItem() instanceof ImprovedJetSuit jetSuit && ImprovedJetSuit.hasFullSet(player) && chest.getOrCreateNbt().getBoolean("toggle_hover")) {
                    jetSuit.fly(player, chest);
                }
            }
        }
    }
}
