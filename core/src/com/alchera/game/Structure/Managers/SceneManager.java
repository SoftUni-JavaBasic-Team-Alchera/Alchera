package com.alchera.game.Structure.Managers;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.Scenes.GameplayScene;
import com.alchera.game.Structure.Components.Scenes.Scene;
import com.alchera.game.Structure.Components.Scenes.SplashScene;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Inspix on 09/09/2015.
 */
public class SceneManager {

    public enum SceneType{
        SPLASH,
        MAINMENU,
        GAMEPLAY,
        OPTIONS,
        CREDITS
    }

    private final Alchera application;
    private Queue<Scene> scenes;

    public SceneManager(final Alchera app){
        this.application = app;
        this.scenes = new PriorityQueue<Scene>();
        this.setScene(SceneType.SPLASH);
    }

    public void update(float delta){
        this.scenes.peek().update(delta);
    }

    public void render(){
        this.scenes.peek().render();
    }

    public void dispose(){
        for(Scene scene : scenes){
            scene.dispose();
        }
        scenes.clear();
    }

    public void setScene(SceneType type){
        if (!scenes.isEmpty())
            scenes.poll().dispose();
        scenes.add(getScene(type));
    }

    private Scene getScene(SceneType type){
        switch (type){
            case SPLASH:
                return new SplashScene(this);
            case MAINMENU:
                // TODO: Implement main menu scene
                return null;
            case GAMEPLAY:
                return new GameplayScene(this);
            case OPTIONS:
                // TODO: Implement options menu scene
                return null;
            case CREDITS:
                // TODO: Implement credits scene
                return null;

        }
        return null;
    }

    public Alchera getApplication(){
        return this.application;
    }

}
