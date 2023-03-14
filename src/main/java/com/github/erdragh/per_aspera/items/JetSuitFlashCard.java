package com.github.erdragh.per_aspera.items;

import com.github.erdragh.per_aspera.client.screen.JetSuitCustomizationScreen;
import com.github.erdragh.per_aspera.items.armour.ImprovedJetSuit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class JetSuitFlashCard extends Item {
    public JetSuitFlashCard() {
        super(new FabricItemSettings().group(CreativeModeTab.TAB_TOOLS));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide) {
            if (user.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ImprovedJetSuit) {
                Minecraft.getInstance().setScreen(new JetSuitCustomizationScreen(user, user.getItemBySlot(EquipmentSlot.CHEST), hand));
                return InteractionResultHolder.success(user.getItemInHand(hand));
            } else {
                return InteractionResultHolder.pass(user.getItemInHand(hand));
            }
        }
        return super.use(world, user, hand);
    }
}
