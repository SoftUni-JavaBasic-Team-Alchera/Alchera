package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Lock;
import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Inspix on 19/09/2015.
 */
public class BonusKey extends Bonus {

    private Lock.Type type;

    public BonusKey(Lock.Type type, float x, float y){
        this(type,new Vector2(x, y));
    }

    public BonusKey(Lock.Type type,Vector2 position) {
        super(position);
        this.type = type;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/key.png")));
        switch (type){
            case GREEN:
                sprite.setColor(Color.GREEN);
                break;
            case BLUE:
                sprite.setColor(Color.BLUE);
                break;
            case YELLOW:
                sprite.setColor(Color.YELLOW);
                break;
        }
        this.sprite.setSize(defaultSize,defaultSize);
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
        player.setKey(this.type);
        this.isActivated = true;
    }

    public Lock.Type getKeyType() {
        return type;
    }
}
