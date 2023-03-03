package com.github.erdragh.jet_suit_additions.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ColoredJetExhaustParticle extends SpriteBillboardParticle {


    protected ColoredJetExhaustParticle(ClientWorld clientWorld, double xPos, double yPos, double zPos, SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(clientWorld, xPos, yPos, zPos, 0, 0, 0);

        velocityMultiplier = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.scale *= 0.75F;
        this.maxAge = 20;
        this.setSpriteForAge(spriteSet);

        this.red = (float) xd;
        this.green = (float) yd;
        this.blue = (float) zd;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float) maxAge) * age + 1);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ColoredJetExhaustParticle(world, x, y, z, this.sprites, velocityX, velocityY, velocityZ);
        }
    }
}