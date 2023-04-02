package com.github.erdragh.per_aspera.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class SlimeGirlEntity extends PathfinderMob {

    public SlimeGirlEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super (entityType, level);
    }

}
