package com.github.erdragh.jet_suit_additions;

import com.github.alexnijjar.ad_astra.registry.ModArmour;
import com.github.alexnijjar.ad_astra.registry.ModItemGroups;
import com.github.erdragh.jet_suit_additions.items.armour.ImprovedJetSuit;
import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class JetSuitAdditions implements ModInitializer {

    public static final String MODID = "jet_suit_additions";
    public static ImprovedJetSuit JET_SUIT_HELMET, JET_SUIT, JET_SUIT_PANTS, JET_SUIT_BOOTS;

    @Override
    public void onInitialize() {
        C2SPackets.register();

        // JET_SUIT_HELMET = Registry.register(Registry.ITEM, new Identifier(MODID, "jet_suit_helmet"), new ImprovedJetSuit(ModArmour.JET_SUIT_ARMOUR_MATERIAL, EquipmentSlot.HEAD, new FabricItemSettings().group(ModItemGroups.ITEM_GROUP_NORMAL).fireproof()));
    }
}
