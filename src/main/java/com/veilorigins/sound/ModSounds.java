/*    */ package com.veilorigins.sound;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.neoforged.bus.api.IEventBus;
/*    */ import net.neoforged.neoforge.registries.DeferredRegister;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModSounds
/*    */ {
/* 14 */   public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "veil_origins");
/*    */   
/* 16 */   public static final Supplier<SoundEvent> SONIC_ORIGIN_EVENT = (Supplier<SoundEvent>)SOUND_EVENTS.register("sonic_origin_event", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("veil_origins", "sonic_origin_event")));
/*    */ 
/*    */   
/*    */   public static void register(IEventBus eventBus) {
/* 20 */     SOUND_EVENTS.register(eventBus);
/*    */   }
/*    */ }


/* Location:              C:\Users\LoyDaBoy\Downloads\veil_origins_mod-MONULITE 1.1.1.jar!\com\veilorigins\sound\ModSounds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */