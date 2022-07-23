package com.hollingsworth.mother_silverfish.entity;

import com.hollingsworth.mother_silverfish.Config;
import com.hollingsworth.mother_silverfish.MotherSilverfish;
import com.hollingsworth.mother_silverfish.entity.goals.ChargeGoal;
import com.hollingsworth.mother_silverfish.entity.goals.EarthquakeGoal;
import com.hollingsworth.mother_silverfish.setup.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MotherSilverfishEntity extends Monster implements IAnimationListener, IAnimatable {
    public static TagKey<Block> BREAK_WHITELIST = BlockTags.create(new ResourceLocation(MotherSilverfish.MODID, "breakable"));

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true).setCreateWorldFog(true).setPlayBossMusic(true);


    public int earthquakeCooldown;
    public int spawnCooldown;
    public int chargeCooldown;
    public boolean isCharging;


    public MotherSilverfishEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        maxUpStep = 2.0f;
        setPersistenceRequired();
    }

    @Override
    public void setYBodyRot(float pOffset) {
        this.yBodyRot = pOffset;
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

        if(!level.isClientSide && spawnCooldown <= 0 && getTarget() != null){
            Silverfish silverfish = getSpawn();
            level.addFreshEntity(silverfish);
            spawnCooldown = getSpawnCooldown();
        }
    }


    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 1.0f;
    }

    @Override
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox();
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
        if(getTarget() != null)
            babyFish.setTarget(getTarget());
        return babyFish;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ChargeGoal(this));
        this.goalSelector.addGoal(3, new EarthquakeGoal(this, () -> this.earthquakeCooldown <= 0));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public int getEarthquakeCooldown(){
        return Config.EASY_QUAKE_COOLDOWN.get();
    }

    public int getSpawnCooldown(){
        return Config.EASY_SUMMON_BABY.get();
    }

    public int getChargeCooldown(){
        return Config.EASY_CHARGE_COOLDOWN.get();
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, Config.EASY_MOTHER_HEALTH.get())
                .add(Attributes.MOVEMENT_SPEED, Config.EASY_MOTHER_MOVE_SPEED.get())
                .add(Attributes.ATTACK_DAMAGE, Config.EASY_MOTHER_ATTACK_DAMAGE.get())
                .add(Attributes.ARMOR_TOUGHNESS, Config.EASY_MOTHER_TOUGHNESS.get())
                .add(Attributes.ARMOR, Config.EASY_MOTHER_ARMOR.get())
                .add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.KNOCKBACK_RESISTANCE, Config.EASY_MOTHER_KNOCKBACK_RESISTANCE.get())
                .add(Attributes.ATTACK_KNOCKBACK, Config.EASY_MOTHER_ATTACK_KNOCKBACK.get());
    }

    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
    }

    public int getCooldownModifier() {
        return 100;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource == DamageSource.LAVA || pSource == DamageSource.IN_WALL || pSource == DamageSource.DROWN)
            return false;
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(pEntity instanceof Silverfish)
            return false;

        return super.doHurtTarget(pEntity);
    }

    @Override
    public void startAnimation(int arg) {
        try {
            if(attackController == null)
                return;
            if (arg == Animations.CHARGE.ordinal()) {
                if (attackController.getCurrentAnimation() != null && (attackController.getCurrentAnimation().animationName.equals("charge"))) {
                    return;
                }
                attackController.markNeedsReload();
                attackController.setAnimation(new AnimationBuilder().addAnimation("charge", false).addAnimation("idle"));
            }

            if (arg == Animations.EARTHQUAKE.ordinal()) {
                if (attackController.getCurrentAnimation() != null && (attackController.getCurrentAnimation().animationName.equals("slam_master"))) {
                    return;
                }
                attackController.markNeedsReload();
                attackController.setAnimation(new AnimationBuilder().addAnimation("slam_master", false).addAnimation("idle"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
        super.checkFallDamage(p_184231_1_, p_184231_3_, p_184231_4_, p_184231_5_);
        this.fallDistance = Math.min(fallDistance, 10);
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0f;
    }

    @Override
    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.remove(RemovalReason.DISCARDED);
        } else {
            this.noActionTime = 0;
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void startSeenByPlayer(ServerPlayer p_184178_1_) {
        super.startSeenByPlayer(p_184178_1_);
        this.bossEvent.addPlayer(p_184178_1_);
    }

    public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
        super.stopSeenByPlayer(p_184203_1_);
        this.bossEvent.removePlayer(p_184203_1_);
    }

    public boolean canChangeDimensions() {
        return false;
    }

    public PlayState crawlController(AnimationEvent arg) {
        Animation animation = arg.getController().getCurrentAnimation();
        if(arg.isMoving()) {
            if(animation != null && (animation.animationName.equals("slam_master") || animation.animationName.equals("charge")))
                return PlayState.STOP;

            arg.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    AnimationController attackController;
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "crawl", 1, this::crawlController));
        attackController = new AnimationController<>(this, "attackController", 1, this::attackPredicate);
        data.addAnimationController(attackController);
    }

    private <T extends IAnimatable> PlayState attackPredicate(AnimationEvent<T> tAnimationEvent) {
        return PlayState.CONTINUE;
    }

    AnimationFactory factory = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public enum Animations{
        CHARGE,
        EARTHQUAKE

    }

}
