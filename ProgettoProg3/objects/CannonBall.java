package Progetto_prog_3.objects;

import static Progetto_prog_3.utils.Constants.Projectiles.CANNON_BALL;
import static Progetto_prog_3.utils.Constants.Projectiles.CannonBall.*;
import java.awt.geom.Rectangle2D;
import Progetto_prog_3.Game;
import Progetto_prog_3.objects.Prototype.ProjectileInterface;

public class CannonBall extends AbstractProjectile implements ProjectileInterface {

    public CannonBall(int x, int y, int direction) {

        super(x, y, direction, CANNON_BALL);
        int yOffset = (int)( 5 * Game.SCALE);
        int xOffset = (int)(-3 * Game.SCALE);

        if (direction == 1) {
            xOffset = (int)(29 * Game.SCALE);
        }

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);

    }

    //Implementazione della funzione di clonazione, che ci permette di implementare il protootype pattern correttamente
    @Override
    public ProjectileInterface makeClone() {

        CannonBall newCannonBall = null;

        try {

            newCannonBall = (CannonBall) super.clone();
            newCannonBall.hitbox = new Rectangle2D.Float(this.hitbox.x, this.hitbox.y, this.hitbox.width, this.hitbox.height);

        } catch (CloneNotSupportedException e) {
            System.out.println("Can't clone the cannonball");
        }

        return newCannonBall;

    }
    
}
