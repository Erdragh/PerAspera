package com.github.erdragh.jet_suit_additions.items;

import com.github.erdragh.jet_suit_additions.client.screen.JetSuitCustomizationScreen;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class JetSuitFlashCard extends Item {
    public JetSuitFlashCard() {
        super(new FabricItemSettings().group(ItemGroup.TOOLS));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            if (user.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ImprovedJetSuit) {
                MinecraftClient.getInstance().setScreen(new JetSuitCustomizationScreen(user, user.getEquippedStack(EquipmentSlot.CHEST), hand));
                return TypedActionResult.success(user.getStackInHand(hand), true);
            } else {
                return TypedActionResult.pass(user.getStackInHand(hand));
            }
        }
        return super.use(world, user, hand);
    }
}
