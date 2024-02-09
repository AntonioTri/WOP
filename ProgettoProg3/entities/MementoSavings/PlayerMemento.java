package Progetto_prog_3.entities.MementoSavings;

import Progetto_prog_3.entities.Player;

public class PlayerMemento extends Memento {

    private float attackBoxX, attackBoxY;
    private float hitBoxX, hitBoxY;
    private int currentHealth;

    public PlayerMemento(Player player){


        hitBoxX = (float)player.getHitbox().getX();
        hitBoxY = (float)player.getHitbox().getY();
        
        attackBoxX = (float)player.getHitbox().getX();
        attackBoxY = (float)player.getHitbox().getY();

        this.currentHealth = player.getCurrentHealth();
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    @Override
    public float getHitboxY() {
        return hitBoxY;
    }
    
    @Override
    public float getHitboxX() {
        return hitBoxX;
    }

    @Override
    public float getAttackBoxX() {
        return attackBoxX;
    }

    @Override
    public float getAttackBoxY() {
        return attackBoxY;
    }
    
}
