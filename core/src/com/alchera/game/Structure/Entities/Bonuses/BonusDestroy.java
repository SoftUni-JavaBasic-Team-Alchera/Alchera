package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusDestroy extends Bonus {

    public BonusDestroy(Vector2 position) {
        super(position);
        // TODO: Add sprite
        this.setType(BonusType.DESTROY);
    }

    public void Activate(/*collection of enemies*/) {

        //destroy all enemies
    }
}
