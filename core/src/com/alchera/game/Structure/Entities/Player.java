package com.alchera.game.Structure.Entities;

import com.alchera.game.Structure.Managers.SoundManager;
import com.alchera.game.Structure.Utils.AssetUtils;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.alchera.game.Structure.Utils.Variables;
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
    private final float jumpForce = 22f;
    private float jumpMultiplier = 1;
    private float speedBoost = 0;
    private Body body;
    private Fixture groundTrigger;
    private int lives;
    private int health = 3;
    //private float mana;
    private float elapsedTime;

    private boolean isIdle;
    private boolean isGrounded;
    private boolean isFlipped;
    private boolean isDying;
    private boolean isFinished;
    private boolean dead;
    private boolean[] keys;

    private Sprite idle;
    private TextureAtlas atlas;
    private Animation run;
    private Animation jump;
    private Animation death;
    private Animation currentAnimation;
    private SoundManager soundManager;
    public Player(World world){
        this(world,10,10);
    }

    public Player(World world, float x, float y){
        atlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
        idle = atlas.createSprite("run0");
        body = this.reCreate(world, x, y);

        isIdle = true;
        run = AssetUtils.createFromAtlas(atlas, "run", 5, 1);
        run.setPlayMode(Animation.PlayMode.LOOP);
        death = AssetUtils.createFromAtlas(atlas,"death",5);
        death.setPlayMode(Animation.PlayMode.NORMAL);
        death.setFrameDuration(1f / 10f);
        jump = AssetUtils.createFromAtlas(atlas,"jump",5);
        soundManager = SoundManager.getInstance();
    }

    public Body reCreate(World world,float x, float y) {
        body = BodyFactory.CreateDynamicRectangle(world,idle.getRegionWidth(),idle.getRegionHeight()-10,x,y,0.25f,0);
        body.setUserData(this);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((idle.getRegionWidth()-2)/2)/PPM,4 / PPM, new Vector2((idle.getRegionWidth()/2)/PPM,0),0);
        fdef.shape = shape;
        fdef.isSensor = true;
        isDying = false;
        dead = false;

        groundTrigger = body.createFixture(fdef);

        shape.dispose();

        keys = new boolean[3];

        return body;
    }

    public void render(SpriteBatch batch){
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (dead)
            return;
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
        if (dead){
            this.body.setType(BodyDef.BodyType.StaticBody);
            return;
        }
        if (isDying) {
            isIdle = false;
            if (currentAnimation != death) {
                currentAnimation = death;
                elapsedTime = 0;
            }
            if (currentAnimation.isAnimationFinished(elapsedTime)){
                dead = true;
            }
            return;
        }
        Vector2 velocity = body.getLinearVelocity();

        // Handle jumping with flipping of the animation. Can jump only if there's a contact with the groundTrigger.
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            if (isGrounded){
                body.applyForceToCenter(0, jumpForce * jumpMultiplier, true);
                soundManager.playSound(Variables.Sounds.JUMP);
                isGrounded = false;
            }
            if (currentAnimation != jump) {
                currentAnimation = jump;
                elapsedTime = 0;
            }
            isIdle = false;
        }

        // Move left if not attacking. Interpolates movement speed if moving in the opposite direction to change direction fast.
        // Handles if the animation should be flipped.
        if (Gdx.input.isKeyPressed(Keys.A)) {
            if (velocity.x > 0)
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.3f), velocity.y);
            isFlipped = true;
            if (currentAnimation != run && isGrounded) {
                currentAnimation = run;
                elapsedTime = 0;
            }
            isIdle = false;
            if (velocity.x > -(maxSpeed + speedBoost))
                body.applyForceToCenter(-accelerationSpeed * delta, 0, true);

        }
        // Move left if not attacking. Interpolates movement speed if moving in the opposite direction to change direction fast.
        // Handles if the animation should be flipped.
        else if (Gdx.input.isKeyPressed(Keys.D)) {
            if (velocity.x < 0)
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.3f), velocity.y);
            isFlipped = false;

            if (currentAnimation != run && isGrounded) {
                currentAnimation = run;
                elapsedTime = 0;
            }
            isIdle = false;
            if (velocity.x < maxSpeed + speedBoost)
                body.applyForceToCenter(accelerationSpeed * delta, 0, true);
        }
        // If neither A or D is pressed, slowly stop the character, and reset values.
        else {
            if(isGrounded)
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.3f), velocity.y);
            else
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.1f), velocity.y);
            if (isGrounded && velocity.x < 0.3) {
                elapsedTime = 0;
                isIdle = true;
            }
        }
    }


    public void setKey(Lock.Type key){
        switch (key){

            case YELLOW:
                this.keys[0] = true;
                break;
            case BLUE:
                this.keys[1] = true;
                break;
            case GREEN:
                this.keys[2] = true;
                break;
        }
    }

    public boolean hasKey(Lock.Type key){
        switch (key){
            case YELLOW: return this.keys[0];
            case BLUE:   return this.keys[1];
            case GREEN:  return this.keys[2];
        }
        return false;
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

    public void setBonusSpeed(float speed){
        this.speedBoost = speed;
    }

    public float getBonusSpeed(){
        return this.speedBoost;
    }

    public void setJumpMultiplier(float mult){
        this.jumpMultiplier = mult;
    }

    public float getJumpMultiplier(){
        return this.jumpMultiplier;
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }


    public void setDying(boolean dying) {
        this.isDying = dying;
    }

    public boolean isFinished(){
        return this.isFinished;
    }

    public void setFinished(boolean finished){
        this.isFinished = finished;
    }

    public boolean isDying(){
        return this.isDying;
    }

    public boolean isDead(){
        return this.dead;
    }
}
