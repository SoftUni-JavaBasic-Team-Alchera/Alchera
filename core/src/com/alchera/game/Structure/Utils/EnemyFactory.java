package com.alchera.game.Structure.Utils;

import com.alchera.game.Structure.Entities.Enemys.Enemy;
import com.alchera.game.Structure.Entities.Enemys.EnemyType;
import com.alchera.game.Structure.Entities.Player;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Nedyalkov on 9/16/2015.
 */
public class EnemyFactory {

    public static Enemy createEnemy(EnemyType enemyType, Player player, World boxWorld, int X, int Y){
        Enemy enemy = null;
        switch (enemyType) {
            case BOSS:
                return new Enemy(boxWorld, player, "sprites/bosssheet.txt", X, Y, "attack0", "walk", 9, "attack", 2);
            case INDIAN:
                return new Enemy(boxWorld, player, "sprites/enemy.txt", X, Y, "walk0", "walk", 4, "attack", 2);
            case SKELETON:
                return new Enemy(boxWorld, player, "sprites/enemy2sheet.txt", X, Y, "walk0", "walk", 9, "attack", 4);
            case NUDEGUY:
                return new Enemy(boxWorld, player, "sprites/enemy3sheet.txt", X, Y, "walk0", "walk", 8, "attack", 3);
        }
        return enemy;
    }

    public static Enemy createRandomEnemy(Player player, World boxWorld, int X, int Y) throws Exception {
        int result = MathUtils.random(3);

        switch (result) {
            case 0:
                return new Enemy(boxWorld, player, "sprites/bosssheet.txt", X, Y, "attack0", "walk", 9, "attack", 2);
            case 1:
                return new Enemy(boxWorld, player, "sprites/enemy.txt", X, Y, "walk0", "walk", 4, "attack", 2);
            case 2:
                return new Enemy(boxWorld, player, "sprites/enemy2sheet.txt", X, Y, "walk0", "walk", 9, "attack", 4);
            case 3:
                return new Enemy(boxWorld, player, "sprites/enemy3sheet.txt", X, Y, "walk0", "walk", 8, "attack", 3);
            default:
                return null;
        }
    }
}
