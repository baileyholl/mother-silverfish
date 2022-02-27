package com.hollingsworth.mother_silverfish.setup;

import com.hollingsworth.mother_silverfish.MotherSilverfish;
import com.hollingsworth.mother_silverfish.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(MotherSilverfish.MODID)
public class EntityRegistry {

    public static EntityType<MotherSilverfishEntity> ENTITY_MOTHER_SILVER = null;
    public static EntityType<BabyPoisonFish> POISON_FISH = null;
    public static EntityType<HealthStealFish> HEALTH_STEAL_FISH = null;
    public static EntityType<ItemStealFish> ITEM_STEAL_FISH = null;
    public static EntityType<SpecialFallingBlock> FALLING_BLOCK = null;

    @Mod.EventBusSubscriber(modid = MotherSilverfish.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {


        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
           ENTITY_MOTHER_SILVER = build(
                    "mother_silverfish",
                    EntityType.Builder.of(MotherSilverfishEntity::new, MobCategory.MONSTER)
                            .sized(2f, 1.7f)
                            .setTrackingRange(10));
            POISON_FISH = build(
                    "poison_silverfish",
                    EntityType.Builder.of(BabyPoisonFish::new, MobCategory.MONSTER)
                            .sized(0.5f, 0.5f)
                            .setTrackingRange(10));
            HEALTH_STEAL_FISH = build(
                    "leech_silverfish",
                    EntityType.Builder.of(HealthStealFish::new, MobCategory.MONSTER)
                            .sized(0.5f, 0.5f)
                            .setTrackingRange(10));
            ITEM_STEAL_FISH = build(
                    "thief_silverfish",
                    EntityType.Builder.of(ItemStealFish::new, MobCategory.MONSTER)
                            .sized(0.5f, 0.5f)
                            .setTrackingRange(10));

            FALLING_BLOCK = build(
                    "special_falling_block",
                                EntityType.Builder.<SpecialFallingBlock>of(SpecialFallingBlock::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));
           event.getRegistry().registerAll(ENTITY_MOTHER_SILVER,
                   POISON_FISH, HEALTH_STEAL_FISH, ITEM_STEAL_FISH, FALLING_BLOCK);
        }

        @SubscribeEvent
        public static void registerEntities(final EntityAttributeCreationEvent event) {
            event.put(ENTITY_MOTHER_SILVER, MotherSilverfishEntity.attributes().build());
            event.put(POISON_FISH, BabyFish.attributes().build());
            event.put(HEALTH_STEAL_FISH, BabyFish.attributes().build());
            event.put(ITEM_STEAL_FISH, BabyFish.attributes().build());


        }
        private static <T extends Entity> EntityType<T> build(final String name, final EntityType.Builder<T> builder) {
            final ResourceLocation registryName = new ResourceLocation(MotherSilverfish.MODID, name);

            final EntityType<T> entityType = builder
                    .build(registryName.toString());

            entityType.setRegistryName(registryName);

            return entityType;
        }
    }
}
