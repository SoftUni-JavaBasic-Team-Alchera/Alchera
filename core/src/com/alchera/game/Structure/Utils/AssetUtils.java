package com.alchera.game.Structure.Utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


public class AssetUtils {

    public static Animation createFromAtlas(TextureAtlas atlas,String name, int count){
        Array<Sprite> sprites = new Array<Sprite>();
        for (int i = 0; i < count; i++) {
            sprites.add(atlas.createSprite(name + i));
        }

        return new Animation(1f/count,sprites);
    }

    public static Animation createFromAtlas(TextureAtlas atlas,String name, int count, int offset){
        Array<Sprite> sprites = new Array<Sprite>();
        for (int i = offset; i < count; i++) {
            sprites.add(atlas.createSprite(name + i));
        }

        return new Animation(1f/count,sprites);
    }

    public static void flipAnimation(Animation animation,boolean x,boolean y){
        for(TextureRegion region : animation.getKeyFrames()){
            region.flip(x,y);
        }
    }
}
