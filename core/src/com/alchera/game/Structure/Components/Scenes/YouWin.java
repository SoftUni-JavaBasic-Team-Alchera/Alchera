package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Managers.SceneManager;
import com.alchera.game.Structure.Managers.SoundManager;
import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Nedyalkov on 9/13/2015.
 */
public class YouWin extends Scene{

    private BitmapFont titleFont;
    private BitmapFont font;

    private final String title = "YOU WIN";
    private String[] menuItems;
    private Texture weasel;

    private int currentItem;
    private Vector3 camPositionIn;
    private Vector3 camPositionOut;
    private ShaderProgram shader;
    private float alpha;
    private boolean transitionExit;
    private float volumeFade;
    private SoundManager soundManager;

    public YouWin(SceneManager sm) {
        super(sm);

    }

    @Override
    protected void create() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FFSolid.ttf"));
        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GrandNord.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 5;
        parameter.borderColor = Color.BLACK;
        parameter.size = 56;

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.borderWidth = 5;
        parameter2.borderColor = Color.BLACK;
        parameter2.size = 36;

        titleFont = generator.generateFont(parameter);
        titleFont.setColor(Color.WHITE);

        font = generator2.generateFont(parameter2);
        font.setColor(Color.WHITE);

        menuItems= new String[]{
                "RESTART",
                "CREDITS",
                "EXIT"
        };

        weasel = new Texture("sprites/youwin.png");

        camPositionIn = camera.position.cpy();
        camera.position.set(-Alchera.WIDTH / 2f, -Alchera.HEIGHT / 2, camera.position.z);
        camPositionOut = camera.position.cpy();

        shader = batch.getShader();

        soundManager = SoundManager.getInstance();
        soundManager.playSongLooping(Variables.Songs.INTRO);

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        shader.setUniformf("fade", alpha);
        //draw weasel
        batch.draw(weasel,700,0);
        //draw title
        titleFont.draw(batch,title,400,500);
        //draw menu
        for (int i = 0; i < menuItems.length; i++) {
            if (currentItem == i) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(batch,menuItems[i],420,400-50*i);
        }

        batch.end();
    }

    @Override
    public void update(float delta) {
        alpha = MathUtils.clamp(alpha + delta, 0, 1);
        if (!transitionExit){
            float x = MathUtils.lerp(camera.position.x,camPositionIn.x,0.1f);
            float y = MathUtils.lerp(camera.position.y,camPositionIn.y,0.1f);
            this.camera.position.set(x,y,camera.position.z);
        }else{
            float x = MathUtils.lerp(camera.position.x,camPositionOut.x,0.1f);
            float y = MathUtils.lerp(camera.position.y,camPositionOut.y,0.1f);
            volumeFade = MathUtils.lerp(volumeFade,0,0.1f);
            if (currentItem == 0)
                soundManager.setSongVolume(volumeFade);
            this.camera.position.set(x,y,camera.position.z);
            if (camPositionOut.epsilonEquals(camera.position, 0.01f)){
                camera.position.set(camPositionIn);
                changeGameState();
            }
            camera.update();
            return;
        }
        camera.update();
        handleInput();
    }

    @Override
    public void dispose() {
        font.dispose();
        titleFont.dispose();
    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            if (currentItem>0) {
                currentItem--;
                soundManager.playSound(Variables.Sounds.MENUCHANGE);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            if (currentItem<2){
                currentItem++;
                soundManager.playSound(Variables.Sounds.MENUCHANGE);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            transitionExit = true;
            soundManager.playSound(Variables.Sounds.MENUSELECT);
        }
    }

    private void changeGameState(){
        if (currentItem == 0){
            manager.setScene(SceneManager.SceneType.GAMEPLAY);
        }if (currentItem == 1){
            manager.setScene(SceneManager.SceneType.CREDITS);
        }if (currentItem ==2){
            Gdx.app.exit();
        }
    }
}
