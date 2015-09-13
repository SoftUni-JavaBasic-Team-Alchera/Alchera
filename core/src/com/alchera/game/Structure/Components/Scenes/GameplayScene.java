package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.Camera.CustomCamera;
import com.alchera.game.Structure.Entities.Enemys.Enemy;
import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Levels.Level;
import com.alchera.game.Structure.Listeners.ContactHandler;
import com.alchera.game.Structure.Managers.SceneManager;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

import static com.alchera.game.Structure.Utils.Variables.PPM;

/**
 * Created by Inspix on 10/09/2015.
 */
public class GameplayScene extends Scene {

    ContactHandler contactHandler;
    CustomCamera camera;
    CustomCamera b2dcamera;
    Box2DDebugRenderer boxRenderer;
    Player player;
    ArrayList<Enemy> enemies;
    Enemy enemy;
    World boxWorld;
    Level level;

    public GameplayScene(SceneManager sm){
        super(sm);
    }
    @Override
    protected void create(){
        // Create a box2d world to simulate all physics
        enemies = new ArrayList<Enemy>();



        boxWorld = new World(new Vector2(0, -18), true);
        level = new Level(batch,boxWorld);
        player = new Player(boxWorld,level.playerSpawn.x,level.playerSpawn.y);
        for(Vector2 vec : level.getEnemyCoordinates()){
            enemies.add(new Enemy(boxWorld,player,"sprites/enemy.txt",(int)vec.x,(int)vec.y,"move0","move",4,"attack",2));
        }
        contactHandler = new ContactHandler(player);
        boxWorld.setContactListener(contactHandler);
        // Camera for the game world
        camera = new CustomCamera(player);
        camera.setToOrtho(false, Alchera.WIDTH, Alchera.HEIGHT);
        b2dcamera = new CustomCamera(player);
        b2dcamera.setToOrtho(false, Alchera.WIDTH / PPM, Alchera.HEIGHT / PPM);
        b2dcamera.isBox2DCamera(true);
        // Debug renderer to see a representation of what happens in the Box2D world.
        boxRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render() {

        // Clear the color buffer.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw begins here.
        level.render(camera);
        batch.begin();
        // Draw the player.
        player.render(batch);
        for (Enemy enemy : enemies){
            enemy.render(batch);
        }
        // Draw ends here.
        batch.end();



        // Draw the Box2D world.
        boxRenderer.render(boxWorld, b2dcamera.combined);
    }

    @Override
    public void update(float delta) {
        // Move box2d world physics.
        boxWorld.step(delta, 8, 2);
        // Update player logic
        player.update(delta);
        for (Enemy enemy : enemies){
            enemy.update(delta);
        }

        // update both camera positions
        camera.update();
        b2dcamera.update();
        batch.setProjectionMatrix(this.camera.combined);
    }

    @Override
    public void dispose() {
        player.dispose();
        boxRenderer.dispose();
    }
}
