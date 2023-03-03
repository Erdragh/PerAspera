package com.github.erdragh.jet_suit_additions;

import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class JetSuitAdditions implements ModInitializer {

    public static final String MODID = "jet_suit_additions";

    public static final DefaultParticleType COLORED_JET_EXHAUST = FabricParticleTypes.simple();


    @Override
    public void onInitialize() {
        C2SPackets.register();
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(MODID, "colored_jet_exhaust"), COLORED_JET_EXHAUST);
    }


}
