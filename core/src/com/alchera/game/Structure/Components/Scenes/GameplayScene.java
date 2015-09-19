package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.Camera.CustomCamera;
import com.alchera.game.Structure.Components.Overlays.Hud;
import com.alchera.game.Structure.Entities.Bonuses.Bonus;
import com.alchera.game.Structure.Entities.Bonuses.BonusHealth;
import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Levels.Level;
import com.alchera.game.Structure.Listeners.ContactHandler;
import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Inspix on 10/09/2015.
 */
public class GameplayScene extends Scene {

    ContactHandler contactHandler;
    CustomCamera camera;
    //CustomCamera b2dcamera;
    //Box2DDebugRenderer boxRenderer;
    Player player;
    LinkedList<Bonus> bonuses;
    Hud hud;
    World boxWorld;
    Level level;
    final float scale = 0.75f;


    public GameplayScene(SceneManager sm){
        super(sm);
    }
    @Override
    protected void create(){
        boxWorld = new World(new Vector2(0, -18), true);

        level = new Level(batch,boxWorld);
        bonuses = level.getBonuses();
        player = new Player(boxWorld,level.playerSpawn.x,level.playerSpawn.y);
        hud = new Hud(manager,player);
        contactHandler = new ContactHandler();
        contactHandler.setPlayer(player);
        boxWorld.setContactListener(contactHandler);
        // Camera for the game world
        camera = new CustomCamera(player);
        camera.setToOrtho(false, Alchera.WIDTH, Alchera.HEIGHT);
        camera.zoom = scale;
        camera.setMinPosition(new Vector2(Alchera.WIDTH * scale / 2f,  Alchera.HEIGHT * scale / 2f));
        camera.setMaxPosition(new Vector2(level.size.x - (Alchera.WIDTH * scale/2f),level.size.y - (Alchera.HEIGHT * scale /2f)));
        camera.setPosition(new Vector3(player.getWorldX(), player.getWorldY(), 0));

        Gdx.app.log("Shader:", batch.getShader().getFragmentShaderSource());
/*
        b2dcamera = new CustomCamera(player);
        b2dcamera.setToOrtho(false, Alchera.WIDTH / PPM, Alchera.HEIGHT / PPM);
        b2dcamera.zoom = scale;
        b2dcamera.isBox2DCamera(true);
        // Debug renderer to see a representation of what happens in the Box2D world.
        boxRenderer = new Box2DDebugRenderer();*/
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        level.render(camera);
        batch.begin();
        // Draw the player.
        player.render(batch);
        for (Bonus bonus : bonuses){
            bonus.render(batch);
        }
        hud.render();
        // Draw ends here.
        batch.end();
        // Draw the Box2D world.
        //boxRenderer.render(boxWorld, b2dcamera.combined);
    }
    ArrayList<Bonus> toRemove = new ArrayList<Bonus>(10);

    @Override
    public void update(float delta) {
        // Move box2d world physics.
        boxWorld.step(delta, 8, 2);
        // Update player logic
        player.update(delta);
        for (Bonus bonus : bonuses){
            if (bonus.isActivated()){
                if (!(bonus instanceof BonusHealth))
                    hud.getBonusField().addBonus(bonus);
                boxWorld.destroyBody(bonus.getBody());
                toRemove.add(bonus);
                continue;
            }

            bonus.update(delta);
        }

        for(Bonus bonus : toRemove){
            bonuses.remove(bonus);
        }
        toRemove.clear();

        // update both camera positions
        camera.update();
        //b2dcamera.update();
        batch.setProjectionMatrix(this.camera.combined);
        hud.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            if (hud.isVisible())
                hud.hide();
            else
                hud.show();
        }
    }

    @Override
    public void dispose() {
        player.dispose();
        //boxRenderer.dispose();
    }
}
