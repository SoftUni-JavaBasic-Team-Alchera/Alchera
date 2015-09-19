package com.alchera.game.Structure.Components.Overlays;

import com.alchera.game.Structure.Components.UI.UIComponent;
import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

/**
 * Created by Inspix on 14/09/2015.
 */
public abstract class Overlay implements Disposable{

    protected Transition transition;
    protected ArrayList<UIComponent> components;
    protected OrthographicCamera camera;
    protected Matrix4 defaultCamera;
    protected SpriteBatch batch;
    protected boolean isVisible;

    protected Overlay(SceneManager sm) {
        this.isVisible = true;
        this.components = new ArrayList<UIComponent>();
        this.batch = sm.getApplication().getBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    public void render(){
        if (!isVisible)
            return;
        defaultCamera = batch.getProjectionMatrix();
        batch.begin();
        batch.setProjectionMatrix(this.camera.combined);
        for(UIComponent component : components){
            if (component.isVisible()){
                component.render(batch);
            }
        }
        batch.end();
        batch.setProjectionMatrix(defaultCamera);
    }

    public void update(float delta){
        for(UIComponent component : components){
            if (component.isVisible()){
                component.update(delta);
            }
        }
    }
    public void show(){
        this.isVisible = true;
    }
    public void hide(){
        this.isVisible = false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public OrthographicCamera getCamera(){
        return this.camera;
    }

    public void setTransition(Transition t){
        this.transition = t;
    }

    public Transition getTransition(){
        return this.transition;
    }

    public void addComponent(UIComponent component){
        this.components.add(component);
    }

    public ArrayList<UIComponent> getComponents(){
        return this.components;
    }

    public void dispose(){
        components.clear();
    }

}
