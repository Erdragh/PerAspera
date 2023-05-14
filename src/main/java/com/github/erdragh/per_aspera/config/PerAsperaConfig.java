package com.github.erdragh.per_aspera.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PerAsperaConfig {
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Config of Per Aspera");

        THRUSTER_BOOTS_ENABLED = builder.comment("Whether the thruster boots should be enabled")
                .define("thrusterBootsEnabled", true);

        THRUSTER_BOOTS_JUMP_HEIGHT = builder.comment("How high thruster boots can boost you")
                .defineInRange("thrusterBootsJumpHeight", 3, 2, 10);

        SPEC = builder.build();
    }

    public static final ForgeConfigSpec.BooleanValue THRUSTER_BOOTS_ENABLED;
    public static final ForgeConfigSpec.IntValue THRUSTER_BOOTS_JUMP_HEIGHT;
}
