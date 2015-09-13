package com.alchera.game.Structure.Listeners;

import com.alchera.game.Structure.Entities.Enemys.Enemy;
import com.alchera.game.Structure.Entities.Enemys.EnemyState;
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
        if (contact.getFixtureA() == player.getGroundTrigger() || contact.getFixtureB() == player.getGroundTrigger()){
            if(contact.getFixtureA().getBody().getUserData() instanceof Enemy || contact.getFixtureB().getBody().getUserData() instanceof Enemy)
            {

            }else{
                player.setGrounded(true);
            }
            Gdx.app.log("Grounded:", String.valueOf(true));
        }

        if (contact.getFixtureB().getUserData() != null)
            if(contact.getFixtureB().getUserData().equals("exit")){
                Gdx.app.exit();
            }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA() == player.getGroundTrigger() || contact.getFixtureB() == player.getGroundTrigger()){
            if(contact.getFixtureA().getBody().getUserData() instanceof Enemy || contact.getFixtureB().getBody().getUserData() instanceof Enemy)
            {

            }else{
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
}
