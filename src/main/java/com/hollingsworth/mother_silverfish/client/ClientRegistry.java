package com.hollingsworth.mother_silverfish.client;

import com.hollingsworth.mother_silverfish.MotherSilverfish;
import com.hollingsworth.mother_silverfish.client.renderer.HealthStealRenderer;
import com.hollingsworth.mother_silverfish.client.renderer.ItemStealRenderer;
import com.hollingsworth.mother_silverfish.client.renderer.PoisonFishRenderer;
import com.hollingsworth.mother_silverfish.setup.EntityRegistry;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MotherSilverfish.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientRegistry {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.ENTITY_MOTHER_SILVER, SilverfishRenderer::new);
        event.registerEntityRenderer(EntityRegistry.HEALTH_STEAL_FISH, HealthStealRenderer::new);
        event.registerEntityRenderer(EntityRegistry.POISON_FISH, PoisonFishRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ITEM_STEAL_FISH, ItemStealRenderer::new);
    }
}