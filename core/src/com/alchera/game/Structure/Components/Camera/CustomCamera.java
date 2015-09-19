package com.alchera.game.Structure.Components.Camera;

import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Inspix on 09/09/2015.
 */
public class CustomCamera extends OrthographicCamera {

    private Interpolation interpolation;
    private Vector2 min = new Vector2(),max = new Vector2();
    private Player player;
    private boolean isBox2D;
    private boolean isLimited;
    public CustomCamera(Player player) {
        super();
        this.player = player;
    }

    @Override
    public void update() {
        if(interpolation == null)
            interpolation = new BasicInterpolation();
        this.setPosition(interpolation.Interpolate(this.position, isBox2D ? player.getBoxWorldX() : player.getWorldX(), isBox2D ? player.getBoxWorldY() : player.getWorldY(), 0));
        super.update();
    }

    public void setInterpolation(Interpolation interpolation){
        this.interpolation = interpolation;
    }

    public void isBox2DCamera(boolean isBox2d){
        this.isBox2D = isBox2d;
    }

    public void setPosition(Vector3 position){
        if (!isLimited)
            this.position.set(position);
        else
            this.position.set(MathUtils.clamp(position.x,min.x,max.x),MathUtils.clamp(position.y,min.y,max.y),position.z);
    }

    public void setMinPosition(Vector2 min){
        this.min = min;
    }

    public void setMaxPosition(Vector2 max){
        this.max = max;
    }

    public void setLimited(boolean limit){
        this.isLimited = limit;
    }

    public boolean isLimited(){
        return this.isLimited;
    }


}
