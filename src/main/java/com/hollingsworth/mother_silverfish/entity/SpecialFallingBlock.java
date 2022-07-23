package com.hollingsworth.mother_silverfish.entity;

import com.hollingsworth.mother_silverfish.ModUtil;
import com.hollingsworth.mother_silverfish.setup.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class SpecialFallingBlock extends FallingBlockEntity {

    boolean didLaunch;

    public SpecialFallingBlock(EntityType<? extends FallingBlockEntity> p_31950_, Level p_31951_) {
        super(p_31950_, p_31951_);
    }


    public SpecialFallingBlock(Level world, double v, double y, double v1, BlockState blockState) {
        this(EntityRegistry.FALLING_BLOCK, world);
        this.blockState = blockState;
        this.blocksBuilding = true;
        this.setPos(v, y, v1);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = v;
        this.yo = y;
        this.zo = v1;
        this.setStartPos(this.blockPosition());
    }

    public static SpecialFallingBlock fall(Level level, BlockPos pos, BlockState state) {
        SpecialFallingBlock fallingblockentity = new SpecialFallingBlock(level, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : state);
        level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(fallingblockentity);
        return fallingblockentity;
    }

    public static boolean canBlockBeHarvested(Level world, BlockPos pos){
        return (world.getBlockState(pos).getDestroySpeed(world, pos) >= 0 && ModUtil.isCorrectHarvestLevel(5, world.getBlockState(pos))) ||
                world.getBlockState(pos).is(MotherSilverfishEntity.BREAK_WHITELIST);
    }


    @Override
    public boolean canCollideWith(Entity pEntity) {
        return !(pEntity instanceof Player) && super.canCollideWith(pEntity) && !(pEntity instanceof SpecialFallingBlock);
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(this.getBlockState()));
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
