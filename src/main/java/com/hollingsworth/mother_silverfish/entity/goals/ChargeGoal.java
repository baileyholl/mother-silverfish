package com.hollingsworth.mother_silverfish.entity.goals;

import com.hollingsworth.mother_silverfish.ModUtil;
import com.hollingsworth.mother_silverfish.entity.MotherSilverfishEntity;
import com.hollingsworth.mother_silverfish.network.Networking;
import com.hollingsworth.mother_silverfish.network.PacketAnimEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class ChargeGoal extends Goal {
    MotherSilverfishEntity boss;
    int timeCharging;

    boolean finished;
    boolean startedCharge;
    boolean isCharging;
    boolean hasHit;

    public ChargeGoal(MotherSilverfishEntity silverfishEntity){
        this.boss = silverfishEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        timeCharging = 0;
        finished = false;
        startedCharge = false;
        isCharging = false;
        hasHit = false;
        boss.isCharging = true;
    }

    @Override
    public void tick() {
        super.tick();
        System.out.println("charging");

        if(timeCharging >= 105) {
            endRam();
        }
        if(this.boss.getTarget() == null) {
            endRam();
        }
        if(!startedCharge){
            Networking.sendToNearby(boss.level, boss, new PacketAnimEntity(boss.getId(), MotherSilverfishEntity.Animations.CHARGE.ordinal()));
            startedCharge = true;
        }
        timeCharging++;


        if(timeCharging <= 25 && !isCharging){
            LivingEntity livingentity = this.boss.getTarget();
            if(livingentity != null)
                this.boss.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            this.boss.getNavigation().stop();
        }

        if(timeCharging > 25 ){
            isCharging = true;
        }
        if(isCharging) {
            if(boss.getNavigation() == null || boss.getTarget() == null) {
                attack();
                return;
            }
            breakBlocks();
            Path path = boss.getNavigation().createPath(this.boss.getTarget().blockPosition().above(),  1);
            if(path == null) {
                return;
            }
            breakBlocks();
            boss.getNavigation().moveTo(path, 2.0f);
            attack();
        }

        if(boss != null && boss.getTarget() != null && hasHit && ModUtil.distanceFrom(boss.position(), boss.getTarget().position()) <= 2.0f){
            endRam();
        }
    }

    public void breakBlocks(){
        if(!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.boss.level, this.boss)){
            return;
        }
        Direction facing = boss.getDirection();
        BlockPos facingPos = boss.blockPosition().above().relative(facing);
        for(int i = 0; i < 2; i++){
            facingPos = facingPos.above(i);
            destroyBlock(facingPos.above());
            destroyBlock(facingPos.east());
            destroyBlock(facingPos.west());
            destroyBlock(facingPos.south());
            destroyBlock(facingPos.north());
        }
    }

    public void destroyBlock(BlockPos pos){
        if(ModUtil.isCorrectHarvestLevel(4, boss.level.getBlockState(pos))) {
            boss.level.destroyBlock(pos, true);
        }
    }

    @Override
    public void stop() {
        super.stop();
        boss.isCharging = false;
    }

    public void endRam(){
        finished = true;
        if(boss.level.random.nextInt(3) != 0) {
            boss.chargeCooldown = boss.getChargeCooldown();
        }
        boss.isCharging = false;
//        Networking.sendToNearby(boss.level, boss, new PacketAnimEntity(boss.getId(), EntityChimera.Animations.ATTACK.ordinal()));
        attack();
    }

    protected void attack() {
        List<LivingEntity> nearbyEntities = boss.level.getEntitiesOfClass(LivingEntity.class, new AABB(boss.blockPosition()).inflate(1, 1, 1));
        for(LivingEntity enemy: nearbyEntities){
            if(enemy.equals(boss))
                continue;
            this.boss.doHurtTarget(enemy);
            enemy.knockback(3.0f, Mth.sin(boss.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(boss.getYRot() * ((float)Math.PI / 180F)));
            hasHit = true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !finished;
    }

    @Override
    public boolean canUse() {
        return boss.getTarget() != null && boss.chargeCooldown <= 0 && boss.canCharge();
    }
}
