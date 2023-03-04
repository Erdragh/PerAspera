package com.github.erdragh.jet_suit_additions.particle;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

public enum JetSuitParticles {

    BUBBLE("bubble", JetSuitAdditions.BUBBLES_JET_EXHAUST),
    END_ROD("end_rod", JetSuitAdditions.END_ROD_JET_EXHAUST),
    SOUL_FIRE_FLAME("soul_fire_flame", ParticleTypes.SOUL_FIRE_FLAME),
    COLORED_EXHAUST("colored_exhaust", JetSuitAdditions.COLORED_JET_EXHAUST);


    private final ParticleEffect particleEffect;
    private final String identifier;
    JetSuitParticles(String identifier, ParticleEffect particleEffect) {
        this.identifier = identifier;
        this.particleEffect = particleEffect;
    }

    public ParticleEffect get() {
        return this.particleEffect;
    }
    public String getIdentifier() {
        return this.identifier;
    }

    public static JetSuitParticles fromIdentifier(String identifier) {
        if (identifier.equals(BUBBLE.identifier)) return BUBBLE;
        if (identifier.equals(END_ROD.identifier)) return END_ROD;
        // if (identifier.equals(COLORED_EXHAUST.identifier)) return COLORED_EXHAUST;
        return SOUL_FIRE_FLAME;
    }
}
