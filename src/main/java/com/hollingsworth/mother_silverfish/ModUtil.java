package com.hollingsworth.mother_silverfish;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

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

    //Get distance between two entities
    public static double distanceFrom(Entity start, Entity end){
        return distanceFrom(start.position(), end.position());
    }

    public static double distanceFrom(Vec3 start, BlockPos end){
        if(start == null || end == null)
            return 0;
        return Math.sqrt(Math.pow(start.x - end.getX(), 2) + Math.pow(start.y - end.getY(), 2) + Math.pow(start.z - end.getZ(), 2));
    }

    public static double distanceFrom(Vec3 start, Vec3 end){
        return Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2) + Math.pow(start.z - end.z, 2));
    }

    public static List<BlockPos> getLine(int x0, int y0, int x1, int y1, float wd) {
        List<BlockPos> vects = new ArrayList<>();
        int dx = abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx - dy, e2, x2, y2;                          /* error value e_xy */
        float ed = dx + dy == 0 ? 1 : Mth.sqrt((float) dx * dx + (float) dy * dy);

        for (wd = (wd + 1) / 2; ; ) {                                   /* pixel loop */
            vects.add(new BlockPos(x0, 0, y0));
            e2 = err;
            x2 = x0;
            if (2 * e2 >= -dx) {                                           /* x step */
                for (e2 += dy, y2 = y0; e2 < ed * wd && (y1 != y2 || dx > dy); e2 += dx) {
                    vects.add(new BlockPos(x0, 0, y2 += sy));
                }
                if (x0 == x1) break;
                e2 = err;
                err -= dy;
                x0 += sx;
            }
            if (2 * e2 <= dy) {                                            /* y step */
                for (e2 = dx - e2; e2 < ed * wd && (x1 != x2 || dx < dy); e2 += dy) {
                    vects.add(new BlockPos(x2 += sx, 0, y0));
                }
                if (y0 == y1) break;
                err += dx;
                y0 += sy;
            }
        }
        return vects;
    }
}
