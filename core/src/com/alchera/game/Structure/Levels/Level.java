package com.alchera.game.Structure.Levels;

import com.alchera.game.Structure.Entities.Bonuses.*;
import com.alchera.game.Structure.Entities.Lock;
import com.alchera.game.Structure.Entities.Traps.*;
import com.alchera.game.Structure.Utils.BodyFactory;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
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
    private World world;
    public Vector2 playerSpawn = new Vector2(0,0);
    private Vector2 exitCoords = new Vector2(0,0);
    private ArrayList<BaseTrap> traps = new ArrayList<BaseTrap>();
    private ArrayList<Vector2> enemyCoords = new ArrayList<Vector2>();
    private LinkedList<Lock> locks = new LinkedList<Lock>();
    private LinkedList<Bonus> bonuses = new LinkedList<Bonus>();

    public Level(SpriteBatch batch,World world){
        this.batch = batch;
        this.world = world;
        map = new TmxMapLoader().load("map.tmx");
        size = parseSize();
        renderer = new OrthogonalTiledMapRenderer(map,batch);
        backgroundTxt = new Texture("background.png");
        this.generateBounds();
        this.generateObjects();
    }


    public void render(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public void renderBG(){
        batch.begin();
        batch.draw(backgroundTxt, 0, 0);
        batch.draw(backgroundTxt, 0, backgroundTxt.getHeight());
        batch.end();
    }

    private void generateObjects(){
        TMXObjectLayerParser bounds = new TMXObjectLayerParser(map.getLayers().get("objects"));
        bounds.parse();
        for (RectangleMapObject rect : bounds.getRectangleObjects()){
            String name = rect.getName();
            Rectangle r = rect.getRectangle();
            Body bd = BodyFactory.CreateStaticRectangle(world, r.getWidth(), r.getHeight(), r.getX(), r.getY(), 1, 0.2f);
            if (name.startsWith("Boost")){
                if (name.endsWith("Jump")){
                    BonusSpring bonus = new BonusSpring(r.x,r.y);
                    bd.getFixtureList().get(0).setUserData(bonus);
                    bd.getFixtureList().get(0).setSensor(true);
                    bonus.setBody(bd);
                    bonuses.add(bonus);
                }
            }else if (name.startsWith("Lock")){
                Lock lock;
                if (name.endsWith("Yellow")){
                    lock = new Lock(Lock.Type.YELLOW,r.x,r.y);
                }else if (name.endsWith("Orange")){
                    lock = new Lock(Lock.Type.BLUE,r.x,r.y);
                }else{
                    lock = new Lock(Lock.Type.GREEN,r.x,r.y);
                }
                locks.add(lock);
                bd.getFixtureList().get(0).setUserData(lock);
            }

        }

       /* for (PolylineMapObject poly : bounds.getPolylineMapObjects()){
            //BodyFactory.createStaticPolyline(world, poly);
        }

        for (PolygonMapObject poly : bounds.getPolygonMapObjects()){
            //BodyFactory.createStaticPolygon(world, poly);
        }*/

        for (EllipseMapObject ellipse : bounds.getEllipseMapObjects()){
            String name = ellipse.getName();
            Ellipse e = ellipse.getEllipse();
            if (name.startsWith("Trap")){
                if (name.endsWith("SawBlade")){
                    SawBlade trap = new SawBlade(world,e.x,e.y);
                    traps.add(trap);
                }else if (name.endsWith("Mine")){
                    LandMine mine = new LandMine(world,e.x,e.y);
                    traps.add(mine);
                }else if (name.endsWith("Flame")){
                    Flame flame = new Flame(world,e.x,e.y);
                    traps.add(flame);
                }else {
                    Rock rock = new Rock(world,e.x,e.y);
                    traps.add(rock);
                }
            }else if (name.startsWith("PlayerSpawn")){
                this.playerSpawn.set(e.x,e.y);
            }else if (name.startsWith("Exit")){
                this.exitCoords.set(e.x,e.y);
            }else if (name.equals("Enemy")){
                this.enemyCoords.add(new Vector2(e.x,e.y));
            }else if (name.startsWith("Bonus")){
                if (name.endsWith("Heart")){
                    BonusHealth bonus = new BonusHealth(e.x,e.y);
                    bonuses.add(bonus);
                    Body bd = BodyFactory.createStaticEllipse(world,ellipse);
                    bonus.setBody(bd);
                    bd.getFixtureList().get(0).setSensor(true);
                    bd.getFixtureList().get(0).setUserData(bonus);
                }else if (name.endsWith("Jump")){
                    BonusJump bonus = new BonusJump(e.x,e.y);
                    bonuses.add(bonus);
                    Body bd = BodyFactory.createStaticEllipse(world,ellipse);
                    bonus.setBody(bd);
                    bd.getFixtureList().get(0).setSensor(true);
                    bd.getFixtureList().get(0).setUserData(bonus);
                }
            }else if (name.startsWith("Key")){
                if (name.endsWith("Yellow")){
                    BonusKey key = new BonusKey(Lock.Type.YELLOW,e.x,e.y);
                    bonuses.add(key);
                    Body bd = BodyFactory.createStaticEllipse(world,ellipse);
                    key.setBody(bd);
                    bd.getFixtureList().get(0).setSensor(true);
                    bd.getFixtureList().get(0).setUserData(key);
                }else if (name.endsWith("Orange")){
                    BonusKey key = new BonusKey(Lock.Type.BLUE,e.x,e.y);
                    bonuses.add(key);
                    Body bd = BodyFactory.createStaticEllipse(world,ellipse);
                    key.setBody(bd);
                    bd.getFixtureList().get(0).setSensor(true);
                    bd.getFixtureList().get(0).setUserData(key);
                }else {
                    BonusKey key = new BonusKey(Lock.Type.GREEN,e.x,e.y);
                    bonuses.add(key);
                    Body bd = BodyFactory.createStaticEllipse(world,ellipse);
                    bd.getFixtureList().get(0).setSensor(true);
                    bd.getFixtureList().get(0).setUserData(key);
                }
            }
        }
    }

    private void generateBounds(){
        TMXObjectLayerParser objects = new TMXObjectLayerParser(map.getLayers().get("bounds"));
        objects.parse();
        for (RectangleMapObject rect : objects.getRectangleObjects()){
            Rectangle r = rect.getRectangle();
            Body bd = BodyFactory.CreateStaticRectangle(world,r.getWidth(),r.getHeight(),r.getX(),r.getY(),1,0.2f);
            if (rect.getName() != null && rect.getName().equals("Lava")){
                Fixture f = bd.getFixtureList().get(0);
                f.setSensor(true);
                f.setUserData("Death");
            }
            bd.setUserData("bounds");
        }

        for (PolylineMapObject poly : objects.getPolylineMapObjects()){
            Body bd = BodyFactory.createStaticPolyline(world, poly);
            bd.setUserData("bounds");
        }

        for (PolygonMapObject poly : objects.getPolygonMapObjects()){
            Body bd = BodyFactory.createStaticPolygon(world, poly);
            bd.setUserData("bounds");
        }

        for (EllipseMapObject ellipse : objects.getEllipseMapObjects()){
            Body bd = BodyFactory.createStaticEllipse(world, ellipse);
            bd.setUserData("bounds");
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

    public void spawnExit(){
        Body bd = BodyFactory.createStaticCircle(world,1,exitCoords.x,exitCoords.y);
        bd.getFixtureList().get(0).setSensor(true);
        bd.getFixtureList().get(0).setUserData("Exit");
    }

    public LinkedList<Bonus> getBonuses(){
        return this.bonuses;
    }

    public ArrayList<BaseTrap> getTraps() {
        return traps;
    }

    public LinkedList<Lock> getLocks(){
        return this.locks;
    }

    public ArrayList<Vector2> getEnemyCoords() { return this.enemyCoords;}
}
