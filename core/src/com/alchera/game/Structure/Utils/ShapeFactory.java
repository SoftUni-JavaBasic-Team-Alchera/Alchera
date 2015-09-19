package com.alchera.game.Structure.Utils;

import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import static com.alchera.game.Structure.Utils.Variables.PPM;

/**
 * Created by Inspix on 12/09/2015.
 */
public class ShapeFactory {

    public static ChainShape createChainShape(PolylineMapObject object){
        float[] vertecies = object.getPolyline().getVertices();
        return createChainShape(vertecies);
    }

    public static ChainShape createChainShape(float[] vertecies){
        for (int i = 0; i < vertecies.length; i++) {
            vertecies[i] /= Variables.PPM;
        }

        ChainShape shape = new ChainShape();
        shape.createChain(vertecies);
        return shape;
    }

    public static CircleShape createCircle(EllipseMapObject object){
        CircleShape shape = new CircleShape();
        shape.setRadius(5/PPM);
        //shape.setPosition(new Vector2(object.getEllipse().x/PPM,object.getEllipse().y/PPM));

        return shape;
    }

    public static PolygonShape createRectangle(float width,float height){

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2) / PPM, (height / 2) / PPM,new Vector2((width/2)/PPM, (height/2)/PPM),0);

        return shape;
    }

}
