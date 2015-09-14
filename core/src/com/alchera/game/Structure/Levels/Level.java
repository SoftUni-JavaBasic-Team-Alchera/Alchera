package com.alchera.game.Structure.Levels;

import com.alchera.game.Structure.Utils.ShapeFactory;
import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

/**
 * Created by Inspix on 12/09/2015.
 */
public class Level {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public Vector2 playerSpawn = new Vector2(0,0);
    public ArrayList<Vector2> enemyCoordinates;


    public Level(SpriteBatch batch,World world){
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,batch);
        enemyCoordinates = new ArrayList<Vector2>();
        parseObjectLayer(world,map.getLayers().get("bounds").getObjects());
    }


    public void render(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    private void parseObjectLayer(World world, MapObjects objects){
        for(MapObject object : objects){
            Shape shape = null;
            Body body;
            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            String name = null;
            if (object instanceof PolylineMapObject){
                shape = ShapeFactory.createChainShape((PolylineMapObject)object);
                fdef.isSensor = false;
            }else if (object instanceof EllipseMapObject){
                EllipseMapObject obj = (EllipseMapObject)object;
                if (obj.getName().equals("playerspawn"))
                    playerSpawn.set(obj.getEllipse().x, obj.getEllipse().y);
                else if (obj.getName().equals("enemy"))
                    enemyCoordinates.add(new Vector2(obj.getEllipse().x,obj.getEllipse().y));
                else if (obj.getName().equals("exit")){
                    shape = ShapeFactory.createCircle(obj);
                    fdef.isSensor = true;
                    name = obj.getName();
                }
            }else if(object instanceof RectangleMapObject){
                RectangleMapObject obj = (RectangleMapObject)object;
                bdef.position.set(obj.getRectangle().x/ Variables.PPM,obj.getRectangle().y/Variables.PPM);
                shape = ShapeFactory.createRectangle(obj.getRectangle().width,obj.getRectangle().height);
                fdef.isSensor = false;
            }
            else{
                continue;
            }

            if(shape == null)
                continue;

            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            fdef.density = 1;
            fdef.shape = shape;
            Fixture fixture = body.createFixture(fdef);
            fixture.setUserData(name);
            shape.dispose();
        }


    }

    public ArrayList<Vector2> getEnemyCoordinates(){
        return this.enemyCoordinates;
    }


}
