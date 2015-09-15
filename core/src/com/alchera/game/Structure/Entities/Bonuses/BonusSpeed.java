package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusSpeed extends Bonus {

    public BonusSpeed(Vector2 position) {
        super(position);
        this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/speed.png")));
        this.sprite.setSize(30,30);

        this.setType(BonusType.SPEED);
    }

    public void Activate(Player player) {

        //increase speed
    }
}
