package com.alchera.game.Structure.Levels;

import com.alchera.game.Structure.Entities.Bonuses.Bonus;
import com.alchera.game.Structure.Entities.Bonuses.BonusHealth;
import com.alchera.game.Structure.Entities.Bonuses.BonusJump;
import com.alchera.game.Structure.Entities.Bonuses.BonusSpeed;
import com.alchera.game.Structure.Entities.Traps.BaseTrap;
import com.alchera.game.Structure.Utils.ShapeFactory;
import com.alchera.game.Structure.Utils.Variables;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Inspix on 12/09/2015.
 */
public class Level {

    Texture backgroundTxt;
    SpriteBatch batch;
    public final Vector2 size;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public Vector2 playerSpawn = new Vector2(0,0);
    private ArrayList<Vector2> traps = new ArrayList<Vector2>();
    private LinkedList<Bonus> bonuses = new LinkedList<Bonus>();


    public Level(SpriteBatch batch,World world){
        this.batch = batch;
        map = new TmxMapLoader().load("map1.tmx");
        size = parseSize();
        renderer = new OrthogonalTiledMapRenderer(map,batch);
        parseObjectLayer(world, map.getLayers().get("bounds").getObjects());
        backgroundTxt = new Texture("background.png");
    }


    public void render(OrthographicCamera camera){

        batch.begin();
        batch.draw(backgroundTxt,-400,0);
        batch.end();
        renderer.setView(camera);
        renderer.render();
    }

    private void parseObjectLayer(World world, MapObjects objects){
        for(MapObject object : objects){
            boolean isBonus = false;
            Shape shape = null;
            Object objectReference = null;
            Body body;
            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            String name = null;
            if (object instanceof PolylineMapObject){
                shape = ShapeFactory.createChainShape((PolylineMapObject)object);
                fdef.isSensor = false;
            }else if (object instanceof EllipseMapObject){
                EllipseMapObject obj = (EllipseMapObject)object;
                name = obj.getName();
                if (name.equals("playerspawn"))
                    playerSpawn.set(obj.getEllipse().x, obj.getEllipse().y);
                else if (name.equals("exit")){
                    shape = ShapeFactory.createCircle(obj);
                    fdef.isSensor = true;
                }else if (name.equals("enemy")){
                    traps.add(new Vector2(obj.getEllipse().x,obj.getEllipse().y));
                    fdef.isSensor = false;
                }else if(name.startsWith("Bonus")){
                    if (name.endsWith("Speed")){
                        shape = ShapeFactory.createCircle(obj);
                        fdef.isSensor = true;
                        BonusSpeed bonus = new BonusSpeed(obj.getEllipse().x,obj.getEllipse().y);
                        isBonus = true;
                        bonuses.add(bonus);
                        objectReference = bonus;
                    }else if(name.endsWith("Health")){
                        shape = ShapeFactory.createCircle(obj);
                        fdef.isSensor = true;
                        BonusHealth bonus = new BonusHealth(obj.getEllipse().x,obj.getEllipse().y);
                        isBonus = true;
                        bonuses.add(bonus);
                        objectReference = bonus;
                    }else if(name.endsWith("Jump")){
                        shape = ShapeFactory.createCircle(obj);
                        fdef.isSensor = true;
                        BonusJump bonus = new BonusJump(obj.getEllipse().x,obj.getEllipse().y);
                        isBonus = true;
                        bonuses.add(bonus);
                        objectReference = bonus;
                    }
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
            fixture.setUserData(objectReference == null ? name : objectReference);
            if (isBonus)
                bonuses.getLast().setBody(body);
            shape.dispose();
        }


    }


    private Vector2 parseSize(){
        MapProperties properties = map.getProperties();

        int tilesX = properties.get("width", Integer.class);
        int tilesY = properties.get("height", Integer.class);
        int tileWidth = properties.get("tilewidth", Integer.class);
        int tileHeight = properties.get("tileheight",Integer.class);

        return new Vector2(tilesX * tileWidth,tilesY * tileHeight);
    }

    public LinkedList<Bonus> getBonuses(){
        return this.bonuses;
    }


    public ArrayList<Vector2> getTraps() {
        return traps;
    }
}
