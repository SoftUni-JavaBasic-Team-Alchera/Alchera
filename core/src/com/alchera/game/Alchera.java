package com.alchera.game;

import com.alchera.game.Structure.Managers.SceneManager;
import com.alchera.game.Structure.Managers.SoundManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Alchera extends ApplicationAdapter {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
    SceneManager sceneManager;
	SoundManager soundManager;

	@Override
	public void create () {

		soundManager = SoundManager.getInstance();

		// Camera for the game world
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Alchera.WIDTH,Alchera.HEIGHT);
		// Set the default background color.
		Gdx.gl.glClearColor(0, 0, 0f, 1);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		ShaderProgram shader = new ShaderProgram(Gdx.files.internal("shaders/basic.vert"),Gdx.files.internal("shaders/basic.frag"));
		if (!shader.isCompiled())
			System.err.println(shader.getLog());
		shader.begin();
		shader.setUniformf("fade",1f);
		shader.end();
		batch.setShader(shader);

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
