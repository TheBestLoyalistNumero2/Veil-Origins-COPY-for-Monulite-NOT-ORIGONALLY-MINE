package com.veilorigins.origins.skyborn;

import com.veilorigins.api.OriginAbility;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GroundSlamAbility extends OriginAbility {
    private static final int DURATION = 100;
    private int ACTIVATED = 0;
    private static final double RADIUS = (double)2.5F;
    private double PARADIUS = (double)0.0F;
    private double PARIUS = (double)0.0F;
    private final Map<UUID, SlamData> groundSlamMP = new HashMap();

    public GroundSlamAbility() {
        super("ground_slam", 25);
    }

    public void onActivate(Player player, Level level) {
        BlockPos blockCheck = player.blockPosition().below();
        BlockPos blockCheckDos = player.blockPosition().below().below();
        BlockPos beaconPos = player.blockPosition();
        if (!level.isEmptyBlock(blockCheck)) {
            player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.RED) + "Too close to the ground!"), false);
        } else if (!level.isEmptyBlock(blockCheckDos)) {
            player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.RED) + "Too close to the ground!"), false);
        } else {
            this.groundSlamMP.put(player.getUUID(), new SlamData(beaconPos, level.getGameTime() + 100L));
            this.startCooldown(player);
        }
    }

    public void tick(Player player) {
        Level level = player.level();
        UUID id = player.getUUID();
        SlamData data = (SlamData)this.groundSlamMP.get(id);
        if (data != null) {
            if (level.getGameTime() > data.endTime) {
                this.groundSlamMP.remove(id);
                player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.GRAY) + "You run out of energy to slam down on people."), false);
                this.ACTIVATED = 0;
                return;
            }

            if (!player.onGround()) {
                double currentY = player.getDeltaMovement().y;
                Vec3 slamVec = new Vec3((double)((new Vector3f()).x + 0.0F), Math.min(currentY, (double)-2.5F), (double)((new Vector3f()).z + 0.0F));
                player.setDeltaMovement(slamVec);
                player.hurtMarked = true;
            }

            if (player.onGround()) {
                this.PARADIUS += (double)0.25F;
                this.PARIUS += 0.45;
                if (this.PARADIUS >= (double)2.5F) {
                    this.PARADIUS = (double)0.0F;
                    this.PARIUS = (double)0.0F;
                    this.groundSlamMP.remove(id);
                    player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.GRAY) + "You hit the ground."), false);
                    this.ACTIVATED = 0;
                    return;
                }

                BlockPos pos1 = player.blockPosition();
                this.pushBalls(player, level, pos1);
                this.ACTIVATED = 0;
                if (level.getGameTime() % 1L == 0L && level instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel)level;

                    for(int i = 0; i < 20; ++i) {
                        double angle = (double)i / (double)20.0F * Math.PI * (double)2.0F;
                        double x = (double)pos1.getX() + (double)0.5F + Math.cos(angle) * this.PARADIUS;
                        double y = (double)pos1.getY();
                        double z = (double)pos1.getZ() + (double)0.5F + Math.sin(angle) * this.PARADIUS;
                        serverLevel.sendParticles(ParticleTypes.DUST_PLUME, x, (double)pos1.getY(), z, 1, (double)0.0F, 0.1, (double)0.0F, (double)0.0F);
                        serverLevel.sendParticles(ParticleTypes.INFESTED, x, (double)pos1.getY(), z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
                        serverLevel.sendParticles(ParticleTypes.SPORE_BLOSSOM_AIR, x, (double)pos1.getY() + 0.2, z, 1, 0.4, (double)0.0F, 0.4, (double)0.5F);
                    }
                }

                if (level.getGameTime() % 1L == 0L && level instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel)level;

                    for(int i = 0; i < 20; ++i) {
                        double angle = (double)i / (double)20.0F * Math.PI * (double)2.0F;
                        double x = (double)pos1.getX() + (double)0.5F + Math.cos(angle) * this.PARIUS;
                        double y = (double)pos1.getY();
                        double z = (double)pos1.getZ() + (double)0.5F + Math.sin(angle) * this.PARIUS;
                        serverLevel.sendParticles(ParticleTypes.CRIT, x, (double)pos1.getY() + 0.1, z, 2, 0.7, (double)0.0F, 0.7, (double)0.0F);
                        serverLevel.sendParticles(ParticleTypes.CRIMSON_SPORE, x, (double)pos1.getY() - 0.2, z, 1, 0.1, (double)0.0F, 0.1, (double)0.0F);
                    }
                }
            }
        }

    }

    private void pushBalls(Player player, Level level, BlockPos pos1) {
        AABB area = (new AABB(pos1)).inflate((double)2.5F);

        for(Entity entity : level.getEntities((Entity)null, area)) {
            Vec3 direction = entity.position().subtract(new Vec3((double)pos1.getX(), (double)pos1.getY(), (double)pos1.getZ())).normalize();
            if (entity instanceof LivingEntity) {
                entity.setDeltaMovement(direction.x * 2.6, direction.y + 1.15, direction.z * 2.6);
            }
        }

    }

    public boolean canUse(Player player) {
        return !this.isOnCooldown(player);
    }

    public int getResourceCost() {
        return 15;
    }

    private static class SlamData {
        BlockPos pos;
        long endTime;

        public SlamData(BlockPos pos, long endTime) {
            this.pos = pos;
            this.endTime = endTime;
        }
    }
}