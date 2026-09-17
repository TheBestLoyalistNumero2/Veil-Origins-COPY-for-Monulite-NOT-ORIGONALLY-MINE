package com.veilorigins.origins.riftwalker;

import com.veilorigins.api.OriginPassive;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;

public class SpatialAwarenessPassive extends OriginPassive {
    private int tickCounter = 0;

    public SpatialAwarenessPassive() {
        super("spatial_awareness");
    }

    public void onTick(Player player) {
        ++this.tickCounter;
        if (player.fallDistance > 3.0F) {
            player.fallDistance = 0.0F;
        }

        if (this.tickCounter >= 102) {
            Level level = player.level();
            AABB area = new AABB(player.getX() - (double)100.0F, player.getY() - (double)100.0F, player.getZ() - (double)100.0F, player.getX() + (double)100.0F, player.getY() + (double)100.0F, player.getZ() + (double)100.0F);

            for(Entity entity : level.getEntities(player, area)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity)entity;
                    if (!entity.isSpectator() && !(entity instanceof ArmorStand)) {
                        Team team = entity.getTeam();
                        if (team != null && team.getName().equals("ender")) {
                            player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(), "team leave " + entity.getStringUUID());
                        }
                    }
                }
            }
        }

        if (this.tickCounter >= 1240) {
            this.tickCounter = 0;
            Level level = player.level();
            AABB area = new AABB(player.getX() - (double)14.0F, player.getY() - (double)14.0F, player.getZ() - (double)14.0F, player.getX() + (double)14.0F, player.getY() + (double)14.0F, player.getZ() + (double)14.0F);

            for(Entity entity : level.getEntities(player, area)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity)entity;
                    if (!entity.isSpectator() && !(entity instanceof ArmorStand)) {
                        living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false));
                    }
                }

                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(), "team join ender " + entity.getStringUUID());
                if (entity instanceof ItemEntity) {
                    ItemEntity itemEntity = (ItemEntity)entity;
                    itemEntity.setGlowingTag(true);
                }
            }
        }

    }

    public void onEquip(Player player) {
    }

    public void onRemove(Player player) {
    }
}