package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusJump extends Bonus {

    public BonusJump(Vector2 position) {
        super(position);
        // TODO: Add sprite
        this.setType(BonusType.JUMP);
    }

    public void Activate(Player player) {

        //player jump height ++....
    }
}
