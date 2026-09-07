package com.veilorigins.origins.technomancer;

import com.veilorigins.api.OriginAbility;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RedstonePulseAbility extends OriginAbility {
    private static final int COOLDOWN = 20;
    private static final int DURATION = 1;

    private BlockPos theRedPos = null;

    private final Map<UUID, RedstoneData> redstones = new HashMap<>();

    public RedstonePulseAbility() {
        super("redstone_pulse", 20);
    }

    private static class RedstoneData {
        BlockPos pos;
        long endTime;

        public RedstoneData(BlockPos pos, long endTime) {
            this.pos = pos;
            this.endTime = endTime;
        }
    }

    public void onActivate(Player player, Level level) {
        this.theRedPos = player.blockPosition();
        this.redstones.put(
                player.getUUID(),
                new RedstoneData(this.theRedPos, level.getGameTime() + 1L)
        );

        if (!level.isClientSide) {
            BlockState redstoneBlock = level.getBlockState(this.theRedPos);

            player.sendSystemMessage(
                    Component.literal("Block: " + String.valueOf(redstoneBlock.getBlock()))
            );

            if (redstoneBlock.is(Blocks.REDSTONE_WIRE)) {
                level.setBlockAndUpdate(
                        this.theRedPos,
                        Blocks.REDSTONE_TORCH.defaultBlockState()
                );

                player.sendSystemMessage(
                        Component.literal("Set redstone to less stupid")
                );
            }
        }

        startCooldown(player);
    }

    public void tick(Player player) {
        Level level = player.level();
        UUID id = player.getUUID();
        RedstoneData data = this.redstones.get(id);

        if (data != null && level.getGameTime() > data.endTime) {
            this.redstones.remove(id);

            if (this.theRedPos != null) {
                BlockState redstoneBlock = level.getBlockState(this.theRedPos);

                if (redstoneBlock.is(Blocks.REDSTONE_TORCH)) {
                    level.setBlockAndUpdate(
                            this.theRedPos,
                            Blocks.REDSTONE_WIRE.defaultBlockState()
                    );

                    player.sendSystemMessage(
                            Component.literal("Set redstone to actual redstone")
                    );
                }
            }

            player.sendSystemMessage(
                    Component.literal("Didnt Get Torch")
            );

            return;
        }
    }

    public boolean canUse(Player player) {
        return !isOnCooldown(player);
    }

    public int getResourceCost() {
        return 0;
    }
}