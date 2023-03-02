package com.github.erdragh.jet_suit_additions.client;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class JetSuitAdditionsClient implements ClientModInitializer {

    public static final String KEY_CATEGORY = JetSuitAdditions.MODID + ".key_category";

    public static KeyBinding jetSuitToggle, jetSuitHoverToggle;

    @Override
    public void onInitializeClient() {
        jetSuitToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(JetSuitAdditions.MODID + ".key.toggle_jet_suit", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, KEY_CATEGORY));
        jetSuitHoverToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(JetSuitAdditions.MODID + ".key.toggle_jet_suit_hover", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PERIOD, KEY_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (jetSuitToggle.wasPressed()) {
                ClientPlayNetworking.send(C2SPackets.TOGGLE_ON, PacketByteBufs.create());
            }
            while (jetSuitHoverToggle.wasPressed()) {
                ClientPlayNetworking.send(C2SPackets.TOGGLE_HOVER, PacketByteBufs.create());
            }
        });
    }
}
