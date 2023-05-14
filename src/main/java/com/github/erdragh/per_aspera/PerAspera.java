package com.github.erdragh.per_aspera;

import com.github.erdragh.per_aspera.config.PerAsperaConfig;
import com.github.erdragh.per_aspera.items.JetSuitFlashCard;
import com.github.erdragh.per_aspera.items.armour.ThrusterBoots;
import com.github.erdragh.per_aspera.items.armour.ThrusterBootsMaterial;
import com.github.erdragh.per_aspera.networking.C2SPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerAspera implements ModInitializer {

    public static final String MODID = "per_aspera";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final SimpleParticleType COLORED_JET_EXHAUST = FabricParticleTypes.simple();
    public static final SimpleParticleType END_ROD_JET_EXHAUST = FabricParticleTypes.simple();
    public static final SimpleParticleType BUBBLES_JET_EXHAUST = FabricParticleTypes.simple();

    public static final Item JET_SUIT_FLASH_CARD = new JetSuitFlashCard();

    public static final ArmorMaterial THRUSTER_BOOTS_MATERIAL = new ThrusterBootsMaterial();
    public static final ArmorItem THRUSTER_BOOTS = new ThrusterBoots();


    @Override
    public void onInitialize() {
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, PerAsperaConfig.SPEC);

        C2SPackets.register();
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "colored_jet_exhaust"), COLORED_JET_EXHAUST);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "end_rod_jet_exhaust"), END_ROD_JET_EXHAUST);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "bubbles_jet_exhaust"), BUBBLES_JET_EXHAUST);

        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "jet_suit_flash_card"), JET_SUIT_FLASH_CARD);
        // only load thruster boots if they're enabled
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get()) {
            Registry.register(Registry.ITEM, new ResourceLocation(MODID, "thruster_boots"), THRUSTER_BOOTS);
        }
        LOGGER.info("Per Aspera initialized.");
    }


}
