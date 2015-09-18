package com.alchera.game.Structure.Entities.Enemys;

import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Utils.AssetUtils;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.alchera.game.Structure.Utils.Variables.PPM;

/**
 * Created by Nedyalkov on 9/11/2015.
 */
public class Enemy{
    //Chase speed
    private final float accelerationSpeed = 10f;
    private final float maxSpeed = 2f;

    //body in box2d world
    private Body body;
    private float elapsedTime;
    private boolean isFlipped;

    //Enemy states and triggers
    private EnemyState enemyState;
    private Fixture chasingTrigger;
    private Fixture attackTrigger;
    private boolean isIdle;

    //Sprites and animations
    private Sprite idle;
    private TextureAtlas atlas;
    private Animation move;
    private Animation attack;
    private Animation currentAnimation;

    //Instance of main player
    private Player player;

    public Enemy(World world, Player player, String spritesPath, int locationX, int locationY, String startSpriteRegion,
                 String moveAnimation, int moveAnimationNumberOfFrames, String attackAnimation, int attackAnimationNumberOfFrames) {

        atlas = new TextureAtlas(Gdx.files.internal(spritesPath));
        TextureAtlas.AtlasRegion region = atlas.findRegion(startSpriteRegion);
        body = BodyFactory.CreateDynamicRectangle(world, region.getRegionWidth(), region.getRegionHeight(), locationX, locationY, 0.5f, 0);
        body.setUserData(this);
        isIdle = true;
        isFlipped = false;
        idle = atlas.createSprite(startSpriteRegion);

        //create animations
        createAnimations(moveAnimation,moveAnimationNumberOfFrames,attackAnimation,attackAnimationNumberOfFrames);

        //Trigegers set
        setChasing(region);
        setAttackTrigger(region);

        //Get instance of player
        this.player = player;

    }

    private void createAnimations(String moveAnimation, int moveAnimationNumberOfFrames,String attackAnimation, int attackAnimationNumberOfFrames) {
        move = AssetUtils.createFromAtlas(atlas, moveAnimation, moveAnimationNumberOfFrames, 1);
        move.setPlayMode(Animation.PlayMode.LOOP);
        attack = AssetUtils.createFromAtlas(atlas,attackAnimation,attackAnimationNumberOfFrames);
        attack.setPlayMode(Animation.PlayMode.LOOP);
        enemyState = EnemyState.chill;
    }

    private void setChasing(TextureAtlas.AtlasRegion region) {
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((region.getRegionWidth() + 300) / 2) / PPM, 10 / PPM, new Vector2((region.getRegionWidth() / 2) / PPM, 0), 0);
        fdef.shape = shape;
        fdef.isSensor = true;
        chasingTrigger = body.createFixture(fdef);
    }

    private void setAttackTrigger(TextureAtlas.AtlasRegion region) {
        FixtureDef fdefAttack = new FixtureDef();
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(((region.getRegionWidth()+90)/2)/PPM,4 / PPM, new Vector2((region.getRegionWidth()/2)/PPM,0),0);
        fdefAttack.shape = shape2;
        fdefAttack.isSensor=true;
        attackTrigger = body.createFixture(fdefAttack);
    }

    private void setEnemyState() {
        if (chasingTrigger.testPoint(player.getBoxWorldX(),player.getBoxWorldY()) &&
                !((attackTrigger.testPoint(player.getBoxWorldX(),player.getBoxWorldY())))){
            this.enemyState = EnemyState.chasing;

        }else if (attackTrigger.testPoint(player.getBoxWorldX(),player.getBoxWorldY())){
            this.enemyState = EnemyState.attack;

        } else {
            this.enemyState = EnemyState.chill;

        }
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
        setEnemyState();
        if (enemyState == EnemyState.chasing){
            isIdle = false;
            body.setLinearDamping(0);
            if (currentAnimation != move) {
                currentAnimation = move;
                elapsedTime = 0;
            }
            if (player.getBoxWorldX() > this.body.getPosition().x){
                isFlipped = false;
                if (velocity.x > -maxSpeed)
                    body.applyForceToCenter(accelerationSpeed * delta, 0, true);
            }
            else {
                isFlipped = true;
                if (velocity.x > -maxSpeed)
                    body.applyForceToCenter(-accelerationSpeed * delta, 0, true);
            }
        }else if (enemyState == EnemyState.attack){
            isIdle = false;
            body.setLinearDamping(2);
            if (currentAnimation != attack) {
                currentAnimation = attack;
                elapsedTime = 0;
            }
            if (player.getBoxWorldX() > this.body.getPosition().x){
                isFlipped = false;
            }
            else {
                isFlipped = true;
            }
        }else {
            body.setLinearDamping(10);
            isIdle = true;
            currentAnimation = null;
        }

    }
}


