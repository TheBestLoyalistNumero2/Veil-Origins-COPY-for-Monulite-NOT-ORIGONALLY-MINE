/*    */ package com.veilorigins.origins.mogged;
/*    */ import com.veilorigins.api.OriginPassive;
import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class MoggedWeaknessesPassive extends OriginPassive {
/*  8 */   private int tickCounter = 0;
/*    */   
/*    */   public MoggedWeaknessesPassive() {
/* 11 */     super("mycomorph_weaknesses");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick(Player player) {
/* 16 */     this.tickCounter++;
/*    */     
/* 18 */     if (this.tickCounter % 20 == 0)
/*    */     {
/* 20 */       if (player.level().getBlockState(player.blockPosition()).is(Blocks.FIRE) || player
/* 21 */         .isOnFire());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEquip(Player player) {
/* 34 */     player.displayClientMessage(
/* 35 */         (Component)Component.literal(String.valueOf(ChatFormatting.DARK_GREEN) + "As a Mogged, you can poison with a bow. but you dont like Explosives or Fire"), false);
/*    */   }
/*    */   
/*    */   public void onRemove(Player player) {}
/*    */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\origins\mogged\MoggedWeaknessesPassive.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */