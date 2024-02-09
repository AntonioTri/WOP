package Progetto_prog_3.objects;

import Progetto_prog_3.Game;

public class Potion extends AbstractObject{

    private float hoverOffset;
    private int maxHoverOffset, hoverDir = -1;


    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int)(3 * Game.SCALE);
        yDrawOffset = (int)(2 * Game.SCALE);

        maxHoverOffset = (int)(7 * Game.SCALE);
    }

    public void update(){
        updateAnimationTick();
        updateHovering();
    }

    //Questo metodo conferisce l'animazione di movimento su e giù della pozione, per far sembrare che voli
    private void updateHovering(){
        //Velocità di movimento verticale
        hoverOffset += (0.06f * Game.SCALE * hoverDir);

        if (hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        } else if (hoverOffset < 0) {
            hoverDir = 1;
        }

        hitbox.y = y + hoverOffset; 
    }
    
}
