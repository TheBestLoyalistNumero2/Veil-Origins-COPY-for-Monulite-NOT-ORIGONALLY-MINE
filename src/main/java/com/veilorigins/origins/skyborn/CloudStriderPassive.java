package com.veilorigins.origins.skyborn;

import com.veilorigins.api.OriginPassive;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class CloudStriderPassive extends OriginPassive {
    private int jumpCount = 0;
    private boolean wasOnGround = true;

    public CloudStriderPassive() {
        super("cloud_strider");
    }

    @Override
    public void onTick(Player player) {

        // Speed at high altitude (> Y=100)
        if (player.getY() > 100) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 0, false, false));
        }
    }

    @Override
    public void onEquip(Player player) {
    }

    @Override
    public void onRemove(Player player) {
        player.removeEffect(MobEffects.MOVEMENT_SPEED);
    }
}
