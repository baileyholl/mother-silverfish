package com.hollingsworth.mother_silverfish.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BabyFish extends Silverfish {

    public int motherID;
    public BabyFish(EntityType<? extends Silverfish> p_33523_, Level p_33524_, int motherID) {
        super(p_33523_, p_33524_);
        this.motherID = motherID;
    }
    public BabyFish(EntityType<? extends Silverfish> p_33523_, Level p_33524_) {
        super(p_33523_, p_33524_);
    }

    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2d).add(Attributes.ATTACK_DAMAGE, 2.0f)
                .add(Attributes.FOLLOW_RANGE, 20f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return super.doHurtTarget(pEntity);
    }

    public MotherSilverfishEntity getMother(){
        return level.getEntity(motherID) instanceof MotherSilverfishEntity mother ? mother : null;
    }
}
