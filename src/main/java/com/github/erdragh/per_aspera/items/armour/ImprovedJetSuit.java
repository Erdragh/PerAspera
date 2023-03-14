package com.github.erdragh.per_aspera.items.armour;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import com.github.alexnijjar.ad_astra.registry.ModItems;
import com.github.alexnijjar.ad_astra.util.ModKeyBindings;
import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.particle.JetSuitParticles;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ImprovedJetSuit extends JetSuit {

    public static final int HOVER_SINK_SPEED = 1;

    public ImprovedJetSuit(ArmorMaterial material, EquipmentSlot slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void fly(Player player, ItemStack stack) {
        // Don't fly if the Jet Suit is disabled
        if (!stack.getOrCreateTag().getBoolean("toggle_on")) {
            stack.getOrCreateTag().putBoolean("spawn_particles", false);
            return;
        }
        super.fly(player, stack);
    }

    @Override
    public void fallFly(Player player, ItemStack stack) {
        if (!ModKeyBindings.jumpKeyDown(player)) {
            if (player.isOnGround()) {
                stack.getOrCreateTag().putBoolean("spawn_particles", false);
                return;
            } else {
                hover(player, stack);
                return;
            }
        }
        super.fallFly(player, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        if (stack.is(ModItems.JET_SUIT)) {
            boolean turnedOn = stack.getOrCreateTag().getBoolean("toggle_on");
            boolean hoverOn = stack.getOrCreateTag().getBoolean("toggle_hover");
            Component turnedOnText = new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_toggle").append(new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_" + (turnedOn ? "on" : "off")).setStyle(Style.EMPTY.withBold(true).withColor(turnedOn ? ChatFormatting.GREEN : ChatFormatting.RED)));
            Component hoverOnText = new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_toggle_hover").append(new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_" + (hoverOn ? "on" : "off")).setStyle(Style.EMPTY.withBold(true).withColor(hoverOn ? ChatFormatting.GREEN : ChatFormatting.RED)));
            tooltip.add(turnedOnText);
            tooltip.add(hoverOnText);
        }
    }

    @Override
    public void hover(Player player, ItemStack stack) {
        if (ModKeyBindings.jumpKeyDown(player)) {
            super.hover(player, stack);
        } else {
            double speed;
            if (player.isCrouching()) {
                speed = -AdAstra.CONFIG.spaceSuit.jetSuitUpwardsSpeed;
            } else {
                speed = -player.getDeltaMovement().y() / HOVER_SINK_SPEED;
            }
            if (!player.isOnGround()) {
                hover(player, stack, speed);
            } else {
                stack.getOrCreateTag().putBoolean("spawn_particles", false);
            }
        }
    }

    private void hover(Player player, ItemStack stack, double speed) {
        player.fallDistance /= 2;
        if (!player.isCreative() && !this.tryUseEnergy(stack, AdAstra.CONFIG.spaceSuit.jetSuitEnergyPerTick)) {
            this.setStoredEnergy(stack, 0);
        }
        isFallFlying = false;

        player.setDeltaMovement(player.getDeltaMovement().add(0.0, speed, 0.0));
        if (Math.abs(player.getDeltaMovement().y()) > Math.abs(speed)) {
            player.setDeltaMovement(player.getDeltaMovement().x(), speed, player.getDeltaMovement().z());
        }
    }

    public static void spawnParticles(Level world, LivingEntity entity, ItemStack chest, double pitch, double yOffset, double zOffset) {
        double yaw = entity.yBodyRot;
        double xRotator = Math.cos(yaw * Math.PI / 180.0) * zOffset;
        double zRotator = Math.sin(yaw * Math.PI / 180.0) * zOffset;
        double xRotator1 = Math.cos((yaw - 90) * Math.PI / 180.0) * pitch;
        double zRotator1 = Math.sin((yaw - 90) * Math.PI / 180.0) * pitch;

        var particleType = JetSuitParticles.fromIdentifier(chest.getOrCreateTag().getString("particle_type")).get();

        var particleVelocity = new Vec3(0,0,0);
        if (chest.getOrCreateTag().getString("particle_type").equals("colored_exhaust")) {
            particleVelocity.add(0.5, 0, 0);
        }

        world.addParticle(particleType, true, entity.getX() + xRotator + xRotator1, entity.getY() + yOffset, entity.getZ() + zRotator1 + zRotator, particleVelocity.x(), particleVelocity.y(), particleVelocity.z());
    }

}
