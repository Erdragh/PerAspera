package com.github.erdragh.per_aspera.networking;

import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.items.armour.ThrusterBoots;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class S2CPackets {

    public static final Identifier PLAYER_BOOSTED_S2C = new Identifier(PerAspera.MODID, "s2c_player_boosted");

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PLAYER_BOOSTED_S2C, (client, packetListener, buf, packetSender) -> {
            if (client.world == null) return;
            var player = client.world.getPlayerByUuid(buf.readUuid());
            if (player == null) return;
            ThrusterBoots.spawnParticleBurst(player.getPos(), client.world);
        });
    }
}
