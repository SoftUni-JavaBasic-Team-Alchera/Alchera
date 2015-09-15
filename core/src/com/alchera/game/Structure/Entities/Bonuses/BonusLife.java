package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusLife extends Bonus {

    public BonusLife(Vector2 position) {
        super(position);
        // TODO: Add sprite
        this.setType(BonusType.LIFE);
    }

    public void Activate(Player player) {

        player.setLives(player.getLives() + 1);
    }
}
