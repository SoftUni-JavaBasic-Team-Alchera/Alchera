package com.alchera.game.Structure.Components.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Inspix on 15/09/2015.
 */
public interface UIComponent {

    void render(SpriteBatch batch);
    void update(float delta);
    boolean isVisible();
    void setAlpha(float value);
    float getAlpha();
    void setVisible(boolean visibility);
    Vector2 getPosition();
}
