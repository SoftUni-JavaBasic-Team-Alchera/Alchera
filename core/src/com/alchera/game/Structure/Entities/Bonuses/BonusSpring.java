package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusSpring extends Bonus {

    private float impulse;

    public BonusSpring(float x, float y){
        this(new Vector2(x, y));
    }

    public BonusSpring(Vector2 position) {
        super(position);
        this.impulse = 2f;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/boxExplosive.png")));
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOriginCenter();
        this.setType(BonusType.SPRING);
    }

    @Override
    public void activate(Object obj) {
        Player player = (Player)obj;
        player.getBody().applyLinearImpulse(0, impulse, player.getBody().getLocalCenter().x, player.getBody().getLocalCenter().y,true);
        this.isActivated = true;
        this.isEffectOver = true;
    }

    @Override
    public void update(float delta) {
    }

    public void setImpulse(float amount){
        this.impulse = amount;
    }
}
