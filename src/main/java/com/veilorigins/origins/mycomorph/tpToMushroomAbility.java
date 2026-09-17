package com.veilorigins.origins.mycomorph;

import com.veilorigins.api.OriginAbility;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class tpToMushroomAbility extends OriginAbility {
    public tpToMushroomAbility() {
        super("tp_to_mush", 120);
    }

    public void onActivate(Player player, Level level) {
        if (!level.isClientSide()) {
            if (FungalNetworkAbility.networkNodes.get(player.getUUID()) != null) {
                List<BlockPos> nodes = (List)FungalNetworkAbility.networkNodes.get(player.getUUID());
                if (player.isCrouching()) {
                    int urpos = player.getPersistentData().getInt("urpos");
                    ++urpos;
                    if (urpos >= nodes.size()) {
                        urpos = 0;
                        player.getPersistentData().putInt("urpos", urpos);
                        this.setCooldown(player, 60);
                        this.displaymsg(player, urpos, true);
                        return;
                    }

                    player.getPersistentData().putInt("urpos", urpos);
                    this.displaymsg(player, urpos, true);
                    this.setCooldown(player, 60);
                    return;
                }

                if (nodes.isEmpty()) {
                    this.displaymsg(player, 0, false);
                }

                BlockPos first = (BlockPos)nodes.get(player.getPersistentData().getInt("urpos"));
                player.teleportTo((double)first.getX(), (double)first.getY(), (double)first.getZ());
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1, false, false));
                player.hurtMarked = true;
                this.startCooldown(player);
            } else {
                this.displaymsg(player, 0, false);
            }

        }
    }

    public void displaymsg(Player player, Integer in, Boolean bool) {
        if (bool) {
            int iny = in + 1;
            player.displayClientMessage(Component.literal("Teleport set to " + iny), true);
        } else {
            player.displayClientMessage(Component.literal("No node exists to teleport to..."), true);
        }

    }

    public boolean canUse(Player player) {
        return !this.isOnCooldown(player);
    }

    public int getResourceCost() {
        return 50;
    }
}
