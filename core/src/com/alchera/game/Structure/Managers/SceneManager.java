package com.alchera.game.Structure.Managers;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.Scenes.*;

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
        CREDITS
    }

    private final Alchera application;
    private Queue<Scene> scenes;

    public SceneManager(final Alchera app){
        this.application = app;
        this.scenes = new PriorityQueue<Scene>();
        this.setScene(SceneType.GAMEPLAY);
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
                return new MainMenu(this);
            case GAMEPLAY:
                return new GameplaySceneTest(this);
            case CREDITS:
                return new CreditsScene(this);

        }
        return null;
    }

    public Alchera getApplication(){
        return this.application;
    }

}
