package com.veilorigins.origins.telvrnis;

import com.veilorigins.VeilOrigins;
import com.veilorigins.api.OriginAbility;
import com.veilorigins.client.KeyBindings;
import com.veilorigins.data.OriginData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = VeilOrigins.MOD_ID)
public class LevitateSelfAbility extends OriginAbility {

    private static final Map<UUID, SelfLevitateData> selfLevData = new HashMap<>();

    public LevitateSelfAbility(){super("levitate_self", 25);}

    private static class SelfLevitateData {
        long endTime;
        long coolTime;

        public SelfLevitateData(long endTime, long coolTime) {
            this.endTime = endTime;
            this.coolTime = coolTime;
        }
    }

    @Override
    public void onActivate(Player player, Level level) {
        if (player.getHealth() < 5) {
            player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.DARK_RED) + "Being weak makes you less able to fly"), true);
            this.selfLevData.put(player.getUUID(), new SelfLevitateData(level.getGameTime() + 150, level.getGameTime() + 15));
            startCooldown(player);
        } else {
            this.selfLevData.put(player.getUUID(), new SelfLevitateData(level.getGameTime() + 400, level.getGameTime() + 15));
            startCooldown(player);
        }
    }

    public void tick(Player player){
        UUID id = player.getUUID();
        Level level = player.level();
        SelfLevitateData data = (SelfLevitateData)this.selfLevData.get(id);
        if (data != null) {

            if (KeyBindings.ABILITY_2.consumeClick()) {
                if (data.coolTime <= level.getGameTime()) {
                    player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                            "team leave " + player.getStringUUID());
                    selfLevData.remove(id);
                    System.out.println("end time!");
                    return;
                }
            }

            if (level.getScoreboard().getPlayerTeam("itsnouse") == null) {
                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                        "team add itsnouse");
                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                        "team modify itsnouse color aqua");
            }

            Team team = player.getTeam();

            if (team == null) {
                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                        "team join itsnouse " + player.getStringUUID());
            }

            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 1, false, false));

            if (data.endTime <= level.getGameTime()){
                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                        "team leave " + player.getStringUUID());
                selfLevData.remove(id);
                System.out.println("end time!");
                return;
            }

            OriginData.PlayerOriginData originData = player.getData(OriginData.PLAYER_ORIGIN);

            if (player.level().getGameTime() % 5 == 0) {
                originData.consumeResource(1);
            }

            if (originData.getResourceBar() <= 0) {
                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(),
                        "team leave " + player.getStringUUID());
                selfLevData.remove(id);
                System.out.println("end time!");
                return;
            }

            Vec3 look = player.getLookAngle();
            Vec3 velocity = look.scale(1);

            player.setDeltaMovement(velocity);
            player.hurtMarked = true;
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        Entity deadEnt = event.getEntity();
        if (deadEnt instanceof Player player) {
            if (selfLevData.get(player.getUUID()) != null) {
                selfLevData.remove(player.getUUID());
            }
        }
    }

    @Override
    public boolean canUse(Player player) {return !isOnCooldown(player);}

    @Override
    public int getResourceCost() {
        return 0;
    }
}
