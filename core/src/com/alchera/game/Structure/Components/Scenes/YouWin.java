package com.alchera.game.Structure.Components.Scenes;

import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

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

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //draw weasel
        batch.draw(weasel,900,0);
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
        handleInput();
    }

    @Override
    public void dispose() {
        font.dispose();
        titleFont.dispose();
    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            if (currentItem>0) currentItem--;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            if (currentItem<2) currentItem++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            changeGameState();
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
