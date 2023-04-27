package com.github.erdragh.per_aspera.particle;

import com.github.erdragh.per_aspera.PerAspera;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

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
        var found = Arrays.stream(PARTICLES).filter(p -> p.identifier.equals(identifier)).findFirst();

        return found.orElse(SOUL_FIRE_FLAME);
    }
}
