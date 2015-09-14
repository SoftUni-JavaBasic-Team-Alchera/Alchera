package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;

/**
 * Created by Zdravko on 14.9.2015 ã..
 */
public class BonusLife extends Bonus {

    public BonusLife() {

        this.setType(BonusType.LIFE);
    }

    public void Activate(Player player) {

        player.setLives(player.getLives() + 1);
    }
}
