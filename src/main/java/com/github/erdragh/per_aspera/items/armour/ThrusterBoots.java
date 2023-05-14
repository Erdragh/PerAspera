package com.github.erdragh.per_aspera.items.armour;

import com.github.erdragh.per_aspera.PerAspera;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;

public class ThrusterBoots extends ArmorItem {
    public ThrusterBoots() {
        super(PerAspera.THRUSTER_BOOTS_MATERIAL, EquipmentSlot.FEET, new FabricItemSettings().group(CreativeModeTab.TAB_TRANSPORTATION));
    }
}
