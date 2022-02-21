package com.hollingsworth.mother_silverfish;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.concurrent.ThreadLocalRandom;

public class ModUtil {

    public static boolean isCorrectHarvestLevel(int strength, BlockState state) {
        Tier tier = switch (strength){
            case 1:
                yield Tiers.WOOD;
            case 2:
                yield Tiers.STONE;
            case 3:
                yield Tiers.IRON;
            case 4:
                yield Tiers.DIAMOND;
            case 5:
                yield Tiers.NETHERITE;
            default:
                yield Tiers.WOOD;
        };
        if(strength > 5)
            tier = Tiers.NETHERITE;
        return TierSortingRegistry.isCorrectTierForDrops(tier, state);
    }

    public static double inRange(double min, double max){
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    public static double distanceFrom(BlockPos start, BlockPos end){
        if(start == null || end == null)
            return 0;
        return Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2) + Math.pow(start.getZ() - end.getZ(), 2));
    }

    public static double distanceFrom(Vec3 start, BlockPos end){
        if(start == null || end == null)
            return 0;
        return Math.sqrt(Math.pow(start.x - end.getX(), 2) + Math.pow(start.y - end.getY(), 2) + Math.pow(start.z - end.getZ(), 2));
    }

    public static double distanceFrom(Vec3 start, Vec3 end){
        return Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2) + Math.pow(start.z - end.z, 2));
    }
}
