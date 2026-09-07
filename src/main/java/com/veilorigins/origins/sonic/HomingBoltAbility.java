/*     */ package com.veilorigins.origins.sonic;
/*     */ 
/*     */ import com.veilorigins.api.OriginAbility;
/*     */ import com.veilorigins.effect.ModEffects;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class HomingBoltAbility
/*     */   extends OriginAbility
/*     */ {
/*     */   private static final int RESOURCE_COST = 1;
/*     */   private static final float DASH_STRENGTH = 0.05F;
/*  27 */   public static int DISTANCE = 0;
/*     */   
/*     */   public HomingBoltAbility() {
/*  30 */     super("homing_bolt", 5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onActivate(Player player, Level level) {
/*  36 */     if (player.hasEffect(ModEffects.DARK_CRAWL)) {
/*  37 */       DISTANCE = 12;
/*     */     } else {
/*  39 */       DISTANCE = 32;
/*     */     } 
/*     */     
/*  42 */     Vec3 lookVec = player.getLookAngle();
/*  43 */     Vec3 startPos = player.getEyePosition();
/*  44 */     Vec3 endPos = startPos.add(lookVec.scale(DISTANCE));
/*  45 */     Vec3 velocity = lookVec.scale(0.05000000074505806D);
/*     */ 
/*     */     
/*  48 */     player.setDeltaMovement(velocity);
/*     */ 
/*     */     
/*  51 */     BlockHitResult blockHitResult = level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)player));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     AABB searchBox = new AABB(startPos, blockHitResult.getLocation());
/*     */     
/*  60 */     LivingEntity hitEntity = null;
/*  61 */     double closestDistance = Double.MAX_VALUE;
/*     */     
/*  63 */     for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, searchBox)) {
/*  64 */       if (entity != player) {
/*  65 */         double distance = entity.position().distanceTo(startPos);
/*  66 */         if (distance < closestDistance) {
/*  67 */           closestDistance = distance;
/*  68 */           hitEntity = entity;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  73 */     if (hitEntity != null) {
/*  74 */       hitEntity.hurt(level.damageSources().sonicBoom((Entity)player), 1.0E18F);
/*  75 */       hitEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
/*     */       
/*  77 */       player.teleportTo(hitEntity.getX(), hitEntity.getY(), hitEntity.getZ());
/*  78 */       player.addEffect(new MobEffectInstance(ModEffects.SONIC_SLOWFALL_EFFECT, 4, 0, false, false));
/*     */       
/*  80 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  81 */         int steps = (int)startPos.distanceTo(endPos);
/*     */         
/*  83 */         Vec3 playerPozzy = player.getEyePosition(-1.0F);
/*     */         
/*  85 */         for (int i = 0; i <= steps; i++) {
/*  86 */           double progress = i / steps;
/*  87 */           Vec3 hitPos = startPos.lerp(playerPozzy, progress);
/*  88 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.SCULK_CHARGE_POP, hitPos.x, hitPos.y + 1.0D, hitPos.z, 2, 0.1D, 0.2D, 0.1D, 0.0D);
/*     */           
/*  90 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.WHITE_ASH, hitPos.x, hitPos.y + 1.0D, hitPos.z, 6, 0.3D, 0.5D, 0.3D, 0.05D);
/*     */           
/*  92 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.SMALL_GUST, hitPos.x, hitPos.y + 1.0D, hitPos.z, 4, 0.3D, 0.5D, 0.3D, 0.05D);
/*     */ 
/*     */           
/*  95 */           Vec3 targetPos = (hitEntity != null) ? hitEntity.position() : blockHitResult.getLocation();
/*     */           
/*  97 */           player.addDeltaMovement(velocity);
/*  98 */           player.hurtMarked = true;
/*     */         }  }
/*     */     
/*     */     } 
/*     */     
/* 103 */     if (hitEntity == null) {
/* 104 */       player.teleportTo((blockHitResult.getLocation()).x, (blockHitResult.getLocation()).y, (blockHitResult.getLocation()).z);
/* 105 */       player.addEffect(new MobEffectInstance(ModEffects.SONIC_SLOWFALL_EFFECT, 4, 0, false, false));
/*     */       
/* 107 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 108 */         int steps = (int)startPos.distanceTo(endPos);
/*     */         
/* 110 */         Vec3 playerPozzy = player.getEyePosition(-1.0F);
/*     */ 
/*     */         
/* 113 */         for (int i = 0; i <= steps; i++) {
/* 114 */           double progress = i / steps;
/* 115 */           Vec3 particlePos = startPos.lerp(playerPozzy, progress);
/* 116 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.WHITE_ASH, particlePos.x, particlePos.y + 1.0D, particlePos.z, 6, 0.3D, 0.5D, 0.3D, 0.05D);
/*     */           
/* 118 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.SMALL_GUST, particlePos.x, particlePos.y + 1.0D, particlePos.z, 4, 0.3D, 0.5D, 0.3D, 0.05D);
/*     */ 
/*     */           
/* 121 */           player.addDeltaMovement(velocity);
/* 122 */           player.hurtMarked = true;
/*     */         }  }
/*     */     
/*     */     } 
/*     */ 
/*     */     
/* 128 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), (Holder)SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 0.5F, 1.5F);
/*     */ 
/*     */     
/* 131 */     startCooldown(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse(Player player) {
/* 136 */     return !isOnCooldown(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResourceCost() {
/* 141 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\origins\sonic\HomingBoltAbility.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */