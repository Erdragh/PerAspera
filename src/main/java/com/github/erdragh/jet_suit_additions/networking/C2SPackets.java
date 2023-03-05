package com.github.erdragh.jet_suit_additions.networking;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import com.github.erdragh.jet_suit_additions.particle.JetSuitParticles;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.UUID;

public class C2SPackets {

    public static final ResourceLocation TOGGLE_ON = new ResourceLocation(JetSuitAdditions.MODID, "toggle_on");
    public static final ResourceLocation TOGGLE_HOVER = new ResourceLocation(JetSuitAdditions.MODID, "toggle_hover");

    public static final ResourceLocation CHANGE_JET_PARTICLE = new ResourceLocation(JetSuitAdditions.MODID, "change_jet_particle");


    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_ON, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateTag().putBoolean("toggle_on", !chestStack.getOrCreateTag().getBoolean("toggle_on"));
                Component text = new TranslatableComponent(JetSuitAdditions.MODID + ".msg.jet_suit_toggle").append(new TranslatableComponent(JetSuitAdditions.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateTag().getBoolean("toggle_on") ? "on" : "off")));

                player.sendMessage(text, ChatType.SYSTEM, null);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_HOVER, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateTag().putBoolean("toggle_hover", !chestStack.getOrCreateTag().getBoolean("toggle_hover"));
                Component text = new TranslatableComponent(JetSuitAdditions.MODID + ".msg.jet_suit_toggle_hover").append(new TranslatableComponent(JetSuitAdditions.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateTag().getBoolean("toggle_hover") ? "on" : "off")));

                player.sendMessage(text, ChatType.SYSTEM, null);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_JET_PARTICLE, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                var particleType = buf.readEnum(JetSuitParticles.class);
                chestStack.getOrCreateTag().putString("particle_type", particleType.getIdentifier());
            }
        });
    }

}
