package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;

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
    private Sprite blackPixel;

    public SplashScene(SceneManager sm) {
        super(sm);
    }

    @Override
    protected void create() {
        Pixmap pixel = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixel.setColor(1, 1, 1, 1);
        pixel.fill();
        batch.enableBlending();
        blackPixel = new Sprite(new Texture(pixel));
        font = new BitmapFont(Gdx.files.getFileHandle("fonts/test.fnt", Files.FileType.Internal));
        layoutAlchera = new GlyphLayout(font,"Alchera");
        layoutPresents = new GlyphLayout(font,"");
        Gdx.app.log("Layoyt:", String.valueOf(layoutAlchera.width));

        alpha = 1f;
        blackPixel.setSize(Alchera.WIDTH, Alchera.HEIGHT);
        blackPixel.setPosition(0,0);
    }

    @Override
    public void render() {
        // glClear is a must! should be in each Scene implementation
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.getData().setScale(1, 1);
        font.draw(batch,layoutAlchera,Alchera.WIDTH/2 - layoutAlchera.width/2,Alchera.HEIGHT/2 + layoutAlchera.height/2);
        if (elapsedTime > 5f){
            font.getData().setScale(0.5f, 0.5f);
            font.draw(batch, layoutPresents, Alchera.WIDTH / 2 - (layoutPresents.width / 2.0f), Alchera.HEIGHT / 2 - layoutPresents.height);
        }
        blackPixel.draw(batch);
        batch.end();
    }

    @Override
    public void update(float delta) {
        if (elapsedTime > 10f){
            alpha += delta * 0.5f;
            Gdx.app.log("Alpha:", String.valueOf(alpha));
            blackPixel.setColor(0,0,0,alpha >= 1 ? 1 : alpha);
            if (alpha >= 1f){
                manager.setScene(SceneManager.SceneType.GAMEPLAY);
            }

        }
        else if(elapsedTime > 5f){
            counterTime += delta;
            if (counterTime >= 0.25f && counter <=7) {
                layoutPresents.setText(font,presents.substring(0,++counter));
                counterTime -= 0.25f;
            }
        }
        else{
            if (alpha > 0f){
                alpha -= delta;
                blackPixel.setColor(0,0,0,alpha <= 0 ? 0 : alpha);
            }
        }
        elapsedTime += delta;

    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
