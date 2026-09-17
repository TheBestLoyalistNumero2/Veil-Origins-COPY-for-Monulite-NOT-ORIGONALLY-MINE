package com.veilorigins.origins.skyborn;

import com.veilorigins.api.OriginPassive;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class SkybornWeaknessesPassive extends OriginPassive {
    private int tickCounter = 0;

    public SkybornWeaknessesPassive() {
        super("skyborn_weaknesses");
    }

    @Override
    public void onTick(Player player) {
        tickCounter++;

        if (tickCounter % 20 == 0) {
            double y = player.getY();

            // Slowness underground < 40
            if (y < 12) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false));
                // Mining Fatigure in caves (implied by "in caves", approximated by y < 40 and
                // low light?)
                // Spec says "Mining Fatigue I in caves". Let's stick to Y level + not seeing
                // sky check?
                if (!player.level().canSeeSky(player.blockPosition())) {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 0, false, false));
                }
            }
        }
    }

    @Override
    public void onEquip(Player player) {
    }

    @Override
    public void onRemove(Player player) {
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        player.removeEffect(MobEffects.DIG_SLOWDOWN);
    }
}
