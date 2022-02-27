package com.hollingsworth.mother_silverfish.entity.goals;

import com.hollingsworth.mother_silverfish.ModUtil;
import com.hollingsworth.mother_silverfish.entity.BabyFish;
import com.hollingsworth.mother_silverfish.entity.MotherSilverfishEntity;
import com.hollingsworth.mother_silverfish.event.EarthquakeEvent;
import com.hollingsworth.mother_silverfish.event.EventQueue;
import com.hollingsworth.mother_silverfish.network.Networking;
import com.hollingsworth.mother_silverfish.network.PacketAnimEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Supplier;

public class EarthquakeGoal extends Goal {
    MotherSilverfishEntity boss;
    Supplier<Boolean> canUse;
    int ticks;
    boolean isDone;
    boolean slammed;
    boolean startedQuake;
    BlockPos targetPos; // Set on goal start so the player can dodge

    public EarthquakeGoal(MotherSilverfishEntity motherSilverfishEntity, Supplier<Boolean> canUse) {
        this.boss = motherSilverfishEntity;
        this.canUse = canUse;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }


    @Override
    public void tick() {
        if(!slammed){
            Networking.sendToNearby(boss.level, boss, new PacketAnimEntity(boss.getId(), MotherSilverfishEntity.Animations.EARTHQUAKE.ordinal()));
            slammed = true;
        }

        ticks++;
        if(ticks >= 20 && !startedQuake){
            if(boss.getTarget() != null) {
                EventQueue.getServerInstance().addEvent(new EarthquakeEvent(boss.level, () -> boss.getOnPos(), () -> {
                    if (boss.getTarget() != null) {
                        double xSlope = targetPos.getX() - boss.getX();
                        double zSlope = targetPos.getZ() - boss.getZ();

                        return targetPos.offset(xSlope * 2, 0, zSlope * 2);
                    }
                    return null;
                }));
            }
            for(Entity e : boss.level.getEntities(null, boss.getBoundingBox().inflate(5,5,5))) {
                if(e instanceof LivingEntity && !(e instanceof BabyFish) && !(e instanceof MotherSilverfishEntity)){
                    e.hurt(DamageSource.mobAttack(boss), 12f);
                }
            }
            startedQuake = true;
        }
        if(boss.getTarget() != null)
            boss.getLookControl().setLookAt(boss.getTarget());
        boss.setDeltaMovement(0,0,0);
        if(ticks > 60){
            finishEarthquake();
        }
    }

    public void finishEarthquake(){
        isDone = true;
        this.boss.earthquakeCooldown = boss.getEarthquakeCooldown();
    }


    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        ticks = 0;
        isDone = false;
        slammed = false;
        startedQuake = false;
        if(boss.getTarget() != null)
            targetPos = boss.getTarget().getOnPos();
    }

    @Override
    public boolean canContinueToUse() {
        return !isDone && boss.getTarget() != null;
    }

    @Override
    public boolean canUse() {
        return boss.earthquakeCooldown <= 0 && canUse.get() && boss.getTarget() != null && ModUtil.distanceFrom(boss.getTarget(), boss) < 10;
    }
}
