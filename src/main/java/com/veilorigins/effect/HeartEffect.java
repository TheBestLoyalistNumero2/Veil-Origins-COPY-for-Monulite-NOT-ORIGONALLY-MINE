/*    */ package com.veilorigins.effect;
/*    */ 
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectCategory;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class HeartEffect
/*    */   extends MobEffect
/*    */ {
/*    */   public HeartEffect(MobEffectCategory category, int color) {
/* 11 */     super(category, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
/* 16 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
/* 21 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\effect\HeartEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */