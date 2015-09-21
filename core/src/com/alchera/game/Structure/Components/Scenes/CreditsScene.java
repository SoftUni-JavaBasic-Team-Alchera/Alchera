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

public class CreditsScene extends Scene {
    private BitmapFont titleFont;
    private BitmapFont font;

    private final String title = "CREDITS:";
    private String alchera = "Team ALCHERA";
    private String[] teamNames;
    private Texture weasel;
    private float alpha;
    private boolean transitionExit;
    private Vector3 camPositionIn;
    private Vector3 camPositionOut;

    private ShaderProgram shader;

    public CreditsScene(SceneManager sm) {
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

        teamNames = new String[]{
                "DESIGN and PRODUCTION",
                "Ylian Rusev",
                "Vasil Nedyalkov",
                "Boyan Blagiev",
                "Galya Georgieva",
                "Zdravko Botushanov"
        };
        weasel = new Texture("sprites/weaselCredits.png");
        camPositionIn = camera.position.cpy();
        camera.position.set(-Alchera.WIDTH/2f,camera.position.y,camera.position.z);
        camPositionOut = camera.position.cpy();

        shader = batch.getShader();

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        shader.setUniformf("fade", alpha);
        //draw weasel
        batch.draw(weasel, 800, 0);
        titleFont.draw(batch, alchera, 400, 600);
        //draw title
        titleFont.draw(batch, title, 400, 500);
        //draw credits info
        for (int i = 0; i < teamNames.length; i++) {
            if (0 == i) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(batch, teamNames[i],420,400-50*i);
        }

        batch.end();

    }

    @Override
    public void update(float delta) {
        alpha = MathUtils.clamp(alpha + delta,0,1);
        if (!transitionExit){
            float x = MathUtils.lerp(camera.position.x,camPositionIn.x,0.2f);
            this.camera.position.set(x,camera.position.y,camera.position.z);
        }else{
            float x = MathUtils.lerp(camera.position.x,camPositionOut.x,0.2f);
            this.camera.position.set(x,camera.position.y,camera.position.z);
            if (camPositionOut.epsilonEquals(camera.position, 0.01f)){
                camera.position.set(camPositionIn);
                manager.setScene(SceneManager.SceneType.MAINMENU);
            }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            transitionExit = true;
            SoundManager.getInstance().playSound(Variables.Sounds.MENUBACK);
        }
    }

}
