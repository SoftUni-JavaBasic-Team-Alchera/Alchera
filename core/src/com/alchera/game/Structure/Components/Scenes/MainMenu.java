package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.Camera.CustomCamera;
import com.alchera.game.Structure.Managers.SceneManager;
import com.alchera.game.Structure.Managers.SoundManager;
import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Administrator on 9/13/2015.
 */
public class MainMenu extends Scene{

    private BitmapFont titleFont;
    private BitmapFont font;

    private final String title = "Blow The Weasel";
    private String[] menuItems;
    private Texture weasel;
    private ShaderProgram shader;
    private OrthographicCamera localCamera;
    private SoundManager soundManager;
    private float rotation;
    private float alpha;
    private float elapsedTime;
    private boolean transitionFinished;
    private int nextScene;

    private int currentItem;

    public MainMenu(SceneManager sm) {
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
                "PLAY",
                "CREDITS",
                "EXIT"
        };
        weasel = new Texture("sprites/weasel.png");
        nextScene = -1;

        localCamera = new OrthographicCamera();
        localCamera.setToOrtho(false, Alchera.WIDTH,Alchera.HEIGHT);
        localCamera.zoom = 9f;
        localCamera.update();

        shader = batch.getShader();

        soundManager = SoundManager.getInstance();

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(localCamera.combined);

        batch.begin();
        shader.setUniformf("fade",alpha);
        //draw weasel
        batch.draw(weasel,800,0);
        //draw title
        titleFont.draw(batch,title,400,500);
        //draw menu
        for (int i = 0; i < menuItems.length; i++) {
            if (currentItem == i) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(batch,menuItems[i],650,400-50*i,(float)Math.cos(elapsedTime)*100,1,false);
        }

        batch.end();
    }

    @Override
    public void update(float delta) {
        elapsedTime += delta;

        if (!transitionFinished){
            alpha = MathUtils.clamp((alpha += delta/4),0,1);
            rotation += delta*550;
            localCamera.rotate(delta*550);
            localCamera.zoom -= delta*4;
            if (localCamera.zoom <= 1.2f){
                localCamera.zoom = 1.2f;
                float mod = rotation % 180;
                alpha = MathUtils.clamp((alpha += delta),0,1);
                if (mod <= 7){
                    rotation += delta*35;
                    localCamera.rotate(delta*100);
                }else{
                    alpha = 1;
                    transitionFinished = true;
                }
            }
            localCamera.update();
        }

        if (nextScene != -1){
            alpha = MathUtils.clamp(alpha - delta,0,1);
            if (nextScene == 0)
                soundManager.changeSongVolume(-delta);
            if (alpha <= 0){
                batch.setProjectionMatrix(camera.combined);
                changeGameState();
            }
        }
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
            nextScene = currentItem;
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
