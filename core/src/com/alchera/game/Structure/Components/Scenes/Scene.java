package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Inspix on 10/09/2015.
 */
public abstract class Scene {

    protected final Alchera application;
    protected SceneManager manager;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected Scene(final SceneManager sm){
        this.manager = sm;
        this.application = sm.getApplication();
        this.batch = application.getBatch();
        this.camera = application.getCamera();
        this.create();
    }

    protected abstract void create();
    public abstract void render();
    public abstract void update(float delta);
    public abstract void dispose();

}
