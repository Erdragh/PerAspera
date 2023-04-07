package com.github.erdragh.per_aspera.blocks;

import com.github.alexnijjar.ad_astra.registry.ModBlocks;
import net.minecraft.world.level.block.RotatedPillarBlock;

public class LegacyBluePillar extends RotatedPillarBlock {
    public LegacyBluePillar(Properties properties) {
        super(properties);
        this.drops = ModBlocks.GLOWING_IRON_PILLAR.getLootTable();
    }
}
