package com.example.examplemod.setup;

import com.example.examplemod.MotherSilverfish;
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
   // public static EntityType<FamiliarStarbuncle> ENTITY_FAMILIAR_STARBUNCLE = null;
   public static EntityType<com.example.examplemod.entity.MotherSilverfish> ENTITY_MOTHER_SILVER = null;

    @Mod.EventBusSubscriber(modid = MotherSilverfish.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {


        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
           ENTITY_MOTHER_SILVER = build(
                    "mother_silverfish",
                    EntityType.Builder.<com.example.examplemod.entity.MotherSilverfish>of(com.example.examplemod.entity.MotherSilverfish::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .setTrackingRange(20)
                            .setShouldReceiveVelocityUpdates(true)
                            .setUpdateInterval(10));
           event.getRegistry().registerAll(ENTITY_MOTHER_SILVER);
        }

        @SubscribeEvent
        public static void registerEntities(final EntityAttributeCreationEvent event) {
            event.put(ENTITY_MOTHER_SILVER, com.example.examplemod.entity.MotherSilverfish.attributes().build());

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
