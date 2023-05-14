package com.github.erdragh.per_aspera.networking;

import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.items.armour.ImprovedJetSuit;
import com.github.erdragh.per_aspera.items.armour.ThrusterBoots;
import com.github.erdragh.per_aspera.particle.JetSuitParticles;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class C2SPackets {

    public static final ResourceLocation TOGGLE_ON = new ResourceLocation(PerAspera.MODID, "toggle_on");
    public static final ResourceLocation TOGGLE_HOVER = new ResourceLocation(PerAspera.MODID, "toggle_hover");

    public static final ResourceLocation CHANGE_JET_PARTICLE = new ResourceLocation(PerAspera.MODID, "change_jet_particle");

    public static final ResourceLocation PLAYER_BOOSTED_C2S = new ResourceLocation(PerAspera.MODID, "c2s_player_boosted");


    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_ON, (server, player, handler, buf, responseSender) -> {
            var thrusterBoots = ThrusterBoots.getWornThrusterBoots(player);
            if (thrusterBoots.is(PerAspera.THRUSTER_BOOTS)) {
                thrusterBoots.getOrCreateTag().putBoolean("toggle_on", !thrusterBoots.getOrCreateTag().getBoolean("toggle_on"));
                Component text = new TranslatableComponent(PerAspera.MODID + ".msg.thruster_boots_toggle").append(new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_" + (thrusterBoots.getOrCreateTag().getBoolean("toggle_on") ? "on" : "off")));

                player.displayClientMessage(text, true);
                return;
            }
            var chestStack = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateTag().putBoolean("toggle_on", !chestStack.getOrCreateTag().getBoolean("toggle_on"));
                Component text = new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_toggle").append(new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateTag().getBoolean("toggle_on") ? "on" : "off")));

                player.displayClientMessage(text, true);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(PLAYER_BOOSTED_C2S, (server, player, handler, buf, responseSender) -> {
            var packet = ServerPlayNetworking.createS2CPacket(S2CPackets.PLAYER_BOOSTED_S2C,  new FriendlyByteBuf(PacketByteBufs.create()).writeUUID(player.getUUID()));
            server.getPlayerList().broadcastAll(packet, player.level.dimension());
        });
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_HOVER, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateTag().putBoolean("toggle_hover", !chestStack.getOrCreateTag().getBoolean("toggle_hover"));
                Component text = new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_toggle_hover").append(new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateTag().getBoolean("toggle_hover") ? "on" : "off")));

                player.displayClientMessage(text, true);
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
