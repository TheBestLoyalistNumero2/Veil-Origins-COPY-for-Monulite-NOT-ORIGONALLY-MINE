package com.veilorigins.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class OriginAbility {
    private final String id;
    private final Map<UUID, Integer> cooldown = new HashMap<>();
    private final int baseCooldown;

    public OriginAbility(String id, int cooldownSeconds) {
        this.id = id;
        this.baseCooldown = cooldownSeconds * 20;
    }

    public String getId() { return id; }
    public int getMaxCooldown() {
        /* 22 */     return this.baseCooldown;
        /*    */   }
    public int getCooldown(Player player) {
        return ((Integer)this.cooldown.getOrDefault(player.getUUID(), Integer.valueOf(0))).intValue();
    }

    public void setCooldown(Player player, int ticks) {
        this.cooldown.put(player.getUUID(), Integer.valueOf(ticks));
    }

    public abstract void onActivate(Player player, Level level);
    public abstract boolean canUse(Player player);
    public abstract int getResourceCost();

    public void tick(Player player) {
        // Override in subclasses for custom tick behavior
    }

    public void tickCooldown(Player player) {
        int current = getCooldown(player);
        if (current > 0) this.cooldown.put(player.getUUID(), Integer.valueOf(current -1));
    }

    public boolean isOnCooldown(Player player) {
        return (getCooldown(player) > 0);
    }

    public void startCooldown(Player player) {
        this.cooldown.put(player.getUUID(), Integer.valueOf(this.baseCooldown));
    }
}
