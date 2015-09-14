package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;

/**
 * Created by Zdravko on 14.9.2015 ã..
 */
public class BonusAttack extends Bonus {

    public BonusAttack() {

        this.setType(BonusType.ATTACK);
    }

    public void Activate(Player player) {

        //increase player's attack
    }
}
