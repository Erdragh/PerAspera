package com.github.erdragh.jet_suit_additions.client.screen;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.github.erdragh.jet_suit_additions.particle.JetSuitParticles;
import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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

        RenderSystem.enableBlend();

        var displayedText = new TranslatableComponent(JetSuitAdditions.MODID + ".gui.jet_suit_customization.title");
        var textWidth = this.font.width(displayedText);
        this.font.draw(matrices, displayedText, (this.width - textWidth) / 2, (this.height - WINDOW_HEIGHT) / 2 - 20, 0xffffff);

        for (var button : this.buttons) {
            button.render(matrices, mouseX, mouseY, delta);
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
