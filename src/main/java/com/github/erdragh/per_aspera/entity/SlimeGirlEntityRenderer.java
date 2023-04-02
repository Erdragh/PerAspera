package com.github.erdragh.per_aspera.entity;

import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.client.PerAsperaClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class SlimeGirlEntityRenderer extends MobRenderer<SlimeGirlEntity, SlimeGirlEntityModel> {

    public SlimeGirlEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeGirlEntityModel(context.bakeLayer(PerAsperaClient.MODEL_SLIME_GIRL)), 0.5f);
        this.addLayer(new SlimeGirlLayerOuter(this, context.getModelSet()));
    }

    @Override
    public void render(SlimeGirlEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SlimeGirlEntity entity) {
        return new ResourceLocation(PerAspera.MODID, "textures/entity/slime_girl/slime_girl.png");
    }

}
