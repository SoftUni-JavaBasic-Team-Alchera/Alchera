package com.alchera.game.Structure.Components.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Inspix on 15/09/2015.
 */
public abstract class BaseUIComponent implements UIComponent {
    protected boolean isVisible;
    protected Vector2 position;

    public abstract void render(SpriteBatch batch);
    public abstract void update(float delta);

    @Override
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void setVisible(boolean visibility) {
        this.isVisible = visibility;
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }
}
