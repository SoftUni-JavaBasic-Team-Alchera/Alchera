package com.alchera.game.Structure.Components.Overlays;

import com.alchera.game.Alchera;
import com.alchera.game.Structure.Components.UI.BonusField;
import com.alchera.game.Structure.Components.UI.HealthBar;
import com.alchera.game.Structure.Components.UI.Timer;
import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Managers.SceneManager;

/**
 * Created by Inspix on 14/09/2015.
 */
public class Hud extends Overlay {

    private BonusField bonusField;

    public Hud(SceneManager sm,Player player) {
        super(sm);
        bonusField = new BonusField(25,Alchera.HEIGHT - 100);
        this.components.add(new HealthBar(player));
        this.components.add(new Timer(25, 40));
        this.components.add(bonusField);
    }

    public BonusField getBonusField(){
        return this.bonusField;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
