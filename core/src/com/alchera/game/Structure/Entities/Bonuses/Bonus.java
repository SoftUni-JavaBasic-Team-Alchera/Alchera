package com.alchera.game.Structure.Entities.Bonuses;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public abstract class Bonus {
    protected final float defaultSize = 30f;
    protected final float rotationMultiplier = 10f;

    protected BonusType type;
    protected Body body;
    protected Sprite sprite;
    protected Vector2 position;
    protected float rotation;
    protected boolean isEffectOver;
    protected boolean isActivated;

    protected Bonus(Vector2 pos){
        this.position = pos;
        this.isEffectOver = false;
    }

    protected Bonus(float x, float y){
        this(new Vector2(x, y));
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }

    public void update(float delta){
        rotation += delta;
        sprite.setRotation((float) Math.cos(rotation) * rotationMultiplier);
    }

    public abstract void activate(Object obj);

    protected void effect(float delta){

    }

    public boolean isEffectOver(){
        return this.isEffectOver;
    }

    public boolean isActivated(){
        return this.isActivated;
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

    public Body getBody(){
        return this.body;
    }

    public void setBody(Body body){
        this.body = body;
    }
}
