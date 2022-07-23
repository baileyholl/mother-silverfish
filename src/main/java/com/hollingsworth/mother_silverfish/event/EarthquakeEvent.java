package com.hollingsworth.mother_silverfish.event;

import com.hollingsworth.mother_silverfish.ModUtil;
import com.hollingsworth.mother_silverfish.entity.SpecialFallingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class EarthquakeEvent implements ITimedEvent {

    int ticks;
    Supplier<BlockPos> origin;
    Level world;
    List<BlockPos> posList = new ArrayList<>();
    int counter;

    public EarthquakeEvent(Level world, Supplier<BlockPos> origin,Supplier<BlockPos> destination) {
        this.origin = origin;
        this.world = world;

        if(origin.get() == null || destination.get() == null){
            return;
        }

        for (BlockPos vec : ModUtil.getLine(origin.get().getX(), origin.get().getZ(), destination.get().getX(), destination.get().getZ(), 2f)) {
            BlockPos adjustedPos = new BlockPos(vec.getX(), origin.get().getY(), vec.getZ());
            if(ModUtil.distanceFrom(origin.get(), adjustedPos) >= 1.5f)
                addPos(adjustedPos);

            for (BlockPos p : BlockPos.betweenClosed(adjustedPos.north(2).east(2), adjustedPos.south(2).west(2))) {
                if (!posList.contains(p) && ModUtil.distanceFrom(origin.get(), p) >= 1.5f)
                    addPos(p.immutable());
            }
            if(ModUtil.distanceFrom(origin.get(), new BlockPos(vec.getX(), origin.get().getY(), vec.getZ())) >= 1.5f)
                addPos(new BlockPos(vec.getX(), origin.get().getY(), vec.getZ()));
        }
    }

    public void addPos(BlockPos pos) {
        if(world.getBlockState(pos).isAir() && !world.getBlockState(pos.below()).isAir() && !posList.contains(pos.below())){
            posList.add(pos.below());
            return;
        }
        posList.add(pos);
    }

    @Override
    public void tick(boolean serverSide) {
//        for (int i = 0; i < 5; i++) {
//            if (counter < posList.size()) {
//                if (world.getRandom().nextFloat() < 0.5) {
//
//                    BlockPos p = posList.get(counter);
//                    FallingBlockEntity blockEntity = new FallingBlockEntity(world, p.getX() + 0.5, p.getY(), p.getZ() + 0.5, world.getBlockState(p));
//                    blockEntity.setDeltaMovement(0, 0.6 + EarthquakeEvent.inRange(-0.1, 0.1), 0.0);
//                    world.addFreshEntity(blockEntity);
//                }
//            }
//            counter++;
//        }

        for(BlockPos p : posList){
            if(world.getBlockState(p).isAir() || world.getBlockState(p).getMaterial().isReplaceable() || !SpecialFallingBlock.canBlockBeHarvested(world, p))
                continue;
            FallingBlockEntity blockEntity = SpecialFallingBlock.fall(world,p, world.getBlockState(p));
            blockEntity.setDeltaMovement(0, 0.6 + EarthquakeEvent.inRange(-0.1, 0.1), 0.0);
        }
        ticks++;

    }

    public static double inRange(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    @Override
    public boolean isExpired() {
        return ticks >= 1;
    }
}
