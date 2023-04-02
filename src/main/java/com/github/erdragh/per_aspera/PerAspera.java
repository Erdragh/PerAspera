package com.github.erdragh.per_aspera;

import com.github.erdragh.per_aspera.entity.slimegirl.SlimeGirlEntity;
import com.github.erdragh.per_aspera.items.JetSuitFlashCard;
import com.github.erdragh.per_aspera.networking.C2SPackets;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

public class PerAspera implements ModInitializer {

    public static final String MODID = "per_aspera";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final SimpleParticleType COLORED_JET_EXHAUST = FabricParticleTypes.simple();
    public static final SimpleParticleType END_ROD_JET_EXHAUST = FabricParticleTypes.simple();
    public static final SimpleParticleType BUBBLES_JET_EXHAUST = FabricParticleTypes.simple();

    public static final Item JET_SUIT_FLASH_CARD = new JetSuitFlashCard();

    public static final EntityType<SlimeGirlEntity> SLIME_GIRL = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MODID, "slime_girl"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, SlimeGirlEntity::new).dimensions(EntityDimensions.scalable(.5f, 2f)).build());


    @Override
    public void onInitialize() {
        C2SPackets.register();
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "colored_jet_exhaust"), COLORED_JET_EXHAUST);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "end_rod_jet_exhaust"), END_ROD_JET_EXHAUST);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, "bubbles_jet_exhaust"), BUBBLES_JET_EXHAUST);

        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "jet_suit_flash_card"), JET_SUIT_FLASH_CARD);

        FabricDefaultAttributeRegistry.register(SLIME_GIRL, SlimeGirlEntity.createMobAttributes());
    }


}
