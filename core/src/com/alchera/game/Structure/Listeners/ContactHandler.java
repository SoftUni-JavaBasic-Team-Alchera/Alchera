package com.alchera.game.Structure.Listeners;

import com.alchera.game.Structure.Entities.Bonuses.Bonus;
import com.alchera.game.Structure.Entities.Player;
import com.alchera.game.Structure.Entities.Traps.BaseTrap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class ContactHandler implements ContactListener {

    private Player player;
    private Fixture playerFix;

    public ContactHandler(Player player){
        this.player = player;
        this.playerFix = player.getBody().getFixtureList().get(0);
    }


    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (b == playerFix){
            a = b;
            b = contact.getFixtureA();
        }


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

        if (player.getBody().getFixtureList().get(0) == a && isTrap(b)){
            player.isDying(true);
        }

        if(isExit(b)){
            Gdx.app.exit();
        }
    }

    private boolean isTrap(Fixture b) {
        return b.getUserData() != null && b.getUserData() instanceof BaseTrap;
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        System.err.println("A:" + a.getUserData());
        System.err.println("B:" + b.getUserData());

        if (isGroundTrigger(a, b)){
            if(isGround(b))
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

    private boolean isGround(Fixture b){
        return b.getUserData() != null && b.getUserData().equals("bounds");
    }


    private boolean isGroundTrigger(Fixture a, Fixture b){
        return a == player.getGroundTrigger() || b == player.getGroundTrigger();
    }

    private boolean isExit(Fixture b){
        return b.getUserData() != null && b.getUserData().equals("Exit");
    }

    private boolean isBonus(Fixture b){
        return b.getUserData() != null && b.getUserData() instanceof Bonus;
    }
}
