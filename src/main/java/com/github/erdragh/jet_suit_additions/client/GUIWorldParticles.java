package com.github.erdragh.jet_suit_additions.client;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

public class GUIWorldParticles {
    private final Map<ParticleRenderType, Queue<Particle>> byType = Maps.newIdentityHashMap();
    private final Queue<Particle> queue = Queues.newArrayDeque();

    GUIWorld world;

    public GUIWorldParticles(GUIWorld world) {
        this.world = world;
    }

    public void addParticle(Particle p) {
        this.queue.add(p);
    }

    public void tick() {
        this.byType.forEach((key, value) -> this.tickParticleList(value));

        Particle particle;
        if (queue.isEmpty())
            return;
        while ((particle = this.queue.poll()) != null)
            this.byType.computeIfAbsent(particle.getRenderType(), $ -> EvictingQueue.create(16384)).add(particle);
    }

    private void tickParticleList(Collection<Particle> particles) {
        if (particles.isEmpty())
            return;
        Iterator<Particle> particleIterator = particles.iterator();

        while (particleIterator.hasNext()) {
            Particle p = particleIterator.next();
            p.tick();
            if (!p.isAlive()) {
                particleIterator.remove();
            }
        }
    }

    public void renderParticles(PoseStack ms, MultiBufferSource buffer, Camera renderInfo, float pt) {
        Minecraft mc = Minecraft.getInstance();
        LightTexture lightTexture = mc.gameRenderer.lightTexture();

        lightTexture.turnOnLightLayer();
        RenderSystem.enableDepthTest();
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.mulPoseMatrix(ms.last().pose());
        RenderSystem.applyModelViewMatrix();

        for (ParticleRenderType iparticlerendertype : this.byType.keySet()) {
            if (iparticlerendertype == ParticleRenderType.NO_RENDER)
                continue;
            Iterable<Particle> iterable = this.byType.get(iparticlerendertype);
            if (iterable != null) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShader(GameRenderer::getParticleShader);

                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuilder();
                iparticlerendertype.begin(bufferbuilder, mc.getTextureManager());

                for (Particle particle : iterable)
                    particle.render(bufferbuilder, renderInfo, pt);

                iparticlerendertype.end(tessellator);
            }
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
    }

    public void clearEffects() {
        this.byType.clear();
    }
}
