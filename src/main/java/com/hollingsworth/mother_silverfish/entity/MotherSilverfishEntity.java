package com.hollingsworth.mother_silverfish.entity;

import com.hollingsworth.mother_silverfish.Config;
import com.hollingsworth.mother_silverfish.entity.goals.ChargeGoal;
import com.hollingsworth.mother_silverfish.setup.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
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
import software.bernie.geckolib3.core.IAnimatable;

public class MotherSilverfishEntity extends Silverfish implements IAnimationListener, IAnimatable {
    public int earthquakeCooldown;
    public int spawnCooldown;
    public int chargeCooldown;
    public boolean isCharging;


    public MotherSilverfishEntity(EntityType<? extends Silverfish> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public void tick() {
        super.tick();
        if(spawnCooldown > 0){
            spawnCooldown--;
        }
        if(earthquakeCooldown > 0){
            earthquakeCooldown--;
        }
        if(chargeCooldown > 0)
            chargeCooldown--;

        if(!level.isClientSide && spawnCooldown <= 0){
            Silverfish silverfish = getSpawn();
            level.addFreshEntity(silverfish);
            spawnCooldown = getSpawnCooldown();
        }
    }

    public BabyFish getSpawn(){
        double rand = random.nextDouble();
        BabyFish babyFish;
        if(rand < 0.33){
            babyFish = new BabyPoisonFish(EntityRegistry.POISON_FISH, level);
        }else if(rand < 0.66){
            babyFish = new HealthStealFish(EntityRegistry.HEALTH_STEAL_FISH, level);
        }else{
            babyFish = new ItemStealFish(EntityRegistry.ITEM_STEAL_FISH, level);
        }
        babyFish.setPos(this.position().x, this.position().y,this.position().z);
        babyFish.motherID = this.getId();
        return babyFish;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ChargeGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public int getEarthquakeCooldown(){
        return 500;
    }

    public int getSpawnCooldown(){
        return Config.EASY_SUMMON_BABY.get();
    }

    public int getChargeCooldown(){
        return 200;
    }

    public boolean canCharge(){
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("charge", chargeCooldown);
        tag.putInt("spawn", spawnCooldown);
        tag.putInt("earthquake", earthquakeCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.chargeCooldown = tag.getInt("charge");
        this.spawnCooldown = tag.getInt("spawn");
        this.earthquakeCooldown = tag.getInt("earthquake");
    }


    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2d).add(Attributes.ATTACK_DAMAGE, 2.0f);
    }

    public int getCooldownModifier() {
        return 100;
    }

    @Override
    public void startAnimation(int arg) {

    }

    public enum Animations{
        CHARGE,
        EARTHQUAKE

    }

}
