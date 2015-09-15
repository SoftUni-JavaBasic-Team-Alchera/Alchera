package com.alchera.game.Structure.Entities.Bonuses;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public abstract class Bonus {

    protected BonusType type;
    protected Sprite sprite;
    protected Vector2 position;
    protected float rotation;
    protected boolean isEffectOver;

    protected Bonus(Vector2 pos){
        this.position = pos;
        this.isEffectOver = false;
    }

    protected Bonus(float x, float y){
        this(new Vector2(x,y));
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }

    public void update(float delta){
        rotation += delta;
        sprite.setRotation((float) Math.cos(rotation));
    }

    public boolean isEffectOver(){
        return this.isEffectOver;
    }

    public BonusType getType() {
        return type;
    }

    public void setType(BonusType type) {
        this.type = type;
    }

    public Sprite getSprite(){
        return this.sprite;
    }
}
