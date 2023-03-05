package com.github.erdragh.jet_suit_additions.mixin;

import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JetSuit.class)
public class JetSuitMixin {
    @Inject(method = "spawnParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/model/HumanoidModel;)V", at = @At("HEAD"), cancellable = true)
    private static void spawnParticles(Level world, LivingEntity entity, HumanoidModel<LivingEntity> model, CallbackInfo ci) {
        if (entity instanceof Player player) {
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            CompoundTag nbt = chest.getOrCreateTag();
            if (nbt.contains("spawn_particles")) {
                if (!nbt.getBoolean("spawn_particles")) {
                    return;
                }
            } else {
                return;
            }
            ImprovedJetSuit.spawnParticles(world, entity, chest, model.rightArm.xRot + 0.05, entity.isFallFlying() ? 0.0 : 0.8, -0.45);
            ImprovedJetSuit.spawnParticles(world, entity, chest, model.leftArm.xRot + 0.05, entity.isFallFlying() ? 0.0 : 0.8, 0.45);

            ImprovedJetSuit.spawnParticles(world, entity, chest, model.rightLeg.xRot + 0.05, entity.isFallFlying() ? 0.1 : 0.0, -0.1);
            ImprovedJetSuit.spawnParticles(world, entity, chest, model.leftLeg.xRot + 0.05, entity.isFallFlying() ? 0.1 : 0.0, 0.1);
            ci.cancel();
        }
    }
}
