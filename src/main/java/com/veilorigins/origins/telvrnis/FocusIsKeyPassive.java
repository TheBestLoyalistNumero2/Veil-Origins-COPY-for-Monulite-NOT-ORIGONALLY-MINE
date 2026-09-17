package com.veilorigins.origins.telvrnis;

import com.veilorigins.api.OriginPassive;
import com.veilorigins.data.OriginData;
import net.minecraft.world.entity.player.Player;

public class FocusIsKeyPassive extends OriginPassive {

    public FocusIsKeyPassive() { super("focus_is_key"); };

    @Override
    public void onTick(Player player) {
        OriginData.PlayerOriginData originData = player.getData(OriginData.PLAYER_ORIGIN);
        if (player.getHealth() < 10) {
            if (originData.getResourceBar() > 50) {
                originData.setResourceBar(50);
            }
        } else if (player.getHealth() > 18) {
            originData.addResource(0.01f);
        }
    }
}
