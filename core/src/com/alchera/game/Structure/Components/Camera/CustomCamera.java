package com.alchera.game.Structure.Components.Camera;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Inspix on 09/09/2015.
 */
public class CustomCamera extends OrthographicCamera {

    private Interpolation interpolation;
    private Player player;
    private boolean isBox2D;
    public CustomCamera(Player player) {
        super();
        this.player = player;
    }

    @Override
    public void update() {
        if(interpolation == null)
            interpolation = new BasicInterpolation();
        this.position.set(interpolation.Interpolate(this.position, isBox2D ? player.getBoxWorldX() : player.getWorldX(), isBox2D ? player.getBoxWorldY() : player.getWorldY(), 0));
        super.update();
    }

    public void setInterpolation(Interpolation interpolation){
        this.interpolation = interpolation;
    }

    public void isBox2DCamera(boolean isBox2d){
        this.isBox2D = isBox2d;
    }


}
