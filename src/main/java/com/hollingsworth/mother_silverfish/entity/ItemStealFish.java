package com.hollingsworth.mother_silverfish.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class ItemStealFish extends BabyFish{
    public ItemStealFish(EntityType<ItemStealFish> p_33523_, Level p_33524_) {
        super(p_33523_, p_33524_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(!level.isClientSide){
            if(pEntity instanceof Player player){
                if(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()){
                    ItemStack playerStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                    ItemStack splitStack = playerStack.split(1);
                    this.setItemSlot(EquipmentSlot.MAINHAND, splitStack);
                }
            }

        }

        return super.doHurtTarget(pEntity);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            ItemStack itemstack = this.getItemBySlot(equipmentslot);

            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.spawnAtLocation(itemstack);
                this.setItemSlot(equipmentslot, ItemStack.EMPTY);
            }
        }

    }

    @Override
    protected float getEquipmentDropChance(EquipmentSlot pSlot) {
        return 1.0f;
    }
}
