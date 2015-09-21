package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.Camera.CustomCamera;
import com.alchera.game.Structure.Components.Overlays.Hud;
import com.alchera.game.Structure.Entities.Bonuses.Bonus;
import com.alchera.game.Structure.Entities.Bonuses.BonusHealth;
import com.alchera.game.Structure.Entities.Enemys.Enemy;
import com.alchera.game.Structure.Entities.Lock;
import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Entities.Traps.*;
import com.alchera.game.Structure.Levels.Level;
import com.alchera.game.Structure.Listeners.ContactHandler;
import com.alchera.game.Structure.Managers.SceneManager;
import static com.alchera.game.Structure.Utils.Variables.*;

import com.alchera.game.Structure.Managers.SoundManager;
import com.alchera.game.Structure.Utils.EnemyFactory;
import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Inspix on 10/09/2015.
 */
public class GameplaySceneTest extends Scene {

    BitmapFont font;

    FrameBuffer frameBufferA;
    FrameBuffer frameBufferB;
    ShaderProgram blur;
    ShaderProgram defaultShader;
    ContactHandler contactHandler;
    CustomCamera camera;
    CustomCamera b2dcamera;
    Box2DDebugRenderer boxRenderer;
    Player player;
    LinkedList<Bonus> bonuses;
    LinkedList<Lock> locks;
    ArrayList<BaseTrap> traps;
    ArrayList<Enemy> enemies;
    SoundManager soundManager;

    Hud hud;
    World boxWorld;
    Level level;

    float fade, blurRadius;
    boolean isBlurred;
    boolean grayscale = false;
    final float scale = 0.75f;
    private boolean debug,debugInfo;
    private boolean camLocked;
    private boolean transitionIn;
    private boolean transitionOut;
    private float elapsedTime;
    private boolean exitSpawned;


    public GameplaySceneTest(SceneManager sm){
        super(sm);
    }
    @Override
    protected void create(){
        fade = 0f;
        blurRadius = 0f;
        isBlurred = false;
        camLocked = true;
        soundManager = SoundManager.getInstance();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/bodoni.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        font = generator.generateFont(parameter);

        enemies = new ArrayList<Enemy>();
        reLoadLevel();

        blur = new ShaderProgram(Gdx.files.internal("shaders/basic.vert"),Gdx.files.internal("shaders/blur.frag"));
        if (!blur.isCompiled()) {
            System.err.println(blur.getLog());
        }

        blur.begin();
        blur.setUniformf("fade", fade);
        blur.setUniformf("dir", 0f, 0f);
        blur.setUniformf("radius", blurRadius);
        blur.setUniformf("resolution", Alchera.WIDTH);
        blur.end();

        defaultShader = batch.getShader();
        defaultShader.begin();
        defaultShader.setUniformf("fade", 1);
        defaultShader.end();
        batch.setShader(blur);

        frameBufferA = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),false,false);
        frameBufferB = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),false,false);


        // Camera for the game world
        camera = new CustomCamera(player);
        camera.setToOrtho(false, Alchera.WIDTH, Alchera.HEIGHT);
        camera.zoom = scale;
        camera.setLimited(camLocked);
        camera.setMinPosition(new Vector2(Alchera.WIDTH * scale / 2f, Alchera.HEIGHT * scale / 2f));
        camera.setMaxPosition(new Vector2(level.size.x - (Alchera.WIDTH * scale / 2f), level.size.y - (Alchera.HEIGHT * scale / 2f)));
        camera.setPosition(new Vector3(player.getWorldX(), player.getWorldY(), 0));

        b2dcamera = new CustomCamera(player);
        b2dcamera.setToOrtho(false, Alchera.WIDTH / PPM, Alchera.HEIGHT / PPM);
        b2dcamera.zoom = scale;
        b2dcamera.isBox2DCamera(true);
        b2dcamera.setLimited(camLocked);
        b2dcamera.setMinPosition(new Vector2((Alchera.WIDTH * scale / 2f) / PPM, (Alchera.HEIGHT * scale / 2f) / PPM));
        b2dcamera.setMaxPosition(new Vector2((level.size.x - (Alchera.WIDTH * scale / 2f)) / PPM, (level.size.y - (Alchera.HEIGHT * scale / 2f)) / PPM));
        b2dcamera.setPosition(new Vector3(player.getWorldX() / PPM, player.getWorldY() / PPM, 0));
        // Debug renderer to see a representation of what happens in the Box2D world.
        boxRenderer = new Box2DDebugRenderer();
        soundManager.playSongLooping(Variables.Songs.GAMEPLAY,0);

    }

    @Override
    public void render() {
        if (isBlurred){
            renderBlur();
        }else{
            renderDefault();
        }
        if (debugInfo)
            renderDebugInfo();
    }

    private void renderDefault() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        level.renderBG();
        batch.begin();
        // Draw the player.
        player.render(batch);
        for (Bonus bonus : bonuses){
            bonus.render(batch);
        }
        for (BaseTrap trap : traps){
            trap.render(batch);
        }
        for (Enemy enemy : enemies){
            enemy.render(batch);
        }
        // Draw ends here.
        batch.end();
        level.render(camera);

        batch.begin();
        for(Lock lock : locks){
            lock.render(batch);
        }
        batch.end();

        hud.render();
        // Draw the Box2D world.
        if (debug){
            boxRenderer.render(boxWorld, b2dcamera.combined);
        }
    }

    private void renderBlur() {
        frameBufferA.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        level.renderBG();
        batch.begin();
        // Draw the player.
        player.render(batch);
        for (Bonus bonus : bonuses){
            bonus.render(batch);
        }
        for (BaseTrap trap : traps){
            trap.render(batch);
        }
        for (Enemy enemy : enemies){
            enemy.render(batch);
        }
        // Draw ends here.
        batch.end();
        level.render(camera);

        batch.begin();
        for(Lock lock : locks){
            lock.render(batch);
        }
        batch.end();

        hud.render();
        frameBufferA.end();


        frameBufferB.begin();
        batch.begin();
        blur.setUniformf("dir", 1, 0);
        batch.draw(frameBufferA.getColorBufferTexture(), 0, 0);
        batch.end();
        frameBufferB.end();

        batch.begin();
        blur.setUniformf("dir", 0, 1);
        batch.draw(frameBufferB.getColorBufferTexture(), 0, 0);
        batch.end();

        if (debug){
            boxRenderer.render(boxWorld, b2dcamera.combined);
        }
    }

    private void renderDebugInfo(){
        batch.setShader(defaultShader);
        batch.begin();

        batch.setProjectionMatrix(hud.getCamera().combined);
        font.draw(batch, "Debug info", Alchera.WIDTH - 220, Alchera.HEIGHT - 50);
        font.draw(batch, "UP: Fade in", Alchera.WIDTH - 220, Alchera.HEIGHT - 70);
        font.draw(batch,"DOWN: Fade out",Alchera.WIDTH - 220, Alchera.HEIGHT - 90);
        font.draw(batch,"Fade Amount: " + fade,Alchera.WIDTH - 220, Alchera.HEIGHT - 110);
        font.draw(batch,"INSERT: Toggle Grayscale - " + grayscale,Alchera.WIDTH - 220, Alchera.HEIGHT - 130);
        font.draw(batch, "B: Enable blurring - Currently:" + isBlurred, Alchera.WIDTH - 220, Alchera.HEIGHT - 150);
        font.draw(batch, "X: Decrease blur", Alchera.WIDTH - 220, Alchera.HEIGHT - 170);
        font.draw(batch, "C: Increase blur", Alchera.WIDTH - 220, Alchera.HEIGHT - 190);
        font.draw(batch, "Blur amount: " + blurRadius, Alchera.WIDTH - 220, Alchera.HEIGHT - 210);
        font.draw(batch, "R: Reset Level: ", Alchera.WIDTH - 220, Alchera.HEIGHT - 250);
        font.draw(batch, "HOME: Debug renderer: " + debug, Alchera.WIDTH - 220, Alchera.HEIGHT - 270);
        font.draw(batch, "END: Lock camera: " + camLocked, Alchera.WIDTH - 220, Alchera.HEIGHT - 290);
        batch.end();
        batch.setShader(blur);

    }


    ArrayList<Bonus> toRemove = new ArrayList<Bonus>(10);

    @Override
    public void update(float delta) {

        if (updateTransitions(delta)){
            return;
        }

        updateElements(delta);

        // Move box2d world physics.
        boxWorld.step(1 / 60f, 8, 3);
        // Update player logic
        player.update(delta);

        if (player.getHealth() <= 0 || player.isFinished()){
            transitionOut = true;
        }else if (player.getHealth() > 0 && player.isDead()){
            reLoadLevel();
            return;
        }

        // update both camera positions
        camera.update();
        b2dcamera.update();
        batch.setProjectionMatrix(this.camera.combined);


        handleInput(delta);

    }

    private boolean updateTransitions(float delta){
        elapsedTime += delta;
        if (!transitionIn && elapsedTime > 1f){
            fade = MathUtils.clamp(fade + delta/4,0,1);
            blur.begin();
            blur.setUniformf("fade", fade);
            blur.end();
            soundManager.changeSongVolume(delta);
            if (fade >= 1){
                transitionIn = true;
                hud.getTimer().restart();
            }
        }

        if (transitionOut){
            camera.update();
            b2dcamera.update();
            batch.setProjectionMatrix(this.camera.combined);
            fade = MathUtils.clamp(fade - delta/2,0,1);
            blur.begin();
            blur.setUniformf("fade", fade);
            blur.end();
            soundManager.setSongVolume(fade);
            if (fade <= 0){
                if (player.getHealth() <= 0){
                    manager.setScene(SceneManager.SceneType.GAMEOVER);
                }else if (player.isFinished()){
                    manager.setScene(SceneManager.SceneType.YOUWIN);
                }
            }

            return true;
        }
        return false;
    }

    private void updateElements(float delta){
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

        for (BaseTrap trap : traps){
            trap.update(delta);
        }
        for(Bonus bonus : toRemove){
            bonuses.remove(bonus);
        }
        int unlocked = 0;
        for(Lock lock : locks){
            lock.update(delta);
            if (!lock.isLocked())
                unlocked++;
        }
        if (unlocked == locks.size()){
            if (!exitSpawned){
                level.spawnExit();
                exitSpawned = true;
                soundManager.playSound(Variables.Sounds.SUCCESS);
            }
        }
        toRemove.clear();

        for (Enemy enemy : enemies){
            enemy.update(delta);
        }
        hud.update(delta);
    }

    private void handleInput(float delta){
        shaderTests(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            if (hud.isVisible())
                hud.hide();
            else
                hud.show();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            reLoadLevel();
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.HOME)){
            debug = !debug;
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            debugInfo = !debugInfo;
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.END)){
            camLocked = !camLocked;
            camera.setLimited(camLocked);
            b2dcamera.setLimited(camLocked);
        }
    }


    private void shaderTests(float delta){
        // Only for testing shaders atm.
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            fade = MathUtils.clamp(fade - delta,0,1);
            batch.getShader().begin();
            batch.getShader().setUniformf("fade",fade);
            batch.getShader().end();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            fade = MathUtils.clamp(fade + delta,0,1);
            batch.getShader().begin();
            batch.getShader().setUniformf("fade", fade);
            batch.getShader().end();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.INSERT)){
            grayscale = !grayscale;
            batch.getShader().begin();
            batch.getShader().setUniformf("u_grayscale", grayscale ? 1 : 0);
            batch.getShader().end();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)){
            blurRadius = MathUtils.clamp(blurRadius - delta * 5,0,10);
            blur.begin();
            blur.setUniformf("radius", blurRadius);
            blur.end();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.C)){
            blurRadius = MathUtils.clamp(blurRadius + delta* 5,0,10);
            blur.begin();
            blur.setUniformf("radius", blurRadius);
            blur.end();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
            isBlurred = !isBlurred;
            blur.begin();
            blur.setUniformf("u_blur",isBlurred ? 1 : 0);
            blur.end();
        }
        // Testing shaders end.
    }

    private void reLoadLevel(){
        if (boxWorld != null)
            boxWorld.dispose();
        exitSpawned = false;
        boxWorld = new World(new Vector2(0,-18),false);
        level = new Level(batch,boxWorld);
        traps = level.getTraps();
        bonuses = level.getBonuses();
        locks = level.getLocks();
        if (player == null){
            player = new Player(boxWorld,level.playerSpawn.x,level.playerSpawn.y);
        }else{
            player.reCreate(boxWorld, level.playerSpawn.x, level.playerSpawn.y);
        }

        player.setJumpMultiplier(1);
        player.setBonusSpeed(0);

        if (hud == null) {
            hud = new Hud(manager,player);
        }
        else{
            hud.getBonusField().clearBonuses();
            hud.getHealthBar().setPlayer(player);
        }

        enemies.clear();
        for (Vector2 pos : level.getEnemyCoords()){
            try {
                enemies.add(EnemyFactory.createRandomEnemy(player, boxWorld, (int) pos.x, (int) pos.y));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (contactHandler == null) {
            contactHandler = new ContactHandler();
        }
        contactHandler.setPlayer(player);
        boxWorld.setContactListener(contactHandler);
    }

    @Override
    public void dispose() {
        player.dispose();
        boxRenderer.dispose();
    }
}
