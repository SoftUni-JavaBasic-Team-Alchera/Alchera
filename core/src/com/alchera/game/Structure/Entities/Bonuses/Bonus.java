package com.alchera.game.Structure.Entities.Bonuses;

/**
 * Created by Zdravko on 14.9.2015 ã..
 */
public abstract class Bonus {

    private BonusType type;

    public BonusType getType() {
        return type;
    }

    public void setType(BonusType type) {
        this.type = type;
    }
}
