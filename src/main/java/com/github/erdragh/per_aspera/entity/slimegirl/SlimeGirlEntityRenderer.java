package com.github.erdragh.per_aspera.entity.slimegirl;

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
        this.shadowRadius = 0.25f * (float)entity.getSize();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    protected void scale(SlimeGirlEntity livingEntity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.999f, 0.999f, 0.999f);
        matrixStack.translate(0.0, 0.001f, 0.0);
        float size = livingEntity.getSize() * SlimeGirlEntity.BASE_SIZE;
        matrixStack.scale(1 * size, 1.0f * size, 1 * size);
    }

    @Override
    public ResourceLocation getTextureLocation(SlimeGirlEntity entity) {
        return new ResourceLocation(PerAspera.MODID, "textures/entity/slime_girl/slime_girl.png");
    }

}
