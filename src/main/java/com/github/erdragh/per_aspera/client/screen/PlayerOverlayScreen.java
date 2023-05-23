package com.github.erdragh.per_aspera.client.screen;

import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.config.PerAsperaConfig;
import com.github.erdragh.per_aspera.items.armour.ThrusterBoots;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class PlayerOverlayScreen {
    public static void render(MatrixStack matrices, float delta) {
        var client = MinecraftClient.getInstance();
        int sWidth = client.getWindow().getScaledWidth();
        int sHeight = client.getWindow().getScaledHeight();
        var player = client.player;

        assert player != null;

        if (player.isSpectator()) return;

        if (ThrusterBoots.playerIsWearingThrusterBoots(player) && PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get()) {
            var charge = ThrusterBoots.getJumpCharge();
            if (charge > 0) {
                int x = 0, y = 0;
                var text = new TranslatableText(PerAspera.MODID + ".hud.thruster_boots_charge", (int)Math.floor(charge * 100));
                var textWidth = client.textRenderer.getWidth(text);
                var textHeight = client.textRenderer.fontHeight;
                int xOffset = PerAsperaConfig.THRUSTER_BOOTS_CHARGE_HUD_X.get(), yOffset = PerAsperaConfig.THRUSTER_BOOTS_CHARGE_HUD_Y.get();
                switch (PerAsperaConfig.THRUSTER_BOOTS_CHARGE_HUD_ALIGNMENT.get()) {
                    case BottomRight -> {
                        y = sHeight - textHeight - yOffset;
                        x = sWidth - textWidth - xOffset;
                    }
                    case TopRight -> {
                        y = yOffset;
                        x = sWidth - textWidth - xOffset;
                    }
                    case BottomCenter -> {
                        y = sHeight - textHeight - yOffset;
                        x = (sWidth - textWidth) / 2 + yOffset;
                    }
                    case TopCenter -> {
                        y = yOffset;
                        x = (sWidth - textWidth) / 2 + yOffset;
                    }
                    case BottomLeft -> {
                        y = sHeight - textHeight - yOffset;
                        x = xOffset;
                    }
                    case TopLeft -> {
                        y = yOffset;
                        x = xOffset;
                    }
                    case RightCenter -> {
                        x = sWidth - textWidth - xOffset;
                        y = (sHeight - textHeight) / 2 - yOffset;
                    }
                    case LeftCenter -> {
                        x = xOffset;
                        y = (sHeight - textHeight) / 2 - yOffset;
                    }
                }
                client.textRenderer.drawWithShadow(matrices, text, x, y, 0xffffff);
            }
        }
    }
}
