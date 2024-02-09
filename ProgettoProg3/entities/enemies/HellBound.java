package Progetto_prog_3.entities.enemies;

import Progetto_prog_3.Game;
import Progetto_prog_3.entities.Player;
import static Progetto_prog_3.utils.Constants.GRAVITY;
import static Progetto_prog_3.utils.Constants.Directions.LEFT;
import static Progetto_prog_3.utils.Constants.Directions.RIGHT;
import static Progetto_prog_3.utils.Constants.EnemtConstants.getSpriteAmount;
import static Progetto_prog_3.utils.Constants.EnemtConstants.HellBound.*;
import static Progetto_prog_3.utils.HelpMetods.*;

import java.awt.geom.Rectangle2D;

public class HellBound extends AbstractEnemy{

    private float jumpForce = -1.5f * Game.SCALE;
    private float orizzontalSpeed = 2f * Game.SCALE, slidingSpeed = 1.5f * Game.SCALE;
    private float slideDistanceTravelled = 0;
    private boolean jumping = false;

    public HellBound(float x, float y) {
        super(x, y, HELL_BOUND_WIDTH, HELL_BOUND_HEIGHT, HELL_BOUND);
        initHitbox(x, y, (int)(46 * Game.SCALE), (27 * Game.SCALE));
        initattackBox();
        state = HELL_BOUND_WALK;
    }

    private void initattackBox() {
        
        attackBox = new Rectangle2D.Float(x, y, (int)(HELL_BOUND_DEAFULT_WIDTH/3), HELL_BOUND_DEAFULT_HEIGHT + (int)(3 * Game.SCALE));
    
    }
    
	@Override
    public void update(int[][] levelData, Player player) {
        
        if (active) {
            act(levelData, player);
            updateAttackBoxDirection();

            if (jumping && aniIndex == 4 && state != HELL_BOUND_DIE) {
                //Skip, non viene aggiornato il frame di caduta altrimenti si passa allo stato di walk mentre è in volo
                //Magari si è riusciti a farlo cadere in un burrone ed in tal caso l'animazione deve fermarsi oppure
                //il cambiamenti di stato farebbe camminare il cane in aria e causerebbe un bug

                //Se non sta saltando o in tutti gli altri casi fa l'update del frame di animazione
            } else updateAnimationTick();
        }

        if (state == HELL_BOUND_DIE && aniIndex == getSpriteAmount(enemyType, state) - 1) {
            active = false;
        }
    }


    //Implementazione del metodo makeMovement utilizzato nel template pattern
    @Override
    public void makeMovement(int[][] levelData, Player player) {

        switch (state) {
                    
            case HELL_BOUND_WALK:
                attackChecked = false;    
                this.walkSpeed = 0.4f;
                aniSpeed = 17;

                if (canSeePlayer(levelData, player)) {
                    turnTowardsPlayer(player);
                    newState(HELL_BOUND_RUN);
                }
                
                move(levelData);
                break;

            case HELL_BOUND_RUN:
                this.walkSpeed = 1.8f;
                
                if (isPlayerCloseForAttack(player)) {
                    newState(HELL_BOUND_JUMP);
                }
                
                move(levelData);
                break;

            case HELL_BOUND_JUMP:
                aniSpeed = 15; 
                jumping = true;
                if(jumping)jumpAttack(levelData);

                //La variabile attackChecked identifica se l'attacco è stato eseguito
                //Nel primo momento in cui la attackbox del nemico collide con la hitbox del player
                //A questo viene applicato il danno e viene sambiato lo stato della flag di attavvo a true
                //Segnalango che l'attacco è stato eseguito, non ne verranno fatti altri ad ogni tick di agiornamento 
                if(!attackChecked) checkEnemyHit(attackBox, player);    
                break;
            
            case HELL_BOUND_SLIDE:
                slide(levelData);
                break;

            case HELL_BOUND_HIT:
                
                //Se il cane viene colpito a mezz'aria prima che colpisca il player, questo non applicherà il danno al player
                if (jumping) {
                    updateInAir(levelData);
                    jumping = false;
                    attackChecked = true;
                }
                break;

            case HELL_BOUND_DIE: 
                aniSpeed = 20;

        }
        
    }

    /*
     * JUMP ATTACK
     * questo attacco permette all'hellbound di saltare per attaccare il player. 
     * Vengono fatti due calcoli in modo parallelo, si agiorna la componente orizzontale e quella verticale
     * Per la direzione destra o sinistra viene fatto un controllo sul possibile movimento, 
     * viene aggiunta una componente alla coordinata se non ci sono muri in traiettoria, 
     * in caso contrario il cagnetto darà una bella testata al muro, ma nel frattemo la componente in Y
     * continuerà ad essere aggiornata fin qando il terreno soto i suoi piedi è aria
     */
    private void jumpAttack(int[][] levelData) {
        
        //Controlli per il movimento orizzontale
        if (wlakDir == RIGHT && canMoveHere(hitbox.x + orizzontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += orizzontalSpeed;
        } else if (wlakDir == LEFT && canMoveHere(hitbox.x - orizzontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x -= orizzontalSpeed;
        }
        
        //Controlli per il salto in alto, se sta salendo non ci interessa di fare altri controlli, soffitti così bassi non sono presenti in gioco
        if (jumpForce <= 0 ) {
            hitbox.y +=jumpForce;
            jumpForce += GRAVITY;
            return;
        //Controlli per il salto in basso, in questo caso il pavimento può essere presente e viene agiunto un ulteriore controllo
        //Questo può fallire ed in tal caso il RETURN non viene effettuato, andando ad eseguire i controlli aggiuntivi nella parte successiva
        } else if (jumpForce > 0 && canMoveHere(hitbox.x, hitbox.y + jumpForce, hitbox.width, hitbox.height, levelData)) {
            hitbox.y +=jumpForce;
            jumpForce += GRAVITY;
            return;
        }

        //viene resettato il valore del salto
        jumpForce = -1.5f * Game.SCALE;
        //E si riposiziona il cagnetto in relazione al terreno
        updateInAir(levelData);

        //Se il nemico si trova sul pavimento, lo stato di attacco viene spento 
        //e si resetta il loop di movimento reimpostando lo stato su WALK
        if (isEntityOnFloor(hitbox, levelData)) {
            jumping = false;
            orizzontalSpeed = 2f * Game.SCALE;
            newState(HELL_BOUND_SLIDE);
        }

    }

    /*
     * Quando il cagnetto atterra sul terreno, conserva la forza del salto ed inizierà a scivolare sul terreno prima di riassestarsi
     * Questa funzione serve a questo, fa scivolare il cane, non oltre i bordi però, era troppo complicato programmarlo
     * Vi è una flag che indica se è stata raggiunta la distanza massima del salto, che quando diventa true, permette al
     * loop del nemico di ricominciare da capo
     * 
     * Se la direzione è destra viene fatto lo slide a destra, altrimenti viene fatto a sinistra,
     * la funzione move slide agiorna la posizione in x con una decrescenza costante
     */
    private void slide(int[][] levelData){

        boolean maxDistance = false;
        //Controlli sulla direzione, e slide in quella direzione
        if (wlakDir == LEFT && isSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)) {
                maxDistance = moveSlide(levelData, -slidingSpeed);
        } else if(wlakDir == RIGHT && isSolid(hitbox.x + hitbox.width + 1, hitbox.y + hitbox.height + 1, levelData)){
                maxDistance = moveSlide(levelData, slidingSpeed);
        }

        //Se lo slide ha raggiunto la lunghezza massima viene segnalato e viene interrotto lo stato riportandolo a quell odi partenza
        if (maxDistance) {
            this.slidingSpeed = 1.5f * Game.SCALE;
            this.slideDistanceTravelled = 0;
            changeWalkDir();
            newState(HELL_BOUND_WALK);
        }

    }

    //La funzione moveSlide sposta il cane con velocità decrescente lineare
    private boolean moveSlide(int[][] levelData, float slidingSpeed){

        //Se non si trova sul bordo massimo viene fatto un agiornamento sulla posizione
        if (canMoveHere(hitbox.x + slidingSpeed, hitbox.y, hitbox.width, hitbox.height , levelData)) {
            hitbox.x += slidingSpeed;
            this.slidingSpeed -= 0.0115f * Game.SCALE;
            this.slideDistanceTravelled += Math.abs(slidingSpeed);
            
            //Se dopo l'aggiornamento viene trovato che la energia cinetica sio è azzerata oppure che ha slidato per 4 blocchi
            //Si ritorna true per segnalare che è stata raggiunta la distanza massima
            if (slideDistanceTravelled > Game.TILES_SIZE * 4 || Math.abs(slidingSpeed) <= 0.3f) {
               return true;
            }
        }

        //In tutti gli altri casi ritorna falso
        return false;
    }

    //Funzione per modificare la direzione della attackbox in base alla dierzione del movimento
    private void updateAttackBoxDirection() {
        if (wlakDir == LEFT) {
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y; 
        } else {
            attackBox.x = hitbox.x + (int)(hitbox.width - attackBox.width);
            attackBox.y = hitbox.y;
        }
    }

    //Questo metodo ci poermette di causare danno ad un nemico se questo viene colpito dal player
    @Override
    public void hurt(int amount){

        currentHealth -= amount;

        if (currentHealth <= 0) {
            jumping = false;
            newState(HELL_BOUND_DIE);
        } else {
            newState(HELL_BOUND_HIT);
            attackChecked = true;
        }
        
    }

    @Override
    public int flipX() {
        if (wlakDir == LEFT) {
            return 0;
        } else{
            return hitBoxWidth;
        }
    }

    @Override
    public int flipW() {
        if (wlakDir == LEFT) {
            return 1;
        } else { 
            return -1;
        }
    }

    @Override
    public int flipXP(Player player) {
        return 0;
    }

    @Override
    public int flipWP(Player player) {
        return 0;
    }

    


}
