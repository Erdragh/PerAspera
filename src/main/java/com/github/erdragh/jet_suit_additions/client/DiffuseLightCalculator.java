package com.github.erdragh.jet_suit_additions.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public interface DiffuseLightCalculator {
    DiffuseLightCalculator NETHER = RenderMath::diffuseLightNether;

    static DiffuseLightCalculator forCurrentLevel() {
        return forLevel(Minecraft.getInstance().level);
    }

    static DiffuseLightCalculator forLevel(ClientLevel level) {
        return NETHER;
    }

    float getDiffuse(float normalX, float normalY, float normalZ, boolean shaded);
}