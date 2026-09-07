/*    */ package com.veilorigins.origins.human;
/*    */ 
/*    */ import com.veilorigins.api.OriginAbility;
/*    */ import com.veilorigins.effect.ModEffects;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HeartAttack
/*    */   extends OriginAbility
/*    */ {
/*    */   public HeartAttack() {
/* 18 */     super("heart_attack", 10);
/*    */   }
/*    */ 
/*    */   @Override
/*    */   public void onActivate(Player player, Level level) {
/* 23 */     player.hurt(level.damageSources().thorns((Entity)player), 2.0F);
/*    */     
/* 25 */     player.addEffect(new MobEffectInstance(ModEffects.HEART_EFFECT, 200, 0, false, false));
/* 26 */     player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 4, false, false));
/*    */     
/* 28 */     startCooldown(player);
/*    */   }
/*    */ 
/*    */   @Override
/*    */   public boolean canUse(Player player) {
/* 33 */     return !isOnCooldown(player);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getResourceCost() {
/* 38 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\origins\human\HeartAttack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */