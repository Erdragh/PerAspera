package com.github.erdragh.jet_suit_additions.client;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import com.github.erdragh.jet_suit_additions.particle.ColoredJetExhaustParticle;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.particle.FlameParticle;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class JetSuitAdditionsClient implements ClientModInitializer {

    public static final String KEY_CATEGORY = JetSuitAdditions.MODID + ".key_category";

    public static KeyMapping jetSuitToggle, jetSuitHoverToggle;

    @Override
    public void onInitializeClient() {
        jetSuitToggle = KeyBindingHelper.registerKeyBinding(new KeyMapping(JetSuitAdditions.MODID + ".key.toggle_jet_suit", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, KEY_CATEGORY));
        jetSuitHoverToggle = KeyBindingHelper.registerKeyBinding(new KeyMapping(JetSuitAdditions.MODID + ".key.toggle_jet_suit_hover", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_PERIOD, KEY_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (jetSuitToggle.consumeClick()) {
                ClientPlayNetworking.send(C2SPackets.TOGGLE_ON, PacketByteBufs.create());
            }
            while (jetSuitHoverToggle.consumeClick()) {
                ClientPlayNetworking.send(C2SPackets.TOGGLE_HOVER, PacketByteBufs.create());
            }
        });

        ParticleFactoryRegistry.getInstance().register(JetSuitAdditions.COLORED_JET_EXHAUST, ColoredJetExhaustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetSuitAdditions.END_ROD_JET_EXHAUST, FlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(JetSuitAdditions.BUBBLES_JET_EXHAUST, FlameParticle.Provider::new);
    }
}
