/*     */ package com.veilorigins.origins.sonic;
/*     */ 
/*     */ import com.veilorigins.api.OriginAbility;
/*     */ import com.veilorigins.effect.ModEffects;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SpinDashAbility extends OriginAbility {
/*     */   private static final int DURATION = 300;
/*  26 */   public int SPINDASH_ACTIVATED = 0;
/*     */   
/*     */   private static final double CRAWL_STRENGTH = 1.0D;
/*  29 */   private final Map<UUID, DarkData> darkCrawlMP = new HashMap<>();
/*     */   
/*     */   public SpinDashAbility() {
/*  32 */     super("spin_dash", 30);
/*     */   }
/*     */   
/*     */   private static class DarkData {
/*     */     BlockPos pos;
/*     */     long endTime;
/*     */     
/*     */     public DarkData(BlockPos pos, long endTime) {
/*  40 */       this.pos = pos;
/*  41 */       this.endTime = endTime;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onActivate(Player player, Level level) {
/*  48 */     BlockPos darkCrawlPOS = player.blockPosition();
/*  49 */     this.darkCrawlMP.put(player.getUUID(), new DarkData(darkCrawlPOS, level.getGameTime() + 300L));
/*  50 */     player.displayClientMessage((Component)Component.literal(String.valueOf(ChatFormatting.BLUE) + "You Charge a Dash... And Spin It"), false);
/*  51 */     player.addEffect(new MobEffectInstance(ModEffects.DARK_CRAWL, 300, 0, false, false));
/*  52 */     player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 3, false, false));
/*  53 */     player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 0, false, false));
/*  54 */     (player.getAbilities()).invulnerable = true;
/*  55 */     this.SPINDASH_ACTIVATED = 1;
/*  56 */     startCooldown(player);
/*     */   }
/*     */   
/*     */   public void tick(Player player) {
/*  60 */     Level level = player.level();
/*  61 */     UUID id = player.getUUID();
/*  62 */     DarkData data = this.darkCrawlMP.get(id);
/*  63 */     Vec3 look = player.getLookAngle();
/*  64 */     Vec3 velocity = look.scale(1.0D);
/*  65 */     Vec3 startPos = player.getEyePosition().add(look.scale(0.5D));
/*     */     
/*  67 */     if (data != null) {
/*     */       
/*  69 */       if (this.SPINDASH_ACTIVATED >= 1) {
/*  70 */         AABB pathBox = player.getBoundingBox().expandTowards(velocity.scale(2.0D)).inflate(1.0D);
/*  71 */         List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, pathBox);
/*  72 */         for (LivingEntity target : targets) {
/*  73 */           if (target != player) {
/*  74 */             target.hurt(level.damageSources().sonicBoom((Entity)player), 50.0F);
/*     */           }
/*     */         } 
/*  77 */         if (level.getGameTime() % 1L == 0L && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  78 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.SCULK_CHARGE_POP, player
/*  79 */               .getX(), player.getY(), player.getZ(), 250, 0.3D, 0.4D, 0.3D, 0.0D);
/*     */           
/*  81 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.SCULK_CHARGE_POP, startPos.x, startPos.y, startPos.z, 100, 0.1D, 0.1D, 0.1D, 0.0D); }
/*     */       
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  87 */       if (level.getGameTime() > data.endTime) {
/*  88 */         this.darkCrawlMP.remove(id);
/*  89 */         player.displayClientMessage((Component)Component.literal(String.valueOf(ChatFormatting.GRAY) + "Starlight Ball Nomore."), false);
/*  90 */         this.SPINDASH_ACTIVATED = 0;
/*  91 */         (player.getAbilities()).invulnerable = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse(Player player) {
/*  98 */     return !isOnCooldown(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResourceCost() {
/* 103 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\origins\sonic\SpinDashAbility.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */