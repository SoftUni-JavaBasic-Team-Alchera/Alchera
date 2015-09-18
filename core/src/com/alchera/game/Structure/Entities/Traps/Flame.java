package com.alchera.game.Structure.Entities.Traps;

import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Nedyalkov on 18/09/2015.
 */
public class Flame extends BaseTrap {

    private float rotation;

    public Flame(World world,float x,float y) {
        this.sprite = new Sprite(new Texture(Gdx.files.internal("sprites/flame.png")));
        this.sprite.setPosition(x,y);
        this.sprite.setOriginCenter();
        this.isStatic = true;
        this.body = BodyFactory.createStaticCircle(world,sprite.getWidth()/2f,x,y);
        this.fixture = body.getFixtureList().get(0);
        this.fixture.setUserData(this);
        this.rotation = 100f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        sprite.rotate(delta * rotation);
    }
}
