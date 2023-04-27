package com.github.erdragh.per_aspera.networking;

import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.items.armour.ImprovedJetSuit;
import com.github.erdragh.per_aspera.particle.JetSuitParticles;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class C2SPackets {

    public static final Identifier TOGGLE_ON = new Identifier(PerAspera.MODID, "toggle_on");
    public static final Identifier TOGGLE_HOVER = new Identifier(PerAspera.MODID, "toggle_hover");

    public static final Identifier CHANGE_JET_PARTICLE = new Identifier(PerAspera.MODID, "change_jet_particle");


    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_ON, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateNbt().putBoolean("toggle_on", !chestStack.getOrCreateNbt().getBoolean("toggle_on"));
                Text text = new TranslatableText(PerAspera.MODID + ".msg.jet_suit_toggle").append(new TranslatableText(PerAspera.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateNbt().getBoolean("toggle_on") ? "on" : "off")));

                player.sendMessage(text, true);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_HOVER, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateNbt().putBoolean("toggle_hover", !chestStack.getOrCreateNbt().getBoolean("toggle_hover"));
                Text text = new TranslatableText(PerAspera.MODID + ".msg.jet_suit_toggle_hover").append(new TranslatableText(PerAspera.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateNbt().getBoolean("toggle_hover") ? "on" : "off")));

                player.sendMessage(text, true);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_JET_PARTICLE, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                var particleType = buf.readEnumConstant(JetSuitParticles.class);
                chestStack.getOrCreateNbt().putString("particle_type", particleType.getIdentifier());
            }
        });
    }

}
