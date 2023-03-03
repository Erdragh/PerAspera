package com.github.erdragh.jet_suit_additions.items.armour;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

public enum JetSuitParticle {

    BUBBLE("bubble", ParticleTypes.BUBBLE),
    END_ROD("en_rod", ParticleTypes.END_ROD),
    SOUL_FIRE_FLAME("soul_fire_flame", ParticleTypes.SOUL_FIRE_FLAME),
    PORTAL("portal", ParticleTypes.PORTAL);


    private final ParticleEffect particleEffect;
    private final String identifier;
    JetSuitParticle(String identifier, ParticleEffect particleEffect) {
        this.identifier = identifier;
        this.particleEffect = particleEffect;
    }

    public ParticleEffect get() {
        return this.particleEffect;
    }
    public String getIdentifier() {
        return this.identifier;
    }

    public static JetSuitParticle fromIdentifier(String identifier) {
        if (identifier.equals(BUBBLE.identifier)) return BUBBLE;
        if (identifier.equals(END_ROD.identifier)) return END_ROD;
        if (identifier.equals(PORTAL.identifier)) return PORTAL;
        return SOUL_FIRE_FLAME;
    }
}
