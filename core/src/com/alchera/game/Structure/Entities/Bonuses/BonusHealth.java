package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusHealth extends Bonus {

    public BonusHealth(float x, float y){
        this(new Vector2(x, y));
    }

    public BonusHealth(Vector2 position) {
        super(position);
        this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/bonusheart.png")));
        this.sprite.setPosition(
                this.position.x-(defaultSize/2)* sprite.getScaleX(),
                this.position.y-(defaultSize/2)* sprite.getScaleY());
        this.sprite.setSize(defaultSize, defaultSize);
        this.sprite.setOriginCenter();
        this.setType(BonusType.HEALTH);
    }

    @Override
    public void activate(Object obj) {
        Player player = (Player)obj;
        player.setHealth(player.getHealth() + 1);
        this.isActivated = true;
    }
}
