package Progetto_prog_3.entities.RenderChain;

import static Progetto_prog_3.utils.Constants.EnemtConstants.HellBound.*;
import java.awt.Graphics;
import Progetto_prog_3.entities.Player;
import Progetto_prog_3.entities.enemies.AbstractEnemy;

public class RenderHellBound implements RenderInterface{

    RenderInterface nextHandler;

    @Override
    public void setNextHandler(RenderInterface nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void renderEntity(Graphics g, Player player, RenderingRequest[] requests, AbstractEnemy enemy, int xLevelOffset, int yLevelOffset) {
        if (enemy.getEnemyType() == HELL_BOUND) {

            g.drawImage(requests[enemy.getEnemyType()].getSprites()[enemy.getState()][enemy.getAniIndex()],
                            (int)enemy.getHitbox().x - xLevelOffset - HELL_BOUND_DROW_OFFSET_X + enemy.flipX(),
                            ((int) enemy.getHitbox().y - yLevelOffset - HELL_BOUND_DROW_OFFSET_Y),
                            HELL_BOUND_WIDTH * enemy.flipW(),
                            HELL_BOUND_HEIGHT, null);

            //Queste due righe di codice possono essere decommentate per mostrare la hitbox ed attackbox dell'hellbound
            //enemy.drawHitbox(g, xLevelOffset, yLevelOffset);
            //enemy.drowAttackBox(g, xLevelOffset, yLevelOffset);

            if (enemy.getState() == HELL_BOUND_DIE) {
                enemy.setAniSpeed(20);
                enemy.setInvulnerability(true);
            }
            
        } else {
            nextHandler.renderEntity(g, player, requests, enemy, xLevelOffset, yLevelOffset);
        }
    }
    


}
