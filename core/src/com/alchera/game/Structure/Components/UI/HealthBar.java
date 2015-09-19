package com.alchera.game.Structure.Components.UI;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Inspix on 15/09/2015.
 */
public class HealthBar extends BaseUIComponent {

    private Player player;
    private Sprite heart;
    private float scale;

    public HealthBar(){
        this.heart = new Sprite(new Texture(Gdx.files.internal("sprites/bonusheart.png")));
        this.isVisible = true;
        this.scale = 0.5f;
        this.heart.setScale(scale,scale);
        this.heart.setPosition(5, Alchera.HEIGHT - heart.getHeight() - 5);

    }

    public void setPlayer(Player player){
        this.player = player;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (int i = 0; i < player.getHealth(); i++) {
            heart.draw(batch);
            heart.setX(heart.getX() + heart.getWidth() * scale + 5);
        }
        heart.setX(5);
    }

    @Override
    public void update(float delta) {
        // Can be used for smooth transition between visible and not or other logic, in this case it's not needed
    }

    @Override
    public void setAlpha(float value) {
        heart.setAlpha(MathUtils.clamp(value,0,1));
    }

    @Override
    public float getAlpha() {
        return this.heart.getColor().a;
    }

    public void setPosition(Vector2 pos) {
        this.position = pos;
        this.heart.setPosition(pos.x,pos.y);
    }
}
