package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class CreditsScene extends Scene {
    private BitmapFont titleFont;
    private BitmapFont font;

    private final String title = "CREDITS:";
    private String alchera = "Team ALCHERA";
    private String[] teamNames;
    private Texture weasel;

    private int currentItem;
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
                "DESIGN & PRODUCTION",
                "Ylian Rusev",
                "Vasil Nedyalkov",
                "Boyan Blagiev",
                "Galya Georgieva",
                "Zdravko Bîtushanov"
        };
        weasel = new Texture("sprites/weaselCredits.png");

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //draw weasel
        batch.draw(weasel,800,0);
        titleFont.draw(batch, alchera, 400, 600);
        //draw title
        titleFont.draw(batch, title, 400, 500);
        //draw credits info
        for (int i = 0; i < teamNames.length; i++) {
            if (currentItem == i) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(batch, teamNames[i],420,400-50*i);
        }

        batch.end();

    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void dispose() {
        font.dispose();
        titleFont.dispose();
    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            manager.setScene(SceneManager.SceneType.MAINMENU);
        }
    }

}
