package Progetto_prog_3.objects;

import Progetto_prog_3.Game;

public class Spike extends AbstractObject{

    public Spike(int x, int y, int objType) {
        super(x, y, objType);

        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitbox.y += yDrawOffset;

    }
    
}
