package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusAttack extends Bonus {

    public BonusAttack(float x, float y){
        this(new Vector2(x,y));
    }

    @Override
    public void activate(Object obj) {
        Player player = (Player)obj;
        //increase player's attack
    }

    public BonusAttack(Vector2 position) {
        super(position);
        // TODO: Add sprite
        this.setType(BonusType.ATTACK);
    }
}
