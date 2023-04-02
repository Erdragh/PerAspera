package com.github.erdragh.per_aspera.entity.slimegirl;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class SlimeGirlEntityModel extends HumanoidModel<SlimeGirlEntity> {

    private static float chestOffset = 12;

    public SlimeGirlEntityModel(ModelPart root) {
        this(root, RenderType::entityCutoutNoCull);
    }

    public SlimeGirlEntityModel(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
        super (root, renderType);
    }

    public static LayerDefinition createInnerLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition torso_group = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -1.0F + chestOffset, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 51).addBox(-3.0F, -11.0F + chestOffset, -1.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition waist = torso_group.addOrReplaceChild("waist", CubeListBuilder.create().texOffs(56, 16).addBox(-1.0F, -18.0F + chestOffset, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(0.0F).extend(0.5f)), PartPose.offset(0.0f, 0.0f, 0.0f));
        var cubeDeformation = new CubeDeformation(0);

        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0f, -2.0f, -2.0f, 0, 0, 0, cubeDeformation), PartPose.offset(-5.0f, 2.0f + 0, 0.0f));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0f, -2.0f, -2.0f, 0, 0, 0, cubeDeformation), PartPose.offset(5.0f, 2.0f + 0, 0.0f));

        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(-1.9f, 12.0f + 0, 0.0f));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0f, 0.0f, -2.0f, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(1.9f, 12.0f + 0, 0.0f));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 16).addBox(-3.0F, -5.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 30).addBox(0.0F, -5.0F, -3.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, 41).addBox(0.0F, -2.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 56).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(0.0F).extend(0.5f)), PartPose.offset(0.0f, 0.0f, 0.0f));

        PartDefinition torso_group = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(40, 48).addBox(-4.0F, -12.0F + chestOffset, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition chest_r1 = torso_group.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(32, 32).addBox(-4.0F, -1.0F + 10, -3.0F + 6, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition right_leg_bottom_upper_r1 = right_leg.addOrReplaceChild("right_leg_bottom_upper_r1", CubeListBuilder.create().texOffs(40, 27).addBox(0.0F, -6.0F, 5.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition right_leg_bottom_r1 = right_leg.addOrReplaceChild("right_leg_bottom_r1", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, -7.0F, 0.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition left_leg_bottom_r1 = left_leg.addOrReplaceChild("left_leg_bottom_r1", CubeListBuilder.create().texOffs(40, 23).addBox(0.0F, -7.0F, 0.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition left_leg_bottom_upper_r1 = left_leg.addOrReplaceChild("left_leg_bottom_upper_r1", CubeListBuilder.create().texOffs(40, 27).addBox(0.0F, -6.0F, 5.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(32, 39).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 16).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, 5.0F, 1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 4.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 2.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

}
