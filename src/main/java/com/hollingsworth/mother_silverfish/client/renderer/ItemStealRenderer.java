package com.hollingsworth.mother_silverfish.client.renderer;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;

public class ItemStealRenderer extends MobRenderer<Silverfish, ColoredFishModel<Silverfish>> {
    private static final ResourceLocation SILVERFISH_LOCATION = new ResourceLocation("textures/entity/silverfish.png");

    public ItemStealRenderer(EntityRendererProvider.Context p_174378_) {
        super(p_174378_, new ColoredFishModel(p_174378_.bakeLayer(ModelLayers.SILVERFISH), 0.5f, 0.5f, 1.0f), 0.3F);
    }

    protected float getFlipDegrees(Silverfish pLivingEntity) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(Silverfish pEntity) {
        return SILVERFISH_LOCATION;
    }
}