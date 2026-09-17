/*    */ package com.veilorigins.origins.sonic;
/*    */ 
/*    */ import com.veilorigins.api.OriginPassive;
/*    */ import com.veilorigins.effect.ModEffects;
/*    */ import com.veilorigins.event.OriginEventHandler;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpeedPassive
/*    */   extends OriginPassive
/*    */ {
/*    */   public SpeedPassive() {
/* 29 */     super("the_wind");
/*    */   }
/*    */   
/*    */   private static final float WIND_STRENGTH = 0.25F;
/*    */   
/*    */   public void onTick(Player player) {
/* 38 */     Vec3 lookVec = player.getLookAngle();
/* 39 */     Vec3 velocity = lookVec.scale(0.25D);
/*    */     
/* 41 */     Level level = player.level();
/*    */     
/* 52 */     OriginEventHandler.skybornFalldm(player);
/*    */     
/* 54 */     if (player.onGround() && 
/* 55 */       player.isSprinting()) {
/*    */       
/* 57 */       AABB pathBox = player.getBoundingBox().expandTowards(player.getLookAngle().scale(2.0D)).inflate(1.0D);
/* 58 */       List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, pathBox);
/* 59 */       for (LivingEntity target : targets) {
/* 60 */         if (target != player) {
/* 61 */           target.hurt(level.damageSources().sonicBoom((Entity)player), 5.0F);
/* 62 */           target.push(new Vec3(velocity.x * -1.0D, velocity.y * -1.0D, velocity.z * -1.0D));
/*    */         } 
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 70 */       player.addEffect(new MobEffectInstance(ModEffects.SONIC_RUNNING_EFFECT, 2, 0, false, false));
/*    */       
/* 72 */       if (level.getGameTime() % 2L == 0L && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 73 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.SMALL_GUST, player
/* 74 */             .getX(), player.getY() + 0.2D, player.getZ(), 10, 0.2D, 0.2D, 0.2D, 0.8D);
/*    */         
/* 76 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.WHITE_SMOKE, player
/* 77 */             .getX(), player.getY() + 0.2D, player.getZ(), 10, 0.2D, 0.0D, 0.2D, 0.2D);
/*    */         
/* 79 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.SMALL_FLAME, player
/* 80 */             .getX(), player.getY() + 0.15D, player.getZ(), 3, 0.2D, 0.0D, 0.2D, 0.2D);
/*    */         
/* 82 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.DRIPPING_LAVA, player
/* 83 */             .getX(), player.getY() + 0.01D, player.getZ(), 3, 0.1D, 0.0D, 0.1D, 0.2D); }
/*    */     
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 89 */     if (level.getGameTime() % 5L == 0L && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 90 */       serverLevel.sendParticles((ParticleOptions)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, player
/* 91 */           .getX(), player.getY(), player.getZ(), 2, 0.0D, 0.0D, 0.0D, 0.2D); }
/*    */   
/*    */   }
/*    */ }