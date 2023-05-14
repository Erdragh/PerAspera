package com.github.erdragh.per_aspera.mixin;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.util.ModKeyBindings;
import com.github.erdragh.per_aspera.config.PerAsperaConfig;
import com.github.erdragh.per_aspera.items.armour.ImprovedJetSuit;
import com.github.erdragh.per_aspera.items.armour.ThrusterBoots;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void jet_suit_additions_tick(CallbackInfo ci) {
        if (AdAstra.CONFIG.spaceSuit.enableJetSuitFlight) {
            Player player = ((Player) (Object) this);
            if (!player.isVehicle() && !ModKeyBindings.jumpKeyDown(player)) {
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                if (chest.getItem() instanceof ImprovedJetSuit jetSuit && ImprovedJetSuit.hasFullSet(player) && chest.getOrCreateTag().getBoolean("toggle_hover")) {
                    jetSuit.fly(player, chest);
                }
            }
        }
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get()) {
            Player player = ((Player) (Object) this);
            if (player.isCrouching() && ModKeyBindings.jumpKeyDown(player) && ThrusterBoots.playerIsWearingThrusterBoots(player)) {
                ThrusterBoots.boostPlayer(player, ThrusterBoots.getWornThrusterBoots(player));
            }
        }
    }
}
