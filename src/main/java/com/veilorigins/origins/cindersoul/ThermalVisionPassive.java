package com.veilorigins.origins.cindersoul;

import com.veilorigins.api.OriginPassive;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;

public class ThermalVisionPassive extends OriginPassive {
    private int tickCounter = 0;
    private int glowing = 0;

    public ThermalVisionPassive() {
        super("thermal_vision");
    }

    public void onTick(Player player) {
        ++this.tickCounter;
        if (this.tickCounter % 20 == 0) {
            AABB area = (new AABB(player.blockPosition())).inflate((double)10.0F);
            List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, area);
            AABB areaRmvl = (new AABB(player.blockPosition())).inflate((double)32.0F);

            for(LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, areaRmvl)) {
                if (entity != player && !(entity instanceof ArmorStand)) {
                    Team team = entity.getTeam();
                    if (this.glowing == 1 && team != null && team.getName().equals("fire") && !entity.hasEffect(MobEffects.GLOWING)) {
                        player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(), "team empty fire");
                        this.glowing = 0;
                    }
                }
            }

            for(LivingEntity entity : entities) {
                if (entity != player && !(entity instanceof ArmorStand)) {
                    Team team = entity.getTeam();
                    if (team == null || !team.getName().equals("fire")) {
                        player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(), "team join fire " + entity.getStringUUID());
                    }

                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 30, 0, false, false));
                    this.glowing = 1;
                }
            }
        }

    }

    public void onEquip(Player player) {
    }

    public void onRemove(Player player) {
    }
}