package com.veilorigins.origins.technomancer;

import com.veilorigins.api.OriginAbility;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class JetpackAbility extends OriginAbility {
    private static final int COOLDOWN = 10;
    private static final int HUNGER_COST = 3;
    private static final double DASH_STRENGTH = 3.5D;

    public JetpackAbility() {
        super("jetpack", 10);
    }

    public void onActivate(Player player, Level level) {
        Vec3 look = player.getLookAngle();
        Vec3 velocity = look.scale(3.5D);

        player.setDeltaMovement(velocity);
        player.hurtMarked = true;
        player.fallDistance = 0.0F;

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.DRAGON_FIREBALL_EXPLODE,
                SoundSource.PLAYERS,
                1.0F,
                1.5F
        );

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.FIREWORK_ROCKET_LARGE_BLAST_FAR,
                SoundSource.PLAYERS,
                1.0F,
                1.5F
        );

        AABB pathBox = player.getBoundingBox()
                .expandTowards(velocity.scale(10.0D))
                .inflate(0.0D);

        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level;

            for (int i = 0; i < 10; i++) {
                double d = i / 10.0D;

                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        player.getX() - look.x * d * 2.0D,
                        player.getY() - look.y * d * 2.0D,
                        player.getZ() - look.z * d * 2.0D,
                        5,
                        0.2D,
                        0.1D,
                        0.2D,
                        0.01D
                );

                serverLevel.sendParticles(
                        ParticleTypes.CRIT,
                        player.getX() - look.x * d * 2.0D,
                        player.getY() - look.y * d * 2.0D,
                        player.getZ() - look.z * d * 2.0D,
                        40,
                        0.2D,
                        0.2D,
                        0.2D,
                        0.5D
                );

                serverLevel.sendParticles(
                        ParticleTypes.END_ROD,
                        player.getX() - look.x * d * 2.0D,
                        player.getY() - look.y * d * 2.0D + 1.0D,
                        player.getZ() - look.z * d * 2.0D,
                        10,
                        0.4D,
                        0.2D,
                        0.4D,
                        0.5D
                );
            }
        }

        player.causeFoodExhaustion(3.0F);
        startCooldown(player);
    }

    public boolean canUse(Player player) {
        return (!isOnCooldown(player) && player.getFoodData().getFoodLevel() >= 2);
    }

    public int getResourceCost() {
        return 55;
    }
}