package com.github.erdragh.per_aspera.entity.slimegirl;

import com.github.erdragh.per_aspera.client.PerAsperaClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

@Environment(EnvType.CLIENT)
public class SlimeGirlLayerOuter extends RenderLayer<SlimeGirlEntity, SlimeGirlEntityModel> {
    private final EntityModel<SlimeGirlEntity> model;

    public SlimeGirlLayerOuter(RenderLayerParent<SlimeGirlEntity, SlimeGirlEntityModel> renderer, EntityModelSet loader) {
        super(renderer);
        this.model = new SlimeGirlEntityModel(loader.bakeLayer(PerAsperaClient.MODEL_SLIME_GIRL_OUTER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, SlimeGirlEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        boolean outline;
        var mc = Minecraft.getInstance();
        outline = mc.shouldEntityAppearGlowing(livingEntity) && livingEntity.isInvisible();

        if (livingEntity.isInvisible() && !outline) {
            return;
        }

        var vc = outline ? buffer.getBuffer(RenderType.outline(this.getTextureLocation(livingEntity))) : buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(livingEntity)));
        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
        this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(poseStack, vc, packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0), 1, 1, 1, 1);
    }
}
