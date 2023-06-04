package com.github.erdragh.per_aspera.client.renderers;

import com.github.alexnijjar.ad_astra.client.renderer.spacesuit.NetheriteSpaceSuitModel;
import com.github.alexnijjar.ad_astra.client.renderer.spacesuit.SpaceSuitModel;
import com.github.erdragh.per_aspera.PerAspera;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.util.Identifier;

public class ThrusterBootsRenderer {
    public static Identifier THRUSTER_BOOTS_TEXTURE = new Identifier(PerAspera.MODID, "textures/armor/thruster_boots.png");

    public static void register() {
        ArmorRenderer.register(((matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
            EntityModelLoader modelLoader = MinecraftClient.getInstance().getEntityModelLoader();
            ModelPart layer = modelLoader.getModelPart(NetheriteSpaceSuitModel.LAYER_LOCATION);
            SpaceSuitModel model = new SpaceSuitModel(layer, contextModel, entity, slot, stack, THRUSTER_BOOTS_TEXTURE);
            ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, THRUSTER_BOOTS_TEXTURE);
        }), PerAspera.THRUSTER_BOOTS);
    }
}
