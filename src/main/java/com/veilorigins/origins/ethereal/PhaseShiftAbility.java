package com.veilorigins.origins.ethereal;

import com.veilorigins.api.OriginAbility;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public class PhaseShiftAbility extends OriginAbility {
    private static final int COOLDOWN = 200;
    private static final int DURATION = 400;
    private static final int HUNGER_COST = 5;
    private final Map<UUID, GameType> originalModes = new HashMap();
    private final Map<UUID, Integer> activePhases = new HashMap();

    public PhaseShiftAbility() {
        super("phase_shift", 200);
    }

    public void onActivate(Player player, Level level) {
        if (!player.isSpectator()) {
            if (level instanceof ServerLevel) {
                ServerLevel sl = (ServerLevel)level;
                ServerPlayer sp = (ServerPlayer)player;
                this.originalModes.put(player.getUUID(), sp.gameMode.getGameModeForPlayer());
                sp.setGameMode(GameType.SPECTATOR);
                this.activePhases.put(player.getUUID(), 400);
                player.causeFoodExhaustion(5.0F);
                this.startCooldown(player);
            }

        }
    }

    public void tick(Player player) {
        if (!player.level().isClientSide()) {
            UUID id = player.getUUID();
            if (this.activePhases.containsKey(id)) {
                int ticks = (Integer)this.activePhases.get(id);
                if (ticks == 200) {
                    player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.AQUA) + "Phasing Form Fading... Get Back To The Surface Soon."), false);
                }

                if (ticks == 100) {
                    player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.DARK_AQUA) + "Phasing Form Fading In 5 Seconds..."), false);
                }

                if (ticks > 0) {
                    this.activePhases.put(id, ticks - 1);
                } else {
                    this.activePhases.remove(id);
                    if (player instanceof ServerPlayer) {
                        ServerPlayer sp = (ServerPlayer)player;
                        GameType original = (GameType)this.originalModes.getOrDefault(id, GameType.SURVIVAL);
                        sp.setGameMode(original);
                        this.originalModes.remove(id);
                        player.displayClientMessage(Component.literal(String.valueOf(ChatFormatting.GRAY) + "Your Body Comes Back..."), false);
                    }
                }
            }

        }
    }

    public boolean canUse(Player player) {
        return !this.isOnCooldown(player) && player.getFoodData().getFoodLevel() >= 5;
    }

    public int getResourceCost() {
        return 45;
    }

    public boolean isActive(Player player) {
        return this.activePhases.containsKey(player.getUUID());
    }
}