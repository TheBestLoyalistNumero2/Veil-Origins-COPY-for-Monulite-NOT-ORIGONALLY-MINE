package com.veilorigins.origins.telvrnis;

import com.veilorigins.VeilOrigins;
import com.veilorigins.api.OriginAbility;
import com.veilorigins.data.OriginData;
import com.veilorigins.origins.starborne.StarlightBeaconAbility;
import com.veilorigins.origins.umbrakin.DarkCrawlAbility;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = VeilOrigins.MOD_ID)
public class LevitatingFoeAbility extends OriginAbility {
    private static final int DURATION = 15 * 20;
    private static int RESOURCE_COST = 5;

    public static final Map<UUID, LevitatingData> levitatingDat = new HashMap<>();

    public LevitatingFoeAbility() {
        super("levitating_foe", 20);
    }

    public static class LevitatingData {
        public Player player;
        public Entity entity;
        public double distance;
        public long endTime;
        public boolean grabbed;
        public boolean thrown;

        public LevitatingData(Player player, Entity entity, double distance, long endTime, boolean grabbed, boolean thrown) {
            this.player = player;
            this.entity = entity;
            this.distance = distance;
            this.endTime = endTime;
            this.grabbed = grabbed;
            this.thrown = thrown;
        }
    }

    @Override
    public void onActivate(Player player, Level level) {
        Vec3 lookVec = player.getLookAngle();
        Vec3 startPos = player.position().add(0, player.getEyeHeight(), 0);

        System.out.println("activated");
        // Raycast to find target
        Vec3 endPos = startPos.add(lookVec.scale(20));
        HitResult hitResult = level.clip(new net.minecraft.world.level.ClipContext(
                startPos, endPos,
                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                player));

        // Check for entity hit
        net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(
                startPos, hitResult.getLocation()).inflate(1.0);

        LivingEntity hitEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, searchBox)) {
            double distance = entity.position().distanceTo(startPos);
            if (entity != player) {
                if (distance < closestDistance) {
                    closestDistance = distance;
                    hitEntity = entity;
                }
            }
        }
        if (hitEntity != null) {
            double distance = player.distanceTo(hitEntity);
            levitatingDat.put(player.getUUID(), new LevitatingData(player, hitEntity, distance, level.getGameTime() + DURATION, true, false));
        }
        startCooldown(player);
    }

    public void tick(Player player){
        UUID id = player.getUUID();
        LevitatingData data = (LevitatingData)this.levitatingDat.get(id);
        Level level = player.level();
        if (data != null) {
            Entity hitEntity = data.entity;
            Player playvr = data.player;
            if (playvr.getHealth() < 3) {
                player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.DARK_RED) + "Cannot focus properly..."), true);
                levitatingDat.remove(id);
                System.out.println("end time!");
                return;
            }

            Vec3 lookVec = playvr.getLookAngle();
            Vec3 eyePos = playvr.getEyePosition();
            if (data.endTime >= level.getGameTime()){
                if (data.entity != null) {

                    if (level.getScoreboard().getPlayerTeam("itsnouse") == null) {
                        player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                                "team add itsnouse");
                        player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                                "team modify itsnouse color aqua");
                    }

                    Team team = data.entity.getTeam();

                    if (team == null) {
                        player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                                "team join itsnouse " + data.entity.getStringUUID());
                    }

                    OriginData.PlayerOriginData originData = player.getData(OriginData.PLAYER_ORIGIN);

                    if (originData.getResourceBar() <= 0) {
                        player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                                "team leave " + data.entity.getStringUUID());
                        levitatingDat.remove(id);
                        System.out.println("end time!");
                        return;
                    }

                    Vec3 targetPos = eyePos.add(lookVec.scale(data.distance));

                    Vec3 currentPos = hitEntity.position();
                    Vec3 diff = targetPos.subtract(currentPos);

                    if (hitEntity instanceof Player) {
                        levitatingDat.remove(id);
                    }

                    if (player.isCrouching()) {
                        data.grabbed = false;
                    }
                    if (hitEntity.isAlive()) {
                        if (data.grabbed) {
                            ServerLevel serverLevel = (ServerLevel)level;
                            serverLevel.sendParticles(ParticleTypes.UNDERWATER, hitEntity.getX(), hitEntity.getY() + 0.1, hitEntity.getZ(),
                                    5, 0.45, (double)0.25F, 0.45, 0.2);

                            if (player.level().getGameTime() % 20 == 0) {
                                originData.consumeResource(1);
                            }

                            if (data.entity instanceof LivingEntity livingEntity) {
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 1, false, false));
                            }

                            hitEntity.setDeltaMovement(diff.x * 0.5, diff.y * 0.5, diff.z * 0.5);
                            hitEntity.hurtMarked = true;
                        }else {
                            Vec3 velocity = lookVec.scale(0.55D);

                            if (!data.thrown) {
                                int num = 3;
                                Vec3 its_no_use = new Vec3(
                                        lookVec.x * num,
                                        lookVec.y * num,
                                        lookVec.z * num);
                                hitEntity.setDeltaMovement(its_no_use);
                                data.thrown = true;
                            }

                            AABB pathBox = hitEntity.getBoundingBox().expandTowards(velocity.scale(1.5D)).inflate(0.25D);
                            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, pathBox);
                            for (LivingEntity target : targets) {
                                if (target != hitEntity) {
                                    target.hurt(level.damageSources().sonicBoom((Entity)player), 5.0F);
                                    levitatingDat.remove(id);
                                    System.out.println("end time!");
                                }
                            }
                        }
                    } else {
                        levitatingDat.remove(id);
                        System.out.println("end time!");
                    }
                }
            }else {
                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                        "team leave " + data.entity.getStringUUID());
                levitatingDat.remove(id);
                System.out.println("end time!");
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        Entity deadEnt = event.getEntity();
        LevitatingData data = (LevitatingData) levitatingDat.get(deadEnt.getUUID());
        if (data != null){
            if (deadEnt == data.player) {
                levitatingDat.remove(data.player.getUUID());
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return !isOnCooldown(player);
    }

    @Override
    public int getResourceCost() {
        return RESOURCE_COST;
    }
}
