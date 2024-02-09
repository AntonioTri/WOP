package Progetto_prog_3.entities.MementoSavings;

import static Progetto_prog_3.utils.Constants.EnemtConstants.Ghost.GHOST;
import static Progetto_prog_3.utils.Constants.EnemtConstants.Ghost.GHOST_NOT_SPAWNED;
import static Progetto_prog_3.utils.Constants.EnemtConstants.HellBound.HELL_BOUND;
import static Progetto_prog_3.utils.Constants.EnemtConstants.HellBound.HELL_BOUND_WALK;
import static Progetto_prog_3.utils.Constants.EnemtConstants.NightBorne.NIGHT_BORNE;
import static Progetto_prog_3.utils.Constants.EnemtConstants.NightBorne.NIGHT_BORNE_IDLE;

import Progetto_prog_3.entities.enemies.AbstractEnemy;

public class EnemyMemento extends Memento {
    
    private float attackBoxX, attackBoxY;
    private float hitBoxX, hitBoxY;
    private int currentHealth;
    private float airSpeed;
    private boolean active, firstUpdate, invulnerability;
    private int state;

    public EnemyMemento(AbstractEnemy enemy){

        hitBoxX = (float)enemy.getHitbox().getX();
        hitBoxY = (float)enemy.getHitbox().getY();
        currentHealth = enemy.getCurrentHealth();
        airSpeed = enemy.getAirSpeed();
        active = enemy.getActive();
        firstUpdate = enemy.getFirstUpdate();
        invulnerability = enemy.getInvulnerability();
        setEnemyState(enemy.getEnemyType());

    }

    private void setEnemyState(int enemyType) {
        
        switch (enemyType) {
            case NIGHT_BORNE:
                state = NIGHT_BORNE_IDLE;
                break;
            
            case HELL_BOUND:
                state = HELL_BOUND_WALK;
                break;
        
            case GHOST:
                state = GHOST_NOT_SPAWNED;
                break;
            
            default:
                break;
        }

    }

    @Override
    public float getHitboxX() {
        return hitBoxX;
    }

    @Override
    public float getHitboxY() {
        return hitBoxY;
    }

    @Override
    public float getAttackBoxX() {
        return attackBoxX;
        
    }

    @Override
    public float getAttackBoxY(){ 
        return attackBoxY; 
    }


    public int getCurrentHealth(){ return currentHealth; }
    public int getState(){ return state; }
    public float getAirSpeed(){ return airSpeed; }
    public boolean getActive(){ return active; }
    public boolean getFirstUpdate(){ return firstUpdate; }
    public boolean getInvulnerability(){ return invulnerability; }

}
