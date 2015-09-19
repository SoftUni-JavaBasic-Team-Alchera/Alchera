package com.alchera.game.Structure.Entities.Traps;

import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Inspix on 18/09/2015.
 */
public abstract class BaseTrap {
    protected Body body;
    protected Fixture fixture;
    protected Sprite sprite;
    protected boolean isStatic;

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }

    public void update(float delta){
        if (!isStatic)
            sprite.setPosition(getX(),getY());
    }

    public Body getBody(){
        return this.body;
    }

    public Fixture getFixture(){
        return this.fixture;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public float getWorldX(){
        return this.body.getPosition().x;
    }

    public float getWorldY(){
        return this.body.getPosition().y ;
    }

    public float getX(){
        return this.body.getPosition().x * Variables.PPM;
    }

    public float getY(){
        return this.body.getPosition().y * Variables.PPM;
    }

    public void setX(float x){
        this.body.setTransform(x/Variables.PPM, getWorldY(), body.getAngle());
        this.sprite.setX(x);
    }

    public void setY(float y){
        this.body.setTransform(getWorldX(),y/Variables.PPM,body.getAngle());
        this.sprite.setY(y);
    }

    public void setPosition(float x, float y){
        this.body.setTransform(x/Variables.PPM, y/Variables.PPM, body.getAngle());
        this.sprite.setPosition(x,y);
    }

    public void setStatic(boolean s){
        this.isStatic = s;
    }

    public boolean isStatic(){
        return this.isStatic;
    }
}
