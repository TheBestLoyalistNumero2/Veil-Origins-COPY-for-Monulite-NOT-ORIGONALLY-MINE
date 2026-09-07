/*    */ package com.veilorigins.effect;
/*    */ 
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectCategory;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class SonicSlowFallEffect
/*    */   extends MobEffect {
/*    */   public SonicSlowFallEffect(MobEffectCategory category, int color) {
/* 10 */     super(category, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
/* 15 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
/* 20 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\effect\SonicSlowFallEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */