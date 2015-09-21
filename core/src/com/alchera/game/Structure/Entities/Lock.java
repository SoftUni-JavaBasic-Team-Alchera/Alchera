package com.alchera.game.Structure.Entities;

import com.alchera.game.Structure.Managers.SoundManager;
import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Inspix on 19/09/2015.
 */
public class Lock {



    public enum Type{
        YELLOW,
        BLUE,
        GREEN
    }
    private Type type;
    private Sprite sprite;
    private boolean toBeRemoved;
    private boolean isLocked;
    private float alpha;

    public Lock(Type type,float x, float y){
        this.alpha = 1;
        this.type = type;
        this.isLocked = true;
        this.toBeRemoved = false;
        switch (type){
            case YELLOW:
                this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/lock_yellow.png")));
                break;
            case BLUE:
                this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/lock_blue.png")));
                break;
            case GREEN:
                this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/lock_green.png")));
                break;
        }

        this.sprite.setPosition(x,y);
    }

    public void render(SpriteBatch batch){
        if (toBeRemoved)
            return;
        sprite.draw(batch);
    }

    public void update(float delta){
        if (toBeRemoved)
            return;
        if (!isLocked){
            alpha = MathUtils.clamp(alpha -= delta,0,1);
            sprite.setAlpha(alpha);
            if (alpha <= 0) {
                this.toBeRemoved = true;
                SoundManager.getInstance().playSound(Variables.Sounds.OPENDOOR);
            }
        }
    }

    public boolean Unlock(Player player){
        if (player.hasKey(this.type)){
            isLocked = false;
            return true;
        }
        return false;
    }

    public boolean isLocked(){ return this.isLocked; }

    public boolean toBeRemoved(){
        return this.toBeRemoved;
    }
}
