package com.hollingsworth.mother_silverfish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.ItemStack;

public class ItemStealRenderer extends MobRenderer<Silverfish, ColoredFishModel<Silverfish>> {
    private static final ResourceLocation SILVERFISH_LOCATION = new ResourceLocation("textures/entity/silverfish.png");

    public ItemStealRenderer(EntityRendererProvider.Context p_174378_) {
        super(p_174378_, new ColoredFishModel(p_174378_.bakeLayer(ModelLayers.SILVERFISH), 0.5f, 0.5f, 1.0f), 0.3F);
    }


    @Override
    public void render(Silverfish pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.pushPose();
        pMatrixStack.translate(0, 0.3, 0);
        ItemStack itemstack = pEntity.getItemInHand(InteractionHand.MAIN_HAND);
        Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemTransforms.TransformType.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pMatrixStack,pBuffer, (int)pEntity.getOnPos().asLong());
        pMatrixStack.popPose();

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