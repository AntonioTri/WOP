package Progetto_prog_3.entities.RenderChain;

import static Progetto_prog_3.utils.Constants.EnemtConstants.NightBorne.*;
import java.awt.Graphics;
import Progetto_prog_3.entities.Player;
import Progetto_prog_3.entities.enemies.AbstractEnemy;

public class RenderNightBorne implements RenderInterface {

    RenderInterface nextHandler;

    @Override
    public void setNextHandler(RenderInterface nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void renderEntity(Graphics g, Player player, RenderingRequest[] requests, AbstractEnemy enemy, int xLevelOffset, int yLevelOffset) {
        if (enemy.getEnemyType() == NIGHT_BORNE) {
            
            g.drawImage(requests[enemy.getEnemyType()].getSprites()[enemy.getState()][enemy.getAniIndex()],
                            (int)enemy.getHitbox().x - xLevelOffset - NIGHT_BORNE_DROW_OFFSET_X + enemy.flipX(), 
                            ((int)enemy.getHitbox().y - yLevelOffset - NIGHT_BORNE_DROW_OFFSET_Y),
                            (NIGHT_BORNE_WIDHT + 25) * enemy.flipW(),
                             NIGHT_BORNE_HEIGHT + 25, null);

            //Queste due righe di codice possono essere decommentate per mostrare la hitbox ed attackbox del nightborne
            //enemy.drawHitbox(g, xLevelOffset, yLevelOffset);
            //enemy.drowAttackBox(g, xLevelOffset, yLevelOffset);

            if (enemy.getState() == NIGHT_BORNE_DIE) {
                enemy.setAniSpeed(20);
                enemy.setInvulnerability(true);
                    
            }

        } else {
            nextHandler.renderEntity(g, player, requests, enemy, xLevelOffset, yLevelOffset);
        }
    }

}
