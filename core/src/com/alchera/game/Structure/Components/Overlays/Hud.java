package com.alchera.game.Structure.Components.Overlays;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.UI.BonusField;
import com.alchera.game.Structure.Components.UI.HealthBar;
import com.alchera.game.Structure.Components.UI.Timer;
import com.alchera.game.Structure.Components.UI.UIComponent;
import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Managers.SceneManager;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Inspix on 14/09/2015.
 */
public class Hud extends Overlay {

    private BonusField bonusField;
    private HealthBar healthBar;
    private Timer timer;
    private boolean isTransitioningIn = false;
    private boolean isTransitioningOut = false;

    public Hud(SceneManager sm,Player player) {
        super(sm);
        this.bonusField = new BonusField(25,Alchera.HEIGHT - 100);
        this.healthBar = new HealthBar();
        this.healthBar.setPlayer(player);
        this.timer = new Timer(25, 40);
        this.components.add(this.healthBar);
        this.components.add(this.timer);
        this.components.add(this.bonusField);
        this.transition = new Transition() {


            @Override
            public boolean transitionIn(float delta) {
                float alpha = MathUtils.lerp(components.get(0).getAlpha(), 1, 0.1f);
                for (UIComponent component : components){
                    component.setAlpha(alpha);
                }
                if (alpha < 1)
                    return false;
                else
                    return true;
            }

            @Override
            public boolean transitionOut(float delta) {
                float alpha = MathUtils.lerp(components.get(0).getAlpha(), 0, 0.1f);
                for (UIComponent component : components){
                    component.setAlpha(alpha);
                }
                if (alpha > 0)
                    return false;
                else{
                    isVisible = false;
                    return true;
                }
            }
        };
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void update(float delta) {
        if (isTransitioningIn)
            isTransitioningIn = !transition.transitionIn(delta);
        if (isTransitioningOut)
            isTransitioningOut = !transition.transitionOut(delta);
        super.update(delta);
    }

    @Override
    public void show() {
        if (isTransitioningOut)
            isTransitioningOut = false;
        isTransitioningIn = true;
        this.isVisible = true;
    }

    @Override
    public void hide() {
        if (isTransitioningIn)
            isTransitioningIn = false;
        isTransitioningOut = true;
    }

    public BonusField getBonusField(){
        return this.bonusField;
    }

    public HealthBar getHealthBar() {return this.healthBar;}

    @Override
    public void dispose() {
        super.dispose();
    }

    public Timer getTimer() {
        return timer;
    }
}
