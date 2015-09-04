package com.alchera.game.Structure.Utils;

import static com.alchera.game.Structure.Utils.Variables.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Inspix on 04/09/2015.
 */
public class BodyFactory {

    public static Body CreateStaticRectangle(World world, float width, float height, float x, float y, float density, float friction){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2) / PPM, (height / 2) / PPM,new Vector2((width/2)/PPM, (height/2)/PPM),0);

        Body result = createBody(world,shape,x,y,density,friction,true, BodyDef.BodyType.StaticBody);
        shape.dispose();
        return result;
    }

    public static Body CreateDynamicRectangle(World world, float width, float height, float x, float y, float density,float friction){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2) / PPM, (height / 2) / PPM,new Vector2((width/2)/PPM, (height/2)/PPM),0);

        Body result = createBody(world,shape,x,y,density,friction,true, BodyDef.BodyType.DynamicBody);
        shape.dispose();
        return result;
    }

    private static Body createBody(World world, Shape shape, float x, float y, float density, float friction, boolean fixedangle, BodyDef.BodyType type){
        BodyDef bdef = new BodyDef();
        bdef.type = type;
        bdef.position.set(x / PPM, y / PPM);
        bdef.fixedRotation = fixedangle;
        Body result = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.friction = friction;
        fdef.density = density;
        fdef.shape = shape;

        result.createFixture(fdef);

        return result;
    }
}
