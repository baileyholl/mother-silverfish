package com.hollingsworth.mother_silverfish.network;

import com.hollingsworth.mother_silverfish.MotherSilverfish;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;

    private static int ID = 0;
    public static int nextID(){return ID++;}
    public static void registerMessages(){
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MotherSilverfish.MODID, "network"), () -> "1.0", s->true, s->true);


        INSTANCE.registerMessage(nextID(),
                PacketAnimEntity.class,
                PacketAnimEntity::encode,
                PacketAnimEntity::decode,
                PacketAnimEntity.Handler::handle);

    }

    public static void sendToNearby(Level world, BlockPos pos, Object toSend){
        if (world instanceof ServerLevel) {
            ServerLevel ws = (ServerLevel) world;
            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(Level world, Entity e, Object toSend) {
        sendToNearby(world, e.blockPosition(), toSend);
    }

    public static void sendToPlayer(Object msg, Player player) {
        if (EffectiveSide.get() == LogicalSide.SERVER) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
        }
    }
}
