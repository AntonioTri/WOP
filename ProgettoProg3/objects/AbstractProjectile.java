package Progetto_prog_3.objects;

import java.awt.geom.Rectangle2D;

import static Progetto_prog_3.utils.Constants.GRAVITY;
import static Progetto_prog_3.utils.Constants.Projectiles.*;

//Questa clase definisce tutta la famiglia di oggetti di tipo proiettile
//Richiede nel costruttore una tipologia di proiettile, 
//questa viene usata da delle funzioni apposite per definire la larghezza e l'altezza
public abstract class AbstractProjectile {

    private int projType;
    protected Rectangle2D.Float hitbox;
    protected int direction;
    private boolean active = true;
    private boolean canDoDamage = true;
    private float projectileSpeed;

    public AbstractProjectile(int x, int y, int direction, int projType){
        this.projType = projType;
        this.direction = direction;
        projectileSpeed = getProjectileSpeed(projType);
        hitbox = new Rectangle2D.Float(x, y,getProjectileWidth(projType), getProjectileHeight(projType));
    }

    public void updatePosition(){
        hitbox.x += direction * projectileSpeed;
    }

    public void setPosition(int x, int y){
        hitbox.x = x;
        hitbox.y = y;
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public boolean getActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public int getDirection(){
        return direction;
    }

    public void setDirection(int dirction){
        this.direction = dirction;
    }

    public float getProjSpeed(){
        return projectileSpeed;
    }

    public void setProjectileSpeed(float velocity){
        projectileSpeed = velocity;
    }

    public void setCanDoDamage(boolean canDoDamage){
        this.canDoDamage = canDoDamage;
    }

    public boolean getCanDoDamage(){
        return canDoDamage;
    }

}
