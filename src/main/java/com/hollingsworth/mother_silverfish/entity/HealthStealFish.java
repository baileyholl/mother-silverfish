package com.hollingsworth.mother_silverfish.entity;

import com.hollingsworth.mother_silverfish.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HealthStealFish extends BabyFish{

    public HealthStealFish(EntityType<HealthStealFish> entityEntityType, Level level) {
        super(entityEntityType, level);
    }


    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(getMother() != null){
            getMother().heal(Config.LEECH_HEAL_AMOUNT.get().floatValue());
        }
        return super.doHurtTarget(pEntity);
    }
}
