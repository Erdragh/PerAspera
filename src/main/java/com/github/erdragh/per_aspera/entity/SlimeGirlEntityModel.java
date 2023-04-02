package com.github.erdragh.per_aspera.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SlimeGirlEntityModel extends HierarchicalModel<SlimeGirlEntity> {
    private final ModelPart root;

    private final ModelPart head;

    public SlimeGirlEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
    }

    public static LayerDefinition createInnerLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition torso_group = partdefinition.addOrReplaceChild("torso_group", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 51).addBox(-3.0F, -11.0F, -1.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition waist = torso_group.addOrReplaceChild("waist", CubeListBuilder.create().texOffs(56, 9).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 16).addBox(-3.0F, -5.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 30).addBox(0.0F, -5.0F, -3.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, 41).addBox(0.0F, -2.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 56).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition torso_group = partdefinition.addOrReplaceChild("torso_group", CubeListBuilder.create().texOffs(40, 48).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition chest_r1 = torso_group.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(32, 32).addBox(-4.0F, -1.0F, -3.0F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition right_leg_bottom_upper_r1 = right_leg.addOrReplaceChild("right_leg_bottom_upper_r1", CubeListBuilder.create().texOffs(40, 27).addBox(0.0F, -6.0F, 5.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition right_leg_bottom_r1 = right_leg.addOrReplaceChild("right_leg_bottom_r1", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, -7.0F, 0.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition left_leg_bottom_r1 = left_leg.addOrReplaceChild("left_leg_bottom_r1", CubeListBuilder.create().texOffs(40, 23).addBox(0.0F, -7.0F, 0.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition left_leg_bottom_upper_r1 = left_leg.addOrReplaceChild("left_leg_bottom_upper_r1", CubeListBuilder.create().texOffs(44, 0).addBox(0.0F, -6.0F, 5.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, 0.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(32, 39).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 16).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, 5.0F, 1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 4.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 32).addBox(4.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(-8.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SlimeGirlEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
