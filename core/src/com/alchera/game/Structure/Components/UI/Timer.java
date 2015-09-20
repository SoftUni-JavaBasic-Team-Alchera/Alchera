package com.alchera.game.Structure.Components.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Inspix on 15/09/2015.
 */
public class Timer extends BaseUIComponent {

    private BitmapFont font;
    private long startTime;

    private long seconds;
    private long minutes;

    public Timer(Vector2 pos){
        this(pos.x,pos.y);
    }

    public Timer(float x, float y){
        this.position = new Vector2(x,y);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kraash.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.color = Color.WHITE;
        parameters.size = 12;
        this.isVisible = true;

        font = generator.generateFont(parameters);
        startTime = System.currentTimeMillis();

    }

    @Override
    public void render(SpriteBatch batch) {

        font.draw(batch,String.format("%02d:%02d",minutes,seconds),position.x,position.y);
    }

    @Override
    public void update(float delta) {
        long current = TimeUtils.timeSinceMillis(startTime);
        seconds = (current / 1000) % 60;
        minutes = (current / (1000 * 60)) % 60;
    }

    public void restart(){
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void setAlpha(float value) {
        Color clr = font.getColor();
        this.font.setColor(clr.r,clr.g,clr.b, MathUtils.clamp(value,0,1));
    }

    @Override
    public float getAlpha() {
        return font.getColor().a;
    }
}
