package com.alchera.game.Structure.Components.UI;

import com.alchera.game.Structure.Entities.Bonuses.Bonus;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

/**
 * Created by Inspix on 15/09/2015.
 */
public class BonusField extends BaseUIComponent {

    private LinkedList<Bonus> bonuses;
    private float x,y;
    private Vector2 startingPosition;

    public BonusField(Vector2 position){
        this.startingPosition = position;
        this.isVisible = true;
        x = position.x + 5;
        y = position.y;
        bonuses = new LinkedList<Bonus>();
    }

    public BonusField(float x, float y){
        this(new Vector2(x,y));
    }

    @Override
    public void render(SpriteBatch batch) {
        int counter = 0;

        for(Bonus bonus : bonuses){
            batch.draw(bonus.getSprite(), x, y,
                    bonus.getSprite().getOriginX(),
                    bonus.getSprite().getOriginY(),
                    bonus.getSprite().getWidth(),
                    bonus.getSprite().getHeight(),
                    bonus.getSprite().getScaleX(),
                    bonus.getSprite().getScaleY(),
                    0);
            x += bonus.getSprite().getWidth() * bonus.getSprite().getScaleX() + 5;
            counter++;
            if (counter % 5 == 0) {
                x = startingPosition.x;
                y -= bonus.getSprite().getHeight() * bonus.getSprite().getScaleY() + 5;
            }

        }
        x = startingPosition.x;
        y = startingPosition.y;

    }

    @Override
    public void update(float delta) {
        for(Bonus bonus : bonuses){
            if (bonus.isEffectOver()){
                bonuses.remove(bonus);
            }else{
                bonus.update(delta);
            }
        }
    }

    public void addBonus(Bonus bonus){
        this.bonuses.addLast(bonus);
    }
}
