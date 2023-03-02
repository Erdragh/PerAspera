package com.github.erdragh.jet_suit_additions.networking;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class C2SPackets {

    public static final Identifier TOGGLE_HOVER = new Identifier(JetSuitAdditions.MODID, "toggle_hover");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_HOVER, (server, player, handler, buf, responseSender) -> {
            System.out.println(player.getName());
        });
    }

}
