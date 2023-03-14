package com.github.erdragh.per_aspera.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ColoredJetExhaustParticle extends RisingParticle {


    protected ColoredJetExhaustParticle(ClientLevel clientWorld, double xPos, double yPos, double zPos, FabricSpriteProvider spriteSet, double xd, double yd, double zd) {
        super(clientWorld, xPos, yPos, zPos, 0, 0, 0);

        friction = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.quadSize *= 0.75F;
        this.lifetime = 20;
        this.setSpriteFromAge(spriteSet);

        this.rCol = (float) xd;
        this.gCol = (float) yd;
        this.bCol = (float) zd;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    private void fadeOut() {
        this.alpha = (-(1/(float) lifetime) * age + 1);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final FabricSpriteProvider sprites;

        public Factory(FabricSpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ColoredJetExhaustParticle(world, x, y, z, this.sprites, velocityX, velocityY, velocityZ);
        }
    }
}