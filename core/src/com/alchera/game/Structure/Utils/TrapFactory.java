package com.alchera.game.Structure.Utils;

import com.alchera.game.Structure.Entities.Traps.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Nedyalkov on 9/16/2015.
 */
public class TrapFactory {

    public static BaseTrap createTrap(TrapType type, World boxWorld, float X, float Y){
        BaseTrap trap = null;
        switch (type){
            case ROCK:
                trap= new Rock(boxWorld,X,Y);
            case FLAME:
                trap= new Flame(boxWorld,X,Y);
            case LANDMINE:
                trap= new LandMine(boxWorld,X,Y);
            case SAWBLADE:
                trap= new SawBlade(boxWorld,X,Y);
        }
        return trap;
    }

    public static BaseTrap createRandomTrap(World boxWorld, float X, float Y){
        int result = MathUtils.random(3);
        switch (result){
            case 0:
                return new Rock(boxWorld,X,Y);
            case 1:
                return new Flame(boxWorld,X,Y);
            case 2:
                return new LandMine(boxWorld,X,Y);
            case 3:
                return new SawBlade(boxWorld,X,Y);
        }
        return null;
    }
}
