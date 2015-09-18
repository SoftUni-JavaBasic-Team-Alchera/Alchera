package com.alchera.game.Structure.Listeners;

import com.alchera.game.Structure.Entities.Bonuses.Bonus;
import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class ContactHandler implements ContactListener {

    private Player player;

    public ContactHandler(Player player){
        this.player = player;
    }


    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (isGroundTrigger(a,b)){
            if(!isBonus(b))
            {
                player.setGrounded(true);
            }
            Gdx.app.log("Grounded:", String.valueOf(true));
        }

        if (player.getBody().getFixtureList().get(0) == a && isBonus(b)){
            Bonus bonus = (Bonus)b.getUserData();
            bonus.activate(player);
        }

        if(isExit(b)){
            Gdx.app.exit();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (isGroundTrigger(a, b)){
            if(!isBonus(b))
            {
                player.setGrounded(false);
            }
            Gdx.app.log("Grounded:", String.valueOf(false));
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


    private boolean isGroundTrigger(Fixture a, Fixture b){
        return a == player.getGroundTrigger() || b == player.getGroundTrigger();
    }

    private boolean isExit(Fixture b){
        return b.getUserData() != null && b.getUserData().equals("exit");
    }

    private boolean isBonus(Fixture b){
        return b.getUserData() != null && b.getUserData() instanceof Bonus;
    }
}
