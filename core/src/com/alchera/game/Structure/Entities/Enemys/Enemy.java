package com.alchera.game.Structure.Entities.Enemys;

import com.alchera.game.Structure.Utils.AssetUtils;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.alchera.game.Structure.Utils.Variables.PPM;

/**
 * Created by Administrator on 9/11/2015.
 */
public class Enemy {
    private final float accelerationSpeed = 20f;

    private Body body;
    private float elapsedTime;
    private boolean isFlipped;
    private String direction;

    private EnemyState enemyState;

    private Fixture chasingTrigger;
    private Fixture attackTrigger;

    private boolean isIdle;

    private Sprite idle;
    private TextureAtlas atlas;
    private Animation move;
    private Animation attack;

    private Animation currentAnimation;

    public Enemy(World world) {
        atlas = new TextureAtlas(Gdx.files.internal("sprites/enemy.txt"));
        TextureAtlas.AtlasRegion region = atlas.findRegion("move0");
        body = BodyFactory.CreateDynamicRectangle(world, region.getRegionWidth(), region.getRegionHeight(), 500, 0, 0.5f, 0);
        body.setUserData(this);
        isIdle = true;
        isFlipped = false;
        idle = atlas.createSprite("move0");
        move = AssetUtils.createFromAtlas(atlas, "move", 4, 1);
        move.setPlayMode(Animation.PlayMode.LOOP);
        attack = AssetUtils.createFromAtlas(atlas,"attack",2);
        attack.setPlayMode(Animation.PlayMode.LOOP);
        enemyState = EnemyState.chill;

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((region.getRegionWidth() + 300) / 2) / PPM, 10 / PPM, new Vector2((region.getRegionWidth() / 2) / PPM, 0), 0);
        fdef.shape = shape;
        fdef.isSensor = true;
        chasingTrigger = body.createFixture(fdef);

        FixtureDef fdefAttack = new FixtureDef();
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(((region.getRegionWidth()+20)/2)/PPM,4 / PPM, new Vector2((region.getRegionWidth()/2)/PPM,0),0);
        fdefAttack.shape = shape2;
        fdefAttack.isSensor=true;
        attackTrigger = body.createFixture(fdefAttack);

    }
    public void render(SpriteBatch batch){
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (isIdle)
        {
            batch.draw(idle, body.getPosition().x * PPM, body.getPosition().y * PPM,idle.getOriginX(),idle.getOriginY(),idle.getRegionWidth(),idle.getRegionHeight(), isFlipped ? -1 : 1,1,0);
        }
        else{
            TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime);
            batch.draw(currentFrame, body.getPosition().x * PPM, body.getPosition().y * PPM, currentFrame.getRegionWidth() / 2, 0f, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), isFlipped ? -1f : 1f, 1f, 0);
        }
    }

    public void update(float delta) {
        Vector2 velocity = body.getLinearVelocity();

        if (enemyState == EnemyState.chasing) {
            if (direction == "Left"){
                isFlipped = true;
                body.applyForceToCenter(-accelerationSpeed * delta, 0, true);
            }
            else if(direction == "Right") {
                isFlipped = false;
                body.applyForceToCenter(accelerationSpeed * delta, 0, true);
            }
            if (currentAnimation != move ) {
                currentAnimation = move;
                elapsedTime = 0;
            }
            isIdle = false;
        }else if (enemyState == EnemyState.attack){
            if (direction == "Left"){
                isFlipped = true;
            }
            else if(direction == "Right") {
                isFlipped = false;
            }
            if (currentAnimation != attack ) {
                currentAnimation = attack;
                elapsedTime = 0;
            }
        }else {
            body.setLinearDamping(10);
            isIdle = true;
            currentAnimation = null;
        }

    }




    public Fixture getChasingTrigger() {
        return chasingTrigger;
    }
    public Fixture getAttackTrigger() {
        return attackTrigger;
    }
    public void setEnemyState(EnemyState enemyState) {
        this.enemyState = enemyState;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

}
