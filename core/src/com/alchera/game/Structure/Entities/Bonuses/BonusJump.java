package com.alchera.game.Structure.Entities.Bonuses;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Zdravko on 14.9.2015 ..
 */
public class BonusJump extends Bonus {

    private Player player;
    private final float bonusMultiplier = 0.3f;
    private float duration = 3f;

    public BonusJump(float x, float y){
        this(new Vector2(x,y));
    }

    public BonusJump(Vector2 position) {
        super(position);
        this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/bonusjump.png")));
        this.sprite.setPosition(
                this.position.x-(defaultSize/2)* sprite.getScaleX(),
                this.position.y-(defaultSize/2)* sprite.getScaleY());
        this.sprite.setSize(defaultSize, defaultSize);
        this.sprite.setOriginCenter();
        this.setType(BonusType.SPEED);
    }

    @Override
    public void activate(Object obj) {
        player = (Player)obj;
        player.setJumpMultiplier(player.getJumpMultiplier() + bonusMultiplier);
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
            player.setJumpMultiplier(player.getJumpMultiplier() - bonusMultiplier);
        }
    }
}
