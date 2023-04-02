package com.github.erdragh.per_aspera.particle;

import com.github.erdragh.per_aspera.PerAspera;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import java.util.Arrays;

public enum JetSuitParticles {

    BUBBLE("bubble", PerAspera.BUBBLES_JET_EXHAUST),
    END_ROD("end_rod", PerAspera.END_ROD_JET_EXHAUST),
    SOUL_FIRE_FLAME("soul_fire_flame", ParticleTypes.SOUL_FIRE_FLAME),
    COLORED_EXHAUST("colored_exhaust", PerAspera.COLORED_JET_EXHAUST);

    public static final JetSuitParticles[] PARTICLES = new JetSuitParticles[]{
            BUBBLE,
            END_ROD,
            SOUL_FIRE_FLAME
    };

    private final ParticleOptions particleEffect;
    private final String identifier;
    JetSuitParticles(String identifier, ParticleOptions particleEffect) {
        this.identifier = identifier;
        this.particleEffect = particleEffect;
    }

    public ParticleOptions get() {
        return this.particleEffect;
    }
    public String getIdentifier() {
        return this.identifier;
    }

    public static JetSuitParticles fromIdentifier(String identifier) {
        var found = Arrays.stream(PARTICLES).filter(p -> p.identifier.equals(identifier)).findFirst();

        return found.orElse(SOUL_FIRE_FLAME);
    }
}