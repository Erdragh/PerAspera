package com.github.erdragh.jet_suit_additions.mixin;

import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import com.github.alexnijjar.ad_astra.registry.ModArmour;
import com.github.alexnijjar.ad_astra.registry.ModItemGroups;
import com.github.alexnijjar.ad_astra.registry.ModItems;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ModItems.class)
public class ItemRegistryMixin {
    @Redirect(
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = {
                                    "stringValue=jet_suit_helmet"
                            },
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "NEW",
                    target = "Lcom/github/alexnijjar/ad_astra/items/armour/JetSuit;*",
                    ordinal = 0
            ),
            method = "<clinit>"
    )
    private static JetSuit jetSuitHelmet(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
        System.out.println("FUCK");
        return new ImprovedJetSuit(material, slot, settings);
    }

    @Redirect(
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = {
                                    "stringValue=jet_suit"
                            },
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "NEW",
                    target = "Lcom/github/alexnijjar/ad_astra/items/armour/JetSuit;*",
                    ordinal = 0
            ),
            method = "<clinit>"
    )
    private static JetSuit jetSuit(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
        System.out.println("FUCK2");
        return new ImprovedJetSuit(material, slot, settings);
    }

    @Redirect(
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = {
                                    "stringValue=jet_suit_pants"
                            },
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "NEW",
                    target = "Lcom/github/alexnijjar/ad_astra/items/armour/JetSuit;*",
                    ordinal = 0
            ),
            method = "<clinit>"
    )
    private static JetSuit jetSuitPants(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
        System.out.println("FUCK3");
        return new ImprovedJetSuit(material, slot, settings);
    }

    @Redirect(
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = {
                                    "stringValue=jet_suit_boots"
                            },
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "NEW",
                    target = "Lcom/github/alexnijjar/ad_astra/items/armour/JetSuit;*",
                    ordinal = 0
            ),
            method = "<clinit>"
    )
    private static JetSuit jetSuitBoots(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
        System.out.println("FUCK4");
        return new ImprovedJetSuit(material, slot, settings);
    }
}
