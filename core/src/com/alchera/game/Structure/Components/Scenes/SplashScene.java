package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Managers.SceneManager;
import com.alchera.game.Structure.Managers.SoundManager;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Inspix on 10/09/2015.
 */
public class SplashScene extends Scene {

    private BitmapFont font;
    private GlyphLayout layoutAlchera;
    private GlyphLayout layoutPresents;
    private final String presents = "presents";
    private int counter;
    private float elapsedTime;
    private float counterTime;
    private float alpha;
    private float blur;
    private ShaderProgram shader;

    public SplashScene(SceneManager sm) {
        super(sm);
    }

    @Override
    protected void create() {
        font = new BitmapFont(Gdx.files.getFileHandle("fonts/test.fnt", Files.FileType.Internal));
        layoutAlchera = new GlyphLayout(font,"Alchera");
        layoutPresents = new GlyphLayout(font,"");
        alpha = 0;
        shader = batch.getShader();
        SoundManager.getInstance().playSongLooping("intro");
        shader.begin();
        shader.setUniformf("fade", alpha);
        shader.end();
    }

    @Override
    public void render() {
        // glClear is a must! should be in each Scene implementation
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        shader.setUniformf("fade", MathUtils.clamp(alpha,0,1));
        font.getData().setScale(1, 1);
        font.draw(batch,layoutAlchera,Alchera.WIDTH/2 - layoutAlchera.width/2,Alchera.HEIGHT/2 + layoutAlchera.height/2);
        if (elapsedTime > 5f){
            font.getData().setScale(0.5f, 0.5f);
            font.draw(batch, layoutPresents, Alchera.WIDTH / 2 - (layoutPresents.width / 2.0f), Alchera.HEIGHT / 2 - layoutPresents.height);
        }
        batch.end();
    }

    @Override
    public void update(float delta) {
        if (elapsedTime < 5f){
            alpha += delta * 0.5f;

        }
        else if(elapsedTime > 1.5f){
            counterTime += delta;
            if (counterTime >= 0.15f && counter <=7) {
                layoutPresents.setText(font,presents.substring(0,++counter));
                counterTime -= 0.15f;
            }else if (alpha > 0f){
                alpha -= delta / 2;
                if (alpha <= 0){
                    manager.setScene(SceneManager.SceneType.MAINMENU);
                }
            }
        }
        elapsedTime += delta;

    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
