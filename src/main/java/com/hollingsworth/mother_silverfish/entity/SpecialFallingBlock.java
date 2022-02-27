package com.hollingsworth.mother_silverfish.entity;

import com.hollingsworth.mother_silverfish.setup.EntityRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpecialFallingBlock extends FallingBlockEntity {

    boolean didLaunch;

    public SpecialFallingBlock(EntityType<? extends FallingBlockEntity> p_31950_, Level p_31951_) {
        super(p_31950_, p_31951_);
    }


    public SpecialFallingBlock(Level world, double v, int y, double v1, BlockState blockState) {
        super(world, v, y, v1, blockState);

    }


    @Override
    public boolean canCollideWith(Entity pEntity) {
        return !(pEntity instanceof Player) &&  super.canCollideWith(pEntity);
    }

    @Override
    public EntityType<?> getType() {
        return EntityRegistry.FALLING_BLOCK;
    }

    @Override
    public void tick() {
        super.tick();

        if(!level.isClientSide && !didLaunch){
            didLaunch = true;
            level.getEntities(null, getBoundingBox().inflate(1.5, 1.5, 1.5)).forEach(entity -> {
                if(entity instanceof LivingEntity) {
                    entity.hurt(DamageSource.STALAGMITE, 3);
                    entity.setDeltaMovement(0, 1.3, 0);
                }
            });


        }
    }
}
