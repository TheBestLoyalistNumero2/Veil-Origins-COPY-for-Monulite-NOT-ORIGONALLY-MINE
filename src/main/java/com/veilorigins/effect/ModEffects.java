/*    */ package com.veilorigins.effect;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectCategory;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.neoforged.bus.api.IEventBus;
/*    */ import net.neoforged.neoforge.registries.DeferredRegister;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModEffects
/*    */ {
/* 17 */   public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, "veil_origins");
/*    */   
/* 19 */   public static final Holder<MobEffect> HEART_EFFECT = (Holder<MobEffect>)MOB_EFFECTS.register("heart", () -> (new HeartEffect(MobEffectCategory.NEUTRAL, 3599275)).addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath("veil_origins", "heart"), -19.0D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, ResourceLocation.fromNamespaceAndPath("veil_origins", "heart"), -1.0D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.STEP_HEIGHT, ResourceLocation.fromNamespaceAndPath("veil_origins", "heart"), 1.0D, AttributeModifier.Operation.ADD_VALUE));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final Holder<MobEffect> DARK_CRAWL = (Holder<MobEffect>)MOB_EFFECTS.register("dark_crawl", () -> (new DarkCrawlEffect(MobEffectCategory.NEUTRAL, 3599275)).addAttributeModifier(Attributes.SCALE, ResourceLocation.fromNamespaceAndPath("veil_origins", "dark_crawl"), -0.6D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath("veil_origins", "dark_crawl"), 0.4D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath("veil_origins", "dark_crawl"), -0.5D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.STEP_HEIGHT, ResourceLocation.fromNamespaceAndPath("veil_origins", "dark_crawl"), 10.0D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.FALL_DAMAGE_MULTIPLIER, ResourceLocation.fromNamespaceAndPath("veil_origins", "dark_crawl"), -1.0D, AttributeModifier.Operation.ADD_VALUE));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public static final Holder<MobEffect> SONIC_SLOWFALL_EFFECT = (Holder<MobEffect>)MOB_EFFECTS.register("sonic_slowfall", () -> (new SonicSlowFallEffect(MobEffectCategory.NEUTRAL, 3599275)).addAttributeModifier(Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath("veil_origins", "sonic_slowfall"), -0.01D, AttributeModifier.Operation.ADD_VALUE));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public static final Holder<MobEffect> SONIC_RUNNING_EFFECT = (Holder<MobEffect>)MOB_EFFECTS.register("sonic_run", () -> (new DarkCrawlEffect(MobEffectCategory.NEUTRAL, 3599275)).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath("veil_origins", "sonic_run"), 0.05D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.STEP_HEIGHT, ResourceLocation.fromNamespaceAndPath("veil_origins", "sonic_run"), 10.0D, AttributeModifier.Operation.ADD_VALUE));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void register(IEventBus eventBus) {
/* 63 */     MOB_EFFECTS.register(eventBus);
/*    */   }
/*    */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\effect\ModEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */