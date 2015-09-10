package com.alchera.game;

import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Alchera extends ApplicationAdapter {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
    SceneManager sceneManager;

	@Override
	public void create () {
        // Camera for the game world
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Alchera.WIDTH,Alchera.HEIGHT);
        // Set the default background color.
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        sceneManager = new SceneManager(this);
    }

	@Override
	public void render () {
        sceneManager.update(Gdx.graphics.getDeltaTime());
        sceneManager.render();
	}

	public SpriteBatch getBatch(){
		return this.batch;
	}

	public OrthographicCamera getCamera(){
		return this.camera;
	}

	@Override
	public void dispose() {
        sceneManager.dispose();
        super.dispose();
		batch.dispose();
	}
}
