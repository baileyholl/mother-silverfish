package com.hollingsworth.mother_silverfish.entity;

import com.hollingsworth.mother_silverfish.Config;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BabyPoisonFish extends BabyFish {

    public BabyPoisonFish(EntityType<BabyPoisonFish> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(!level.isClientSide && pEntity instanceof LivingEntity livingEntity){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, Config.POISON_DURATION.get(), Config.POISON_LEVEL.get()));
        }
        return super.doHurtTarget(pEntity);
    }
}
