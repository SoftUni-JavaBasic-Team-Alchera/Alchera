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

    private final float accelerationSpeed = 10f; // Does not mean it instantly goes to 10f, it does it gradually
    private final float maxSpeed = 4f;
    private final float jumpForce = 20f;

    private Body body;
    private Fixture groundTrigger;
    //private int lifes;
    //private float health;
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

        atlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
        TextureAtlas.AtlasRegion region = atlas.findRegion("run0");
        body = BodyFactory.CreateDynamicRectangle(world,region.getRegionWidth(),region.getRegionHeight(),10,10,0.2f,0);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((region.getRegionWidth()/2)/PPM,2 / PPM, new Vector2((region.getRegionWidth()/2)/PPM,0),0);
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
            batch.draw(idle, body.getPosition().x * PPM, body.getPosition().y * PPM);
        }
        else{
            batch.draw(currentAnimation.getKeyFrame(elapsedTime),body.getPosition().x * PPM,body.getPosition().y * PPM);
        }
    }

    public void update(float delta) {
        Vector2 velocity = body.getLinearVelocity();

        // Flip idle sprite
        if (isFlipped && !idle.isFlipX()) {
            idle.flip(true, false);
        } else if (!isFlipped && idle.isFlipX()) {
            idle.flip(true, false);

        }

        // Handle jumping with flipping of the animation. Can jump only if theres a contact with the groundTrigger.
        if (Gdx.input.isKeyPressed(Keys.SPACE) && !isAttacking) {
            if (isGrounded)
                body.applyForceToCenter(0, jumpForce, true);
            isGrounded = false;
            if (currentAnimation != jump) {

                if (isFlipped && !jump.getKeyFrame(delta).isFlipX()) {
                    AssetUtils.flipAnimation(jump, true, false);
                } else if (!isFlipped && jump.getKeyFrame(delta).isFlipX()) {
                    AssetUtils.flipAnimation(jump, true, false);
                }
                currentAnimation = jump;
                elapsedTime = 0;
            }
            isIdle = false;
        }

        // Handle punching with flipping of the animation. Can't do anything while punching animation is going.
        if (Gdx.input.isKeyJustPressed(Keys.E)) {
            if (currentAnimation != punch) {
                if (isFlipped && !punch.getKeyFrame(delta).isFlipX()) {
                    AssetUtils.flipAnimation(punch, true, false);
                } else if (!isFlipped && punch.getKeyFrame(delta).isFlipX()) {
                    AssetUtils.flipAnimation(punch, true, false);
                }
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

        // If the player is in the air, skip all other logic. Cant move in the air atm.
        if (!isGrounded)
            return;


        // Move left if not attacking. Interpolates movement speed if moving in the opposite direction to change direction fast.
        // Handles if the animation should be flipped.
        if (Gdx.input.isKeyPressed(Keys.A) && !isAttacking) {
            if (velocity.x > 0)
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.1f), velocity.y);
            if (!isFlipped) {
                AssetUtils.flipAnimation(run, true, false);
                isFlipped = true;
            }
            if (currentAnimation != run) {
                currentAnimation = run;
                elapsedTime = 0;
            }
            isIdle = false;
            if (velocity.x > -maxSpeed)
                body.applyForceToCenter(-accelerationSpeed * delta, 0, true);

        }
        // Move left if not attacking. Interpolates movement speed if moving in the opposite direction to change direction fast.
        // Handles if the animation should be flipped.
        else if (Gdx.input.isKeyPressed(Keys.D) && !isAttacking) {
            if (velocity.x < 0)
                body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.1f), velocity.y);
            if (isFlipped) {
                AssetUtils.flipAnimation(run, true, false);
                isFlipped = false;
            }
            if (currentAnimation != run) {
                currentAnimation = run;
                elapsedTime = 0;
            }
            isIdle = false;
            if (velocity.x < maxSpeed)
                body.applyForceToCenter(accelerationSpeed * delta, 0, true);
        }
        // If neither A or D is pressed, slowly stop the character, and reset values.
        else {
            body.setLinearVelocity(MathUtils.lerp(velocity.x, 0, 0.2f), velocity.y);

            if (!isAttacking && isGrounded && velocity.x < 0.3) {
                elapsedTime = 0;
                isIdle = true;
                currentAnimation = null;
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

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
