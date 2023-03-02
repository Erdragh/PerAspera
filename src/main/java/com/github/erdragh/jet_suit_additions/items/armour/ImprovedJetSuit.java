package com.github.erdragh.jet_suit_additions.items.armour;

import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import com.github.alexnijjar.ad_astra.registry.ModItems;
import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ImprovedJetSuit extends JetSuit {
    public ImprovedJetSuit(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void fly(PlayerEntity player, ItemStack stack) {
        if (!stack.getOrCreateNbt().getBoolean("toggle_on")) return;
        super.fly(player, stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.isOf(ModItems.JET_SUIT)) {
            boolean turnedOn = stack.getOrCreateNbt().getBoolean("toggle_on");
            Text text = new TranslatableText(JetSuitAdditions.MODID + ".msg.jet_suit_toggle").append(new TranslatableText(JetSuitAdditions.MODID + ".msg.jet_suit_" + (turnedOn ? "on" : "off")).setStyle(Style.EMPTY.withBold(true).withColor(turnedOn ? Formatting.GREEN : Formatting.RED)));

            tooltip.add(text);
        }
    }
}
