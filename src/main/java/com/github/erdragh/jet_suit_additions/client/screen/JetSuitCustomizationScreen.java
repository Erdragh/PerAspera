package com.github.erdragh.jet_suit_additions.client.screen;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.github.erdragh.jet_suit_additions.particle.JetSuitParticles;
import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class JetSuitCustomizationScreen extends Screen {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier(JetSuitAdditions.MODID, "textures/gui/calculator.png");

    private final PlayerEntity player;
    private final ItemStack chestItem;
    private final Hand hand;

    private static final int WINDOW_WIDTH = 150, WINDOW_HEIGHT = 100;

    private ArrayList<ButtonWidget> buttons = new ArrayList<>();

    public JetSuitCustomizationScreen(PlayerEntity player, ItemStack stack, Hand hand) {
        super(new TranslatableText(JetSuitAdditions.MODID + ".gui.jet_suit_customization"));
        this.player = player;
        this.chestItem = stack;
        this.hand = hand;
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.add(this.addSelectableChild(new ButtonWidget((this.width - WINDOW_WIDTH) / 2, (this.height - WINDOW_HEIGHT) / 2, WINDOW_WIDTH, 20, new TranslatableText(JetSuitAdditions.MODID + ".gui.jet_suit_customization.default"), button -> {
            ClientPlayNetworking.send(C2SPackets.CHANGE_JET_PARTICLE, PacketByteBufs.create().writeEnumConstant(JetSuitParticles.SOUL_FIRE_FLAME));
            this.close();
        })));
        this.buttons.add(this.addSelectableChild(new ButtonWidget((this.width - WINDOW_WIDTH) / 2, (this.height - WINDOW_HEIGHT) / 2 + 30, WINDOW_WIDTH, 20, new TranslatableText(JetSuitAdditions.MODID + ".gui.jet_suit_customization.end_rod"), button -> {
            ClientPlayNetworking.send(C2SPackets.CHANGE_JET_PARTICLE, PacketByteBufs.create().writeEnumConstant(JetSuitParticles.END_ROD));
            this.close();
        })));
        this.buttons.add(this.addSelectableChild(new ButtonWidget((this.width - WINDOW_WIDTH) / 2, (this.height - WINDOW_HEIGHT) / 2 + 60, WINDOW_WIDTH, 20, new TranslatableText(JetSuitAdditions.MODID + ".gui.jet_suit_customization.bubble"), button -> {
            ClientPlayNetworking.send(C2SPackets.CHANGE_JET_PARTICLE, PacketByteBufs.create().writeEnumConstant(JetSuitParticles.BUBBLE));
            this.close();
        })));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.setFocused(buttons.get(0));

        var displayedText = new TranslatableText(JetSuitAdditions.MODID + ".gui.jet_suit_customization.title");
        var textWidth = this.textRenderer.getWidth(displayedText);
        this.textRenderer.draw(matrices, displayedText, (this.width - textWidth) / 2, (this.height - WINDOW_HEIGHT) / 2 - 20, 0xffffff);

        for (var button : this.buttons) {
            button.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
