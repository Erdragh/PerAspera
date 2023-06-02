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
                .defineInRange("thrusterBootsJumpStrength", 1.0, 0.6, 5);

        THRUSTER_BOOTS_TIMEOUT_TICKS = builder.comment("Time in ticks for cooldown of using thruster boots")
                .defineInRange("thrusterBootsTimeoutTicks", 120, 0, Integer.MAX_VALUE);

        THRUSTER_BOOTS_CHARGE_HUD_ALIGNMENT = builder.comment("Where the Thruster Boots Charge display is aligned")
                .defineEnum("thrusterBootsChargeHUDAlignment", HUDAlignment.TopRight);

        THRUSTER_BOOTS_CHARGE_HUD_X = builder.comment("X-Offset of the Thruster Boots Charge HUD")
                .defineInRange("thrusterBootsChargeHUDX", 10, -1000, 1000);
        THRUSTER_BOOTS_CHARGE_HUD_Y = builder.comment("Y-Offset of the Thruster Boots Charge HUD")
                .defineInRange("thrusterBootsChargeHUDY", 10, -1000, 1000);

        SPEC = builder.build();
    }

    public static final ForgeConfigSpec.BooleanValue THRUSTER_BOOTS_ENABLED;
    public static final ForgeConfigSpec.DoubleValue THRUSTER_BOOTS_JUMP_STRENGTH;

    public static final ForgeConfigSpec.IntValue THRUSTER_BOOTS_TIMEOUT_TICKS;

    public static final ForgeConfigSpec.EnumValue<HUDAlignment> THRUSTER_BOOTS_CHARGE_HUD_ALIGNMENT;

    public static final ForgeConfigSpec.IntValue THRUSTER_BOOTS_CHARGE_HUD_X, THRUSTER_BOOTS_CHARGE_HUD_Y;

    public enum HUDAlignment {
        TopLeft,
        TopCenter,
        TopRight,
        LeftCenter,
        RightCenter,
        BottomLeft,
        BottomCenter,
        BottomRight
    }
}
