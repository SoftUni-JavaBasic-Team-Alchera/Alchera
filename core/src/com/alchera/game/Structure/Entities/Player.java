package com.alchera.game.Structure.Entities;

import com.alchera.game.Structure.Utils.AssetUtils;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

import static com.alchera.game.Structure.Utils.Variables.*;

public class Player implements Disposable{

    private final float accelerationSpeed = 20f; // Does not mean it instantly goes to 10f, it does it gradually
    private final float maxSpeed = 4f;
    private final float jumpForce = 20f;

    private Body body;
    private Fixture groundTrigger;
    private int lives;
    private int health = 3;
    //private float mana;
    private float elapsedTime;
    private boolean isIdle;
    private boolean isGrounded;


    private boolean isFlipped;
    private boolean isAttacking;

    private Sprite idle;
    private TextureAtlas atlas;
    private Animation run;
    private Animation punch;
    private Animation jump;
    //private Animation spell;
    private Animation currentAnimation;

    public Player(World world){
        this(world,10,10);
    }

    public Player(World world, float x, float y){
        atlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
        TextureAtlas.AtlasRegion region = atlas.findRegion("run0");
        body = BodyFactory.CreateDynamicRectangle(world,region.getRegionWidth(),region.getRegionHeight(),x,y,0.2f,0);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((region.getRegionWidth())/2)/PPM,8 / PPM, new Vector2((region.getRegionWidth()/2)/PPM,0),0);
        fdef.shape = shape;
        fdef.isSensor = true;

        groundTrigger = body.createFixture(fdef);

        shape.dispose();

        isIdle = true;
        idle = atlas.createSprite("run0");
        run = AssetUtils.createFromAtlas(atlas, "run", 5, 1);
        run.setPlayMode(Animation.PlayMode.LOOP);
        punch = AssetUtils.createFromAtlas(atlas,"punch",7);
        punch.setPlayMode(Animation.PlayMode.NORMAL);
        jump = AssetUtils.createFromAtlas(atlas,"jump",5);

        //spell = AssetUtils.createFromAtlas(atlas,"spell",11);
        //spell.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void render(SpriteBatch batch){
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (isIdle)
        {
            batch.draw(idle, body.getPosition().x * PPM, body.getPosition().y * PPM,idle.getOriginX(),idle.getOriginY(),idle.getRegionWidth(),idle.getRegionHeight(), isFlipped ? -1 : 1,1,0);
        }
        else{
            TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime);
            batch.draw(currentFrame,body.getPosition().x * PPM,body.getPosition().y * PPM,currentFrame.getRegionWidth()/2,0f,currentFrame.getRegionWidth(),currentFrame.getRegionHeight(),isFlipped ? -1f:1f,1f,0);
        }
    }

    public void update(float delta) {
        Vector2 velocity = body.getLinearVelocity();

        // Handle jumping with flipping of the animation. Can jump only if there's a contact with the groundTrigger.
        if (Gdx.input.isKeyJustPressed(Keys.SPACE) && isGrounded) {
            body.applyForceToCenter(0, jumpForce, true);
            isGrounded = false;
            if (currentAnimation != jump && !isAttacking) {
                currentAnimation = jump;
                elapsedTime = 0;
            }
            isIdle = false;
        }

        // Handle punching with flipping of the animation. Can't do anything while punching animation is going.
        if (Gdx.input.isKeyJustPressed(Keys.E)) {
            if (currentAnimation != punch) {
                currentAnimation = punch;
                elapsedTime = 0;
                isIdle = false;
                isAttacking = true;
            }
        }

        // Check if the punching animation is finished, to reset the trigger.
        if (currentAnimation != null && currentAnimation == punch && currentAnimation.isAnimationFinished(elapsedTime)) {
            isAttacking = false;
        }

        // Move left if not attacking. Interpolates movement speed if moving in the opposite direction to change direction fast.
        // Handles if the animation should be flipped.
        if (Gdx.input.isKeyPressed(Keys.A) && !isAttacking) {
            if (velocity.x > 0)
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.3f), velocity.y);
            isFlipped = true;
            if (currentAnimation != run && isGrounded) {
                currentAnimation = run;
                elapsedTime = 0;
            }
            isIdle = false;
            if (velocity.x > -maxSpeed)
                body.applyForceToCenter(-accelerationSpeed * delta, 0, true);

        }
        // Move left if not attacking. Interpolates movement speed if moving in the opposite direction to change direction fast.
        // Handles if the animation should be flipped.
        else if (Gdx.input.isKeyPressed(Keys.D)&& !isAttacking) {
            if (velocity.x < 0)
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.3f), velocity.y);
            isFlipped = false;

            if (currentAnimation != run && isGrounded) {
                currentAnimation = run;
                elapsedTime = 0;
            }
            isIdle = false;
            if (velocity.x < maxSpeed)
                body.applyForceToCenter(accelerationSpeed * delta, 0, true);
        }
        // If neither A or D is pressed, slowly stop the character, and reset values.
        else {
            if(isGrounded)
             body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.3f), velocity.y);
            else
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.1f), velocity.y);
            if (!isAttacking && isGrounded && velocity.x < 0.3) {
                elapsedTime = 0;
                isIdle = true;
            }
        }
    }

    public Fixture getGroundTrigger(){
        return this.groundTrigger;
    }

    public Body getBody(){
        return this.body;
    }

    public boolean isGrounded(){
        return isGrounded;
    }

    public void setGrounded(boolean grounded){
        this.isGrounded = grounded;
    }

    public float getWorldX(){
        return this.body.getPosition().x * PPM;
    }

    public float getWorldY(){
        return this.body.getPosition().y * PPM;
    }

    public float getBoxWorldX(){
        return this.body.getPosition().x;
    }

    public float getBoxWorldY(){
        return this.body.getPosition().y;
    }

    public int getLives() { return lives; }

    public void setLives(int lives) { this.lives = lives; }

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public void setPosition(float x,float y){
        this.body.getPosition().set(x/PPM,y/PPM);
    }

    public Vector2 getBoxWorldPosition(){
        return this.body.getPosition();
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }


}
