package com.github.erdragh.per_aspera.networking;

import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.items.armour.ThrusterBoots;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class S2CPackets {

    public static final ResourceLocation PLAYER_BOOSTED_S2C = new ResourceLocation(PerAspera.MODID, "s2c_player_boosted");

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PLAYER_BOOSTED_S2C, (client, packetListener, buf, packetSender) -> {
            if (client.level == null) return;
            var player = client.level.getPlayerByUUID(buf.readUUID());
            if (player == null) return;
            ThrusterBoots.spawnParticleBurst(player.position(), client.level);
        });
    }
}
