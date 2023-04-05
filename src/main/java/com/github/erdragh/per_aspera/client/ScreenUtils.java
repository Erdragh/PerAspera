package com.github.erdragh.per_aspera.client;

import com.github.erdragh.per_aspera.client.screen.JetSuitCustomizationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class ScreenUtils {
    public static void openJetSuitFlashCardScreen(Player user, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new JetSuitCustomizationScreen(user, user.getItemBySlot(EquipmentSlot.CHEST), hand));
    }
}
