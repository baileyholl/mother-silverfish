package com.hollingsworth.mother_silverfish.client.renderer;

import com.hollingsworth.mother_silverfish.MotherSilverfish;
import com.hollingsworth.mother_silverfish.entity.MotherSilverfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MotherSilverfishModel extends AnimatedGeoModel<MotherSilverfishEntity> {
    @Override
    public ResourceLocation getModelLocation(MotherSilverfishEntity wand) {
        return new ResourceLocation(MotherSilverfish.MODID , "geo/mother_silverfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MotherSilverfishEntity wand) {
        return  new ResourceLocation(MotherSilverfish.MODID, "textures/entity/mother_silverfish.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MotherSilverfishEntity wand) {
        return new ResourceLocation(MotherSilverfish.MODID , "animations/mother_silverfish.json");
    }
}
