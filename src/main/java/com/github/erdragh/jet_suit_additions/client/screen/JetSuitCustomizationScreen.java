package com.github.erdragh.jet_suit_additions.client.screen;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.github.erdragh.jet_suit_additions.client.JetSuitAdditionsClient;
import com.github.erdragh.jet_suit_additions.particle.JetSuitParticles;
import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import com.google.common.collect.*;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class JetSuitCustomizationScreen extends Screen {

    private final Player player;
    private final ItemStack chestItem;
    private final InteractionHand hand;

    private static final int WINDOW_WIDTH = 150, WINDOW_HEIGHT = 100;

    private ArrayList<Button> buttons = new ArrayList<>();


    public JetSuitCustomizationScreen(Player player, ItemStack stack, InteractionHand hand) {
        super(new TranslatableComponent(JetSuitAdditions.MODID + ".gui.jet_suit_customization"));
        this.chestItem = stack;
        this.hand = hand;

        this.player = player;

    }

    @Override
    protected void init() {
        super.init();
        buttons = new ArrayList<>();
        this.buttons.add(this.addWidget(new Button((this.width - WINDOW_WIDTH) / 2, (this.height - WINDOW_HEIGHT) / 2, WINDOW_WIDTH, 20, new TranslatableComponent(JetSuitAdditions.MODID + ".gui.jet_suit_customization.default"), button -> {
            ClientPlayNetworking.send(C2SPackets.CHANGE_JET_PARTICLE, PacketByteBufs.create().writeEnum(JetSuitParticles.SOUL_FIRE_FLAME));
            this.onClose();
        })));
        this.buttons.add(this.addWidget(new Button((this.width - WINDOW_WIDTH) / 2, (this.height - WINDOW_HEIGHT) / 2 + 30, WINDOW_WIDTH, 20, new TranslatableComponent(JetSuitAdditions.MODID + ".gui.jet_suit_customization.end_rod"), button -> {
            ClientPlayNetworking.send(C2SPackets.CHANGE_JET_PARTICLE, PacketByteBufs.create().writeEnum(JetSuitParticles.END_ROD));
            this.onClose();
        })));
        this.buttons.add(this.addWidget(new Button((this.width - WINDOW_WIDTH) / 2, (this.height - WINDOW_HEIGHT) / 2 + 60, WINDOW_WIDTH, 20, new TranslatableComponent(JetSuitAdditions.MODID + ".gui.jet_suit_customization.bubble"), button -> {
            ClientPlayNetworking.send(C2SPackets.CHANGE_JET_PARTICLE, PacketByteBufs.create().writeEnum(JetSuitParticles.BUBBLE));
            this.onClose();
        })));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.setFocused(buttons.get(0));

        var displayedText = new TranslatableComponent(JetSuitAdditions.MODID + ".gui.jet_suit_customization.title");
        var textWidth = this.font.width(displayedText);
        this.font.draw(matrices, displayedText, (this.width - textWidth) / 2, (this.height - WINDOW_HEIGHT) / 2 - 20, 0xffffff);

        for (var button : this.buttons) {
            button.render(matrices, mouseX, mouseY, delta);
        }

        renderEntityInInventory(this.width - 100, (this.height - 20) / 2, 20, -(mouseX - this.width + 100), -(mouseY - (this.height - 20) / 2), Minecraft.getInstance().player);
    }

    public static void renderEntityInInventory(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float g = (float)Math.atan((double)(mouseY / 40.0F));
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate((double)posX, (double)posY, 1050.0);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        poseStack2.translate(0.0, 0.0, 1000.0);
        poseStack2.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion2 = Vector3f.XP.rotationDegrees(g * 20.0F);
        quaternion.mul(quaternion2);
        poseStack2.mulPose(quaternion);
        float h = livingEntity.yBodyRot;
        float i = livingEntity.getYRot();
        float j = livingEntity.getXRot();
        float k = livingEntity.yHeadRotO;
        float l = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + f * 20.0F;
        livingEntity.setYRot(180.0F + f * 40.0F);
        livingEntity.setXRot(-g * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion2.conj();
        entityRenderDispatcher.overrideCameraOrientation(quaternion2);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack2, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = h;
        livingEntity.setYRot(i);
        livingEntity.setXRot(j);
        livingEntity.yHeadRotO = k;
        livingEntity.yHeadRot = l;
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
