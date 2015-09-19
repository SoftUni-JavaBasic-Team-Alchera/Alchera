package com.alchera.game.Structure.Entities;

/**
 * Created by Inspix on 19/09/2015.
 */
public class Lock {

    public enum Type{
        YELLOW,
        ORANGE,
        GREEN
    }

    private Type type;
    private float x,y;

    public Lock(Type type,float x, float y){
        this.type = type;
        this.x = x;
        this.y = y;
        // TODO: Lock unlock logic to end the level
    }
}
