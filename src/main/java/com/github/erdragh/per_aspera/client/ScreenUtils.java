package com.github.erdragh.per_aspera.client;

import com.github.erdragh.per_aspera.client.screen.JetSuitCustomizationScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class ScreenUtils {
    public static void openJetSuitFlashCardScreen(PlayerEntity user, Hand hand) {
        MinecraftClient.getInstance().setScreen(new JetSuitCustomizationScreen(user, user.getEquippedStack(EquipmentSlot.CHEST), hand));
    }
}
