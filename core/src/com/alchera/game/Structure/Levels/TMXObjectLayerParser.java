package com.alchera.game.Structure.Levels;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import java.util.ArrayList;

/**
 * Created by Inspix on 19/09/2015.
 */
public class TMXObjectLayerParser {

    private ArrayList<PolylineMapObject>    polylineMapObjects;
    private ArrayList<RectangleMapObject>   rectangleMapObjects;
    private ArrayList<EllipseMapObject>     ellipseMapObjects;
    private ArrayList<PolygonMapObject>     polygonMapObjects;
    private MapLayer layer;

    public TMXObjectLayerParser(MapLayer layer){
        this.layer = layer;
        rectangleMapObjects = new ArrayList<RectangleMapObject>();
        ellipseMapObjects = new ArrayList<EllipseMapObject>();
        polylineMapObjects = new ArrayList<PolylineMapObject>();
        polygonMapObjects = new ArrayList<PolygonMapObject>();
    }

    public void parse(){
        MapObjects objects = layer.getObjects();
        for(MapObject object : objects){
            if (object instanceof RectangleMapObject){
               this.rectangleMapObjects.add((RectangleMapObject)object);
            }else if(object instanceof PolylineMapObject){
                this.polylineMapObjects.add((PolylineMapObject)object);
            }else if(object instanceof EllipseMapObject){
                this.ellipseMapObjects.add((EllipseMapObject)object);
            }else if (object instanceof PolygonMapObject){
                this.polygonMapObjects.add((PolygonMapObject)object);
            }
        }
    }


    public ArrayList<RectangleMapObject> getRectangleObjects(){
        return this.rectangleMapObjects;
    }

    public ArrayList<EllipseMapObject> getEllipseMapObjects() {
        return ellipseMapObjects;
    }

    public ArrayList<PolylineMapObject> getPolylineMapObjects() {
        return polylineMapObjects;
    }

    public ArrayList<PolygonMapObject> getPolygonMapObjects() {
        return polygonMapObjects;
    }

}
