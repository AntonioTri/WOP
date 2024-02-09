package Progetto_prog_3.objects;

import static Progetto_prog_3.utils.Constants.ObjectConstants.*;
import Progetto_prog_3.Game;

public class LootBox extends AbstractObject {

    //Questa variabile serve a far spawnare una sola pozione per cassa, evitando che il clock di memoria
    //Che mentre va in loop per controllare le altre box, spawni un numero di pozioni enorme prima che venghi 
    //settato lo stato di questa come NON ATTIVA
    private boolean canSpawnPotion = true;

    public LootBox(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    private void createHitBox(){

        if (objType == BOX) {
            initHitbox(25, 18);
            xDrawOffset = (int)(7 * Game.SCALE);
            yDrawOffset = (int)(12 * Game.SCALE);
        } else {
            initHitbox(23, 25);
            xDrawOffset = (int)(8 * Game.SCALE);
            yDrawOffset = (int)(5 * Game.SCALE);

        }

        //Queste due righe servono a spostare il rendering della hitbox e della immagine dall'angolo in alto a sinistra
        //Del blocco nel quale vengono disegnate alla base di esso, ecco il perchè della y, ed al centro di esso, motivo della modifica alla x
        hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);
        hitbox.x += xDrawOffset / 2;

    }

    //L'override del metodo reset serve poichè troviamo una variabile in più da dover resettare
    @Override
    public void reset(){

        aniIndex = 0;
        aniTick = 0;
        active = true;
        canSpawnPotion = true;

        if (objType == BARREL || objType == BOX) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    //Nella funzione update, solo se la flag di animazione viene impostata ad attiva viene eseguita l'animazione
    //La logica applicata è che solo se colpita, una cassa dovrebbe fare l'animaizone di rottura per poi smettere
    public void update(){
        if (doAnimation) {
            updateAnimationTick();
        }
    }

    //Getters e setters
    public void setAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    public boolean getCanSpawnPotion() {
        return canSpawnPotion;
    }

    public void setCanSpawnPotion(boolean canSpawnPotion) {
        this.canSpawnPotion = canSpawnPotion;
    }

}