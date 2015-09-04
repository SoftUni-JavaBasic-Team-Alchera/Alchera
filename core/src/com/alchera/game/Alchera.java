package com.alchera.game;

import static com.alchera.game.Structure.Utils.Variables.*;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import static com.badlogic.gdx.Input.*;

public class Alchera extends ApplicationAdapter {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	SpriteBatch batch;
	Sprite img;
	OrthographicCamera camera;
	OrthographicCamera b2dCamera;
	Box2DDebugRenderer boxRenderer;
	Body playerBody;
	Body floor;
	World boxWorld;


	@Override
	public void create () {
		// Set the default background color.
		Gdx.gl.glClearColor(0, 0, 0f, 1);

		// Camera for the game world
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		// Camera for the Box2D world divided by the Pixels per meter.
		b2dCamera = new OrthographicCamera();
		b2dCamera.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM);

		// Create a box2d world to simulate all physics
		boxWorld = new World(new Vector2(0, -10), true);

		// Debug renderer to see a representation of what happens in the Box2D world.
		boxRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		// Create a image to use for the player.
		img = new Sprite(new Texture("badlogic.jpg"));

		// Create a "Player" box2d object in our box2d world. Uses Pixels per meter conversion internally. Fixed rotation
		playerBody = BodyFactory.CreateDynamicRectangle(boxWorld, img.getWidth(), img.getHeight(), 400, 400, 0.1f, 0);
		// Create a floor to simulate ground. Uses Pixels per meter conversion internally.
		floor = BodyFactory.CreateStaticRectangle(boxWorld, WIDTH, 10, 0, 0, 1, 1);
	}

	@Override
	public void render () {
		// Always update the world before drawing it.
		update();

		// Clear the color buffer.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Draw begins here.
		batch.begin();
		// Draw the player getting the position from the Box2D world and converting it back to pixel position.
		batch.draw(img,playerBody.getPosition().x * PPM,playerBody.getPosition().y * PPM);
		// Draw ends here.
		batch.end();


		// Draw the Box2D world.
		boxRenderer.render(boxWorld, b2dCamera.combined);
	}

	private void update() {
		// Move box2d world physics.
		boxWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
		// Handle input
		input();
	}


	private void input(){
		// Get reference to the current velocity of the player
		Vector2 velocity = playerBody.getLinearVelocity();

		// Handle movement to the left and right else stop slowly.
		if (Gdx.input.isKeyPressed(Keys.A)){
			if (velocity.x > 0)
				playerBody.setLinearVelocity(MathUtils.lerp(velocity.x,0,0.1f),velocity.y);

			playerBody.applyForceToCenter(-1,0,true);
		}else if (Gdx.input.isKeyPressed(Keys.D)){
			if (velocity.x < 0)
				playerBody.setLinearVelocity(MathUtils.lerp(velocity.x,0,0.1f),velocity.y);

			playerBody.applyForceToCenter(1,0,true);
		}else {
			playerBody.setLinearVelocity(MathUtils.lerp(velocity.x,0,0.1f),velocity.y);
		}

		// Jump handle, infinite jumping possible atm.
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			playerBody.applyForceToCenter(0,100,true);
		}
	}
}
