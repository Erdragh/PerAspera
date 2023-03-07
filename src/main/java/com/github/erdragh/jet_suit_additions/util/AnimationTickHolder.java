package com.github.erdragh.jet_suit_additions.util;


import com.github.erdragh.jet_suit_additions.client.GUIClientWorld;
import com.github.erdragh.jet_suit_additions.client.GUIWorld;
import io.github.fabricators_of_create.porting_lib.util.MinecraftClientUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LevelAccessor;

public class AnimationTickHolder {

    private static int ticks;
    private static int pausedTicks;

    public static void reset() {
        ticks = 0;
        pausedTicks = 0;
    }

    public static void tick() {
        if (!Minecraft.getInstance()
                .isPaused()) {
            ticks = (ticks + 1) % 1_728_000; // wrap around every 24 hours so we maintain enough floating point precision
        } else {
            pausedTicks = (pausedTicks + 1) % 1_728_000;
        }
    }

    public static int getTicks() {
        return getTicks(false);
    }

    public static int getTicks(boolean includePaused) {
        return includePaused ? ticks + pausedTicks : ticks;
    }

    public static float getPartialTicks() {
        Minecraft mc = Minecraft.getInstance();
        return (mc.isPaused() ? MinecraftClientUtil.getRenderPartialTicksPaused(mc) : mc.getFrameTime());
    }

}