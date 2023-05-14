package com.github.erdragh.per_aspera.items.armour;

import com.github.alexnijjar.ad_astra.items.armour.SpaceSuit;
import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.config.PerAsperaConfig;
import com.github.erdragh.per_aspera.networking.C2SPackets;
import com.github.erdragh.per_aspera.particle.JetSuitParticles;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class ThrusterBoots extends SpaceSuit {

    private static final Random particleRandom = new Random();

    public ThrusterBoots() {
        super(PerAspera.THRUSTER_BOOTS_MATERIAL, EquipmentSlot.FEET, new FabricItemSettings().group(CreativeModeTab.TAB_TRANSPORTATION));
    }

    public static boolean playerIsWearingThrusterBoots(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET).is(PerAspera.THRUSTER_BOOTS);
    }

    public static ItemStack getWornThrusterBoots(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET);
    }

    public static void boostPlayer(Player player, ItemStack thrusterBoots) {
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get()
                && !player.getCooldowns().isOnCooldown(PerAspera.THRUSTER_BOOTS)
                && thrusterBoots.getOrCreateTag().getBoolean("toggle_on")
        ) {
            if (player.level.isClientSide()) {
                player.setDeltaMovement(player.getDeltaMovement().add(0.0, PerAsperaConfig.THRUSTER_BOOTS_JUMP_STRENGTH.get(), 0.0));
                player.getCooldowns().addCooldown(PerAspera.THRUSTER_BOOTS, PerAsperaConfig.THRUSTER_BOOTS_TIMEOUT_TICKS.get());
                ClientPlayNetworking.send(C2SPackets.PLAYER_BOOSTED_C2S, PacketByteBufs.create());
            }
        }
    }

    public static void spawnParticleBurst(Vec3 position, ClientLevel level) {
        for (int i = 0; i < 180; i++) {
            double particleSpeedMultiplier = particleRandom.nextDouble() * 0.1;
            Vec3 particleSpeed = new Vec3(particleRandom.nextDouble() - 0.5, .2, particleRandom.nextDouble() - 0.5).normalize().multiply(particleSpeedMultiplier, particleSpeedMultiplier, particleSpeedMultiplier);
            level.addParticle(JetSuitParticles.SOUL_FIRE_FLAME.get(), true, position.x, position.y, position.z, particleSpeed.x, particleSpeed.y, particleSpeed.z);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        if (stack.is(PerAspera.THRUSTER_BOOTS)) {
            boolean turnedOn = stack.getOrCreateTag().getBoolean("toggle_on");
            Component turnedOnText = new TranslatableComponent(PerAspera.MODID + ".msg.thruster_boots_toggle").append(new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_" + (turnedOn ? "on" : "off")).setStyle(Style.EMPTY.withBold(true).withColor(turnedOn ? ChatFormatting.GREEN : ChatFormatting.RED)));
            tooltip.add(turnedOnText);
        }
    }
}
