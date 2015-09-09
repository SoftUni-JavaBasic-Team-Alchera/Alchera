package com.alchera.game.Structure.Listeners;

import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactHandler implements ContactListener {

    private Player player;

    public ContactHandler(Player player){
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA() == player.getGroundTrigger() || contact.getFixtureB() == player.getGroundTrigger()){
            player.setGrounded(true);
            Gdx.app.log("Grounded:", String.valueOf(true));
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA() == player.getGroundTrigger() || contact.getFixtureB() == player.getGroundTrigger()){
            player.setGrounded(false);
            Gdx.app.log("Grounded:", String.valueOf(false));
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
