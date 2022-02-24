package com.hollingsworth.mother_silverfish.client.renderer;

import com.hollingsworth.mother_silverfish.entity.MotherSilverfishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MotherSilverfishRenderer extends GeoEntityRenderer<MotherSilverfishEntity> {
    public MotherSilverfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MotherSilverfishModel());
    }


}
