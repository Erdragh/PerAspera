package com.github.erdragh.jet_suit_additions.networking;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class C2SPackets {

    public static final Identifier TOGGLE_ON = new Identifier(JetSuitAdditions.MODID, "toggle_on");
    public static final Identifier TOGGLE_HOVER = new Identifier(JetSuitAdditions.MODID, "toggle_hover");


    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_ON, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateNbt().putBoolean("toggle_on", !chestStack.getOrCreateNbt().getBoolean("toggle_on"));
                Text text = new TranslatableText(JetSuitAdditions.MODID + ".msg.jet_suit_toggle").append(new TranslatableText(JetSuitAdditions.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateNbt().getBoolean("toggle_on") ? "on" : "off")));

                player.sendMessage(text, true);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_HOVER, (server, player, handler, buf, responseSender) -> {
            var chestStack = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
            if (chestStack.getItem() instanceof ImprovedJetSuit) {
                chestStack.getOrCreateNbt().putBoolean("toggle_hover", !chestStack.getOrCreateNbt().getBoolean("toggle_hover"));
                Text text = new TranslatableText(JetSuitAdditions.MODID + ".msg.jet_suit_toggle_hover").append(new TranslatableText(JetSuitAdditions.MODID + ".msg.jet_suit_" + (chestStack.getOrCreateNbt().getBoolean("toggle_hover") ? "on" : "off")));

                player.sendMessage(text, true);
            }
        });
    }

}
