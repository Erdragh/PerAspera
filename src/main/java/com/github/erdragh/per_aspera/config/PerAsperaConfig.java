package com.github.erdragh.per_aspera.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PerAsperaConfig {
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Config of Per Aspera");

        THRUSTER_BOOTS_ENABLED = builder.comment("Whether the thruster boots should be enabled")
                .define("thrusterBootsEnabled", true);

        THRUSTER_BOOTS_JUMP_STRENGTH = builder.comment("How high thruster boots can boost you")
                .defineInRange("thrusterBootsJumpStrength", 0.6, 0.05, 5);

        THRUSTER_BOOTS_TIMEOUT_TICKS = builder.comment("Time in ticks for cooldown of using thruster boots")
                .defineInRange("thrusterBootsTimeoutTicks", 120, 0, Integer.MAX_VALUE);

        SPEC = builder.build();
    }

    public static final ForgeConfigSpec.BooleanValue THRUSTER_BOOTS_ENABLED;
    public static final ForgeConfigSpec.DoubleValue THRUSTER_BOOTS_JUMP_STRENGTH;

    public static final ForgeConfigSpec.IntValue THRUSTER_BOOTS_TIMEOUT_TICKS;
}
