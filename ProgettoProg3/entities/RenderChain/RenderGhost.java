package Progetto_prog_3.entities.RenderChain;

import static Progetto_prog_3.utils.Constants.EnemtConstants.Ghost.*;
import java.awt.Graphics;
import Progetto_prog_3.Game;
import Progetto_prog_3.entities.Player;
import Progetto_prog_3.entities.enemies.AbstractEnemy;

public class RenderGhost implements RenderInterface {

    RenderInterface nextHandler;

    @Override
    public void setNextHandler(RenderInterface nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void renderEntity(Graphics g, Player player, RenderingRequest[] requests, AbstractEnemy enemy, int xLevelOffset, int yLevelOffset) {
        if (enemy.getEnemyType() == GHOST) {
            
            g.drawImage(requests[enemy.getEnemyType()].getSprites()[enemy.getState()][enemy.getAniIndex()],
                            (int)enemy.getHitbox().x - xLevelOffset - GHOST_DRAW_OFFSET_X + enemy.flipXP(player), 
                            ((int) enemy.getHitbox().y - yLevelOffset - GHOST_DRAW_OFFSET_Y), 
                            GHOST_WIDTH * enemy.flipWP(player),
                            GHOST_HEIGHT, null);

            //Queste due righe di codice possono essere decommentate per mostrare la hitbox ed attackbox del ghost
            //enemy.drawHitbox(g, xLevelOffset, yLevelOffset);
            //enemy.drowCircularAttackBox(g, xLevelOffset, yLevelOffset);

            if (enemy.getState() == GHOST_ATTACK) {
                g.drawImage(requests[enemy.getEnemyType()].getSpecialAttack()[enemy.getAniIndex()],
                            (int)(enemy.getHitbox().x - (113 * Game.SCALE) - xLevelOffset), 
                            (int)(enemy.getHitbox().y - (100 * Game.SCALE) - yLevelOffset), 
                            GHOST_ELECTRIC_BALL_LENGHT, 
                            GHOST_ELECTRIC_BALL_LENGHT, null);
            }

            if (enemy.getState() == GHOST_DIE) {
                enemy.setAniSpeed(20);
                enemy.setInvulnerability(true);
                    
            }


        } else {
            System.out.println("The enemy type \"" + enemy.getEnemyType() + "\" cannot be rendered!");
        }
    }
    
}
