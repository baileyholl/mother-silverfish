package com.hollingsworth.mother_silverfish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Silverfish;

public class ColoredFishModel<T extends Silverfish> extends SilverfishModel<T> {
    public ColoredFishModel(ModelPart pRoot) {
        super(pRoot);
    }
    public ColoredFishModel(ModelPart pRoot, float r, float g, float b) {
        super(pRoot);
        this.r = r;
        this.g = g;
        this.b = b;
    }
    private float r = 1.0F;
    private float g = 1.0F;
    private float b = 1.0F;

    public void setColor(float pR, float pG, float pB) {
        this.r = pR;
        this.g = pG;
        this.b = pB;
    }

    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, this.r * pRed, this.g * pGreen, this.b * pBlue, pAlpha);
    }
}
