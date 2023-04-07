package com.github.erdragh.per_aspera;

import com.github.alexnijjar.ad_astra.registry.ModBlocks;
import com.github.erdragh.per_aspera.blocks.LegacyBluePillar;
import com.github.erdragh.per_aspera.items.JetSuitFlashCard;
import com.github.erdragh.per_aspera.networking.C2SPackets;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import org.slf4j.Logger;

public class PerAspera implements ModInitializer {

    public static final String MODID = "per_aspera";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final SimpleParticleType COLORED_JET_EXHAUST = FabricParticleTypes.simple();
    public static final SimpleParticleType END_ROD_JET_EXHAUST = FabricParticleTypes.simple();
    public static final SimpleParticleType BUBBLES_JET_EXHAUST = FabricParticleTypes.simple();

    public static final Item JET_SUIT_FLASH_CARD = new JetSuitFlashCard();

    public static final Block LEGACY_BLUE_PILLAR = new LegacyBluePillar(FabricBlockSettings.copy(Blocks.CUT_COPPER).destroyTime(.15f).lightLevel((state) -> {
        return 15;
    }));


    @Override
    public void onInitialize() {
        C2SPackets.register();
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "colored_jet_exhaust"), COLORED_JET_EXHAUST);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "end_rod_jet_exhaust"), END_ROD_JET_EXHAUST);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "bubbles_jet_exhaust"), BUBBLES_JET_EXHAUST);

        Registry.register(Registry.BLOCK, new ResourceLocation("ad_astra", "blue_iron_pillar"), LEGACY_BLUE_PILLAR);

        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "jet_suit_flash_card"), JET_SUIT_FLASH_CARD);
        LOGGER.info("Per Aspera initialized.");
    }


}
