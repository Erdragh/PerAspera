package com.github.erdragh.jet_suit_additions.items.armour;

import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class ImprovedJetSuit extends JetSuit {
    public ImprovedJetSuit(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void fly(PlayerEntity player, ItemStack stack) {
        System.out.println("Fuck Yeah");
        super.fly(player, stack);
    }
}
