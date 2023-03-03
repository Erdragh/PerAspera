package com.github.erdragh.jet_suit_additions.mixin;

import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JetSuit.class)
public class JetSuitMixin {
    @Inject(method = "spawnParticles(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/model/BipedEntityModel;)V", at = @At("HEAD"), cancellable = true)
    private static void spawnParticles(World world, LivingEntity entity, BipedEntityModel<LivingEntity> model, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
            NbtCompound nbt = chest.getOrCreateNbt();
            if (nbt.contains("spawn_particles")) {
                if (!nbt.getBoolean("spawn_particles")) {
                    return;
                }
            } else {
                return;
            }
            ImprovedJetSuit.spawnParticles(world, entity, chest, model.rightArm.pitch + 0.05, entity.isFallFlying() ? 0.0 : 0.8, -0.45);
            ImprovedJetSuit.spawnParticles(world, entity, chest, model.leftArm.pitch + 0.05, entity.isFallFlying() ? 0.0 : 0.8, 0.45);

            ImprovedJetSuit.spawnParticles(world, entity, chest, model.rightLeg.pitch + 0.05, entity.isFallFlying() ? 0.1 : 0.0, -0.1);
            ImprovedJetSuit.spawnParticles(world, entity, chest, model.leftLeg.pitch + 0.05, entity.isFallFlying() ? 0.1 : 0.0, 0.1);
            ci.cancel();
        }
    }
}
