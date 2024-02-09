package Progetto_prog_3.objects;

import static Progetto_prog_3.utils.Constants.ObjectConstants.CANNON_LEFT;

import Progetto_prog_3.Game;

//La classe cannone agiunge i cannoni all'interno della mappa, questi sparano al player se è visibile e sono indistruttibili.
//Sono fissi in un posto e sono rivolti solo a destra o a sinistra
public class Cannon extends AbstractObject {

    private int cannonTileY;
    private CannonBall cannonBall;
    int direction = 1;

    public Cannon(int x, int y, int objType) {
        super(x, y, objType);

        cannonTileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
        //Queste picole modificee servono a far scendere l'immagine dal centro, lo sprite ha dello spazio vuoto sotto
        hitbox.x -= (int)(4 * Game.SCALE);
        hitbox.y += (int)(6 * Game.SCALE);
        
        if (objType == CANNON_LEFT) {
            direction = -1;
        }
        //Creazione della palla di cannone peculiare per oogni cannone
        //Questa scelta è dovuta per una correta implementazione del prototype design pattern
        cannonBall = new CannonBall((int)hitbox.x,(int) hitbox.y, direction);
    }
    

    public void update(){
        if (doAnimation) {
            updateAnimationTick();
        }
    }

    public int getCannonTyleY(){
        return cannonTileY;
    }

    public int getAniTick() {
        return aniTick;
    }

    public CannonBall getCannonBall(){
        return cannonBall;
    }

}
