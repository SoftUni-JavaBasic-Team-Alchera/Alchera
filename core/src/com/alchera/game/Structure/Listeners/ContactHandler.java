package com.alchera.game.Structure.Listeners;

import com.alchera.game.Structure.Entities.Enemys.Enemy;
import com.alchera.game.Structure.Entities.Enemys.EnemyState;
import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactHandler implements ContactListener {

    private Player player;
    private Enemy enemy;

    public ContactHandler(Player player, Enemy enemy){

        this.player = player;
        this.enemy = enemy;
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

        //Enemy logic
        if (contact.getFixtureA() == enemy.getChasingTrigger() || contact.getFixtureB() == enemy.getChasingTrigger() &&
                !(contact.getFixtureA() == enemy.getAttackTrigger() || contact.getFixtureB() == enemy.getAttackTrigger())){
            enemy.setEnemyState(EnemyState.chasing);
            if (player.isFlipped()){
                enemy.setDirection("Right");


            }else if(!player.isFlipped()){
                enemy.setDirection("Left");

            }
            Gdx.app.log("Enemy:", "Chasing");
        }else if (contact.getFixtureA() == enemy.getAttackTrigger() || contact.getFixtureB() == enemy.getAttackTrigger()){
            enemy.setEnemyState(EnemyState.attack);
            Gdx.app.log("Enemy:", "Attack");
        }else {
            enemy.setEnemyState(EnemyState.chill);
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
        if (!(contact.getFixtureA() == enemy.getAttackTrigger() || contact.getFixtureB() == enemy.getAttackTrigger()) &&
               ! (contact.getFixtureA() == player.getGroundTrigger() || contact.getFixtureB() == player.getGroundTrigger())){
            enemy.setEnemyState(EnemyState.chill);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
