package com.veilorigins.origins.umbrakin;

import com.veilorigins.api.OriginAbility;
import com.veilorigins.effect.ModEffects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DarkCrawlAbility extends OriginAbility {
    private static final int DURATION = 200;
    private int ACTIVATED = 0;
    private static final double CRAWL_STRENGTH = (double)1.0F;
    private final Map<UUID, DarkData> darkCrawlMP = new HashMap();

    public DarkCrawlAbility() {
        super("dark_crawl", 30);
    }

    public void onActivate(Player player, Level level) {
        int lightLevel = level.getMaxLocalRawBrightness(player.blockPosition());
        if (lightLevel >= 9) {
            player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.RED) + "Too bright! Dark Crawl requires darkness (light level below 10)."), false);
        } else {
            if (lightLevel <= 8) {
                BlockPos darkCrawlPOS = player.blockPosition();
                this.darkCrawlMP.put(player.getUUID(), new DarkData(darkCrawlPOS, level.getGameTime() + 200L));
                player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.BLACK) + "You Dive Into The Darkness."), false);
                player.addEffect(new MobEffectInstance(ModEffects.DARK_CRAWL, 200, 0, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0, false, false));
                this.ACTIVATED = 1;
            }

            this.startCooldown(player);
        }
    }

    public void tick(Player player) {
        Level level = player.level();
        UUID id = player.getUUID();
        DarkData data = (DarkData)this.darkCrawlMP.get(id);
        Vec3 look = player.getLookAngle();
        Vec3 velocity = look.scale((double)1.0F);
        int lightLevel = level.getMaxLocalRawBrightness(player.blockPosition());
        if (data != null) {
            if (this.ACTIVATED <= 1) {
                AABB pathBox = player.getBoundingBox().expandTowards(velocity.scale((double)2.0F)).inflate((double)1.0F);

                for(LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, pathBox)) {
                    if (target != player) {
                        target.hurt(level.damageSources().magic(), 2.0F);
                    }
                }
            }

            if (level.getGameTime() > data.endTime) {
                this.darkCrawlMP.remove(id);
                player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.GRAY) + "Starlight Beacon faded."), false);
                this.ACTIVATED = 0;
                return;
            }

            if (lightLevel >= 9) {
                this.darkCrawlMP.remove(id);
                player.removeEffect(ModEffects.DARK_CRAWL);
                player.removeEffect(MobEffects.INVISIBILITY);
                player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.WHITE) + "The Light Pushes You Out Of The Shadows."), false);
                this.ACTIVATED = 0;
                return;
            }

            if (this.ACTIVATED >= 1 && level.getGameTime() % 1L == 0L && level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                serverLevel.sendParticles(ParticleTypes.SCULK_CHARGE_POP, player.getX(), player.getY() + 0.1, player.getZ(), 30, 0.45, (double)0.25F, 0.45, 0.2);
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX(), player.getY(), player.getZ(), 15, 0.3, 0.15, 0.3, 2.0E-4);
            }
        }

    }

    public boolean canUse(Player player) {
        return !this.isOnCooldown(player);
    }

    public int getResourceCost() {
        return 40;
    }

    private static class DarkData {
        BlockPos pos;
        long endTime;

        public DarkData(BlockPos pos, long endTime) {
            this.pos = pos;
            this.endTime = endTime;
        }
    }
}