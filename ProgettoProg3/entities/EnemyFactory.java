package Progetto_prog_3.entities;

import static Progetto_prog_3.utils.Constants.EnemtConstants.Ghost.GHOST;
import static Progetto_prog_3.utils.Constants.EnemtConstants.HellBound.HELL_BOUND;
import static Progetto_prog_3.utils.Constants.EnemtConstants.NightBorne.NIGHT_BORNE;
import Progetto_prog_3.entities.enemies.AbstractEnemy;
import Progetto_prog_3.entities.enemies.Ghost;
import Progetto_prog_3.entities.enemies.HellBound;
import Progetto_prog_3.entities.enemies.NightBorne;

public class EnemyFactory {
    
    public AbstractEnemy MakeEnemy(int enemyType, float x, float y, int[][]levelData){

        switch (enemyType) {

            case NIGHT_BORNE: return new NightBorne(x, y);
            case HELL_BOUND: return new HellBound(x, y);
            case GHOST: return new Ghost(x, y, levelData);

            default: return null;

        }

    }
}
