package com.github.erdragh.jet_suit_additions.client;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class JetSuitAdditionsClient implements ClientModInitializer {

    public static final String KEY_CATEGORY = JetSuitAdditions.MODID + ".key_category";

    public static KeyBinding jetSuitToggle;

    @Override
    public void onInitializeClient() {
        jetSuitToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(JetSuitAdditions.MODID + ".key.toggle_jet_suit", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, KEY_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (jetSuitToggle.wasPressed()) {
                client.player.sendMessage(Text.of("FUCK"), false);
            }
        });
    }
}
