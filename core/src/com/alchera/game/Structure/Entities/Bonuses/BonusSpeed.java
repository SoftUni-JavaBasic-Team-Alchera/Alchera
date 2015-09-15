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

    private Player player;
    private final float bonusSpeed = 3f;
    private float duration = 3f;
    public BonusSpeed(float x, float y){
        this(new Vector2(x,y));
    }

    @Override
    public void activate(Object obj) {
        player = (Player)obj;
        player.setBonusSpeed(player.getBonusSpeed() + bonusSpeed);
        this.isActivated = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (isActivated())
            this.effect(delta);
    }

    @Override
    protected void effect(float delta) {
        duration -= delta;
        if (duration <= 0){
            this.isEffectOver = true;
            player.setBonusSpeed(player.getBonusSpeed() - bonusSpeed);
        }
    }

    public BonusSpeed(Vector2 position) {
        super(position);
        this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/bonusspeed.png")));
        this.sprite.setPosition(
                this.position.x-(defaultSize/2)* sprite.getScaleX(),
                this.position.y-(defaultSize/2)* sprite.getScaleY());
        this.sprite.setSize(defaultSize, defaultSize);
        this.sprite.setOriginCenter();
        this.setType(BonusType.SPEED);
    }
}
