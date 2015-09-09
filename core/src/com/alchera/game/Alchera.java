package com.alchera.game;

import static com.alchera.game.Structure.Utils.Variables.*;

import com.alchera.game.Structure.Components.Camera.CustomCamera;
import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Listeners.ContactHandler;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Alchera extends ApplicationAdapter {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	SpriteBatch batch;
	ContactHandler contactHandler;
	CustomCamera camera;
	CustomCamera b2dcamera;
	Box2DDebugRenderer boxRenderer;
	Player player;
	World boxWorld;


	@Override
	public void create () {

		// Set the default background color.
		Gdx.gl.glClearColor(0, 0, 0f, 1);

		// Create a box2d world to simulate all physics
		boxWorld = new World(new Vector2(0, -18), true);

		player = new Player(boxWorld);

		contactHandler = new ContactHandler(player);
		boxWorld.setContactListener(contactHandler);

		// Camera for the game world
		camera = new CustomCamera(player);
		camera.setToOrtho(false, WIDTH, HEIGHT);

		b2dcamera = new CustomCamera(player);
		b2dcamera.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM);
		b2dcamera.isBox2DCamera(true);
		// Debug renderer to see a representation of what happens in the Box2D world.
		boxRenderer = new Box2DDebugRenderer();

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		// Create a platforms to simulate ground. Uses Pixels per meter conversion internally.
		BodyFactory.CreateStaticRectangle(boxWorld, WIDTH, 10, 0, 0, 1, 1);
		BodyFactory.CreateStaticRectangle(boxWorld, 50, 10, 50, 100, 1, 1);
		BodyFactory.CreateStaticRectangle(boxWorld, 50, 10, 0, 200, 1, 1);
		BodyFactory.CreateStaticRectangle(boxWorld, 50, 10, 150, 300, 1, 1);
		BodyFactory.CreateStaticRectangle(boxWorld, 100, 10, 250, 200, 1, 1);
	}

	@Override
	public void render () {
		// Always update the world before drawing it.
		update();

		// Clear the color buffer.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Draw begins here.
		batch.begin();
		// Draw the player.
		player.render(batch);
		// Draw ends here.
		batch.end();

		// Draw the Box2D world.
		boxRenderer.render(boxWorld, b2dcamera.combined);
	}

	private void update() {
		float delta = Gdx.graphics.getDeltaTime();
		// Move box2d world physics.
		boxWorld.step(delta, 8, 2);
		// Update player logic
		player.update(delta);

		// update both camera positions
		camera.update();
		b2dcamera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void dispose() {
		super.dispose();
		player.dispose();
		batch.dispose();
		boxWorld.dispose();
		boxRenderer.dispose();
	}
}
