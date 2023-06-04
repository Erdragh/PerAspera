package com.github.erdragh.per_aspera.mixin;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.util.ModKeyBindings;
import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.config.PerAsperaConfig;
import com.github.erdragh.per_aspera.items.armour.ImprovedJetSuit;
import com.github.erdragh.per_aspera.items.armour.ThrusterBoots;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private static boolean jumpLastTick = false;

    @Inject(method = "tick", at = @At("TAIL"))
    public void jet_suit_additions_tick(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (AdAstra.CONFIG.spaceSuit.enableJetSuitFlight) {
            if (!player.hasPassengers() && !ModKeyBindings.jumpKeyDown(player)) {
                ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
                if (chest.getItem() instanceof ImprovedJetSuit jetSuit && ImprovedJetSuit.hasFullSet(player) && chest.getOrCreateNbt().getBoolean("toggle_hover")) {
                    jetSuit.fly(player, chest);
                }
            }
        }
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get() && player.world.isClient) {
            if (player.isSneaking() && ThrusterBoots.playerIsWearingThrusterBoots(player) && ThrusterBoots.getWornThrusterBoots(player).getOrCreateNbt().getBoolean("toggle_on")) {
                if (player.jumping && !player.getItemCooldownManager().isCoolingDown(PerAspera.THRUSTER_BOOTS)) {
                    ThrusterBoots.chargeJump();
                }
                else if (jumpLastTick) {
                    ThrusterBoots.boostPlayer(player, ThrusterBoots.getWornThrusterBoots(player));
                }
            } else {
                ThrusterBoots.resetCharge();
            }
            jumpLastTick = player.jumping;
        }
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void jump(CallbackInfo ci) {
        var player = (PlayerEntity) (Object) this;
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get() && ThrusterBoots.playerIsWearingThrusterBoots(player) && ThrusterBoots.getWornThrusterBoots(player).getOrCreateNbt().getBoolean("toggle_on") && player.isSneaking()) {
            ci.cancel();
        }
    }
}
