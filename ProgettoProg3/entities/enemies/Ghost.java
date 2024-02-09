package Progetto_prog_3.entities.enemies;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Progetto_prog_3.Game;
import Progetto_prog_3.entities.Player;
import Progetto_prog_3.utils.Constants.EnemtConstants;
import static Progetto_prog_3.utils.HelpMetods.isTileSolid;
import static Progetto_prog_3.utils.Constants.EnemtConstants.Ghost.*;
import static Progetto_prog_3.utils.Constants.EnemtConstants.getEnemyDamage;
import static Progetto_prog_3.utils.Constants.EnemtConstants.getSpriteAmount;

public class Ghost extends AbstractEnemy {

    //Variabili d abimente
    int attackBoxOffset = (int)(125 * Game.SCALE);
    int teleportTimer = 0, timeToTeleport = 1000, attackTimer = 0;
    private List<Point2D> spawnPoints = new ArrayList<>(); 
    private boolean canTeleport = true, firstSpawn = true;
    Random random = new Random();

    public Ghost(float x, float y, int[][] levelData) {

        super(x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST);

        initHitbox(x, y, (int)(24 * Game.SCALE), (int)(47 * Game.SCALE));
        initAttackBox();

        spawnPoints = new ArrayList<>();
        this.state = EnemtConstants.Ghost.GHOST_NOT_SPAWNED;
        
    }

    //La attackbox di questo nemico è quadrata e molto ampia, applica un debuff ad area colpendo il player anche da dietro i muri
    private void initAttackBox() {
        int attackboxLenght = (int)(250 * Game.SCALE);
        circularAttackbox = new Ellipse2D.Float(hitbox.x + hitbox.width/2, hitbox.y + hitbox.height/2 , attackboxLenght, attackboxLenght);
    }
    
    private void updateAttackBox() {
        circularAttackbox.x = hitbox.x - attackBoxOffset + hitbox.width/2;
        circularAttackbox.y = hitbox.y - attackBoxOffset + hitbox.height/2;
    }

    @Override
    public void update(int[][] levelData, Player player) {
        
        if (active) {
            act(levelData, player);
            updateAttackBox();
            flipXP(player);
            flipWP(player);
        }

        updateAnimationTick();

    }


    //La funzione makeMoovement qui viene implementata in modo particolare, il ghost non si muove, si teletrasporta
    //Questa funzione serve a gestire il suo movimento peculiare andando a gestire i suoi stati 
    //con flag e speciali funzioni dedicate/Overloadate
    @Override
    public void makeMovement(int[][] levelData, Player player) {
        switch (state) {

            case EnemtConstants.Ghost.GHOST_NOT_SPAWNED:
                if (isPlayerInRangeOfVision(player)) {
                    newState(GHOST_SPAWN);
                }
                break;

            case GHOST_SPAWN:
                //Se può spawnare viene eseguito il teletrasporto
                if (canTeleport && !firstSpawn) {
                    //Si blocca l'entrata a questa funzione fino a quando non riavverà l'animazione di teletrasporto
                    canTeleport = false;
                    teleport(levelData);
                } 
                //Dopo il primo spawn questa flag rimane su false
                //ed ogni nuovo spawn del ciclo farà spawnare il ghost in una nuova posizione
                break;
            
            //Nel caso di ritorno in stato di Idle, viene settata l'invulnerabilità guadagnata durante il teletrasporto a falso
            case GHOST_IDLE:
                //L'attack timer serve a non far spammare l'attacco dal ghost, comunque è un attacco ad area molto potente,
                //Ok la difficoltà ma teniamolo bilanciato
                attackTimer++;
                aniSpeed = 20;
                invulnerability = false;
                attackChecked = false;
                updateTeleportTick();
                //Se è passato abbastanza tempo viene eseguito l'attacco
                if(attackTimer >= 300 && isPlayerCloseForAttack(player)){
                    attackTimer = 0;
                    newState(GHOST_ATTACK);
                }
                break;
            
            //Il timer del teletrasporto non si ferma nemmeno quando il ghost attacca
            case GHOST_ATTACK:
                aniSpeed = 17;
                updateTeleportTick();
                //Come al solito, la funzione per applicare il danno viene bloccata da se stessa durante la prima esecusione
                //Quando si ritorna nello stato di idle per poi rifare i controlli di attackdistance, viene resettata a falso
                //Per permettere di fare danno nel prosimo attacco
                if(!attackChecked && aniIndex >= 4) checkEnemyHitEllipse(circularAttackbox, player);
                break;
                
            case GHOST_HIT:
                aniSpeed = 25;
                break;

            case GHOST_DIE:
                aniSpeed = 25;
                
            default:
                break;
        }
    }
    

    //La funzione teleport definisce un nuovo punto di spawn quando viene richiamata
    //settando poi la posizione del Ghost in uno dei punti si spawn possibili trovati
    private void teleport(int[][] levelData) {

        //Trova i punti di spawn
        findTeleportPoints(levelData);
        //Genera un numero randomico in base ai punti di spawn trovati
        int randomSP = random.nextInt(spawnPoints.size());

        //Imposta la posizione del ghost nel nuovo punto scelto
        hitbox.x = (float)spawnPoints.get(randomSP).getX();
        hitbox.y = (float)spawnPoints.get(randomSP).getY();

        //La lista vine pulita per evitare spreco di memoria
        spawnPoints.clear();

    }

    /*
     * Quello che viene fato in questo metodo è trovare i punti si spawn in un raggio di azione attorno al Ghost
     * Per evitaare di dover prima calcolare tutti i possibili punti di spawn e poi fare una selezione dei possibili
     * Facciamo direttamente un calcolo dei possibili, nell'evenienza ci siano livello enormi, per evitare di fare calcoli 
     * lunghi ed inutili
     */
    private void findTeleportPoints(int[][] levelData) {

        //si trova l'attuale blocco di partenza del Ghost
        int Xposition = (int)(hitbox.x / Game.TILES_SIZE);
        int Yposition = (int)(hitbox.y / Game.TILES_SIZE);

        //Tramite questi si calcola il massimo angolo in alto a sinistra ...
        int Xstart = Xposition - 10;
        int Ystart = Yposition - 10;

        //...ed il massimo angolo in basso a destra
        int Xend = Xposition + 10;
        int Yend = Yposition + 10;

        //Questi 4 controlli impediscono ai punti di inizio ed di fine ricerca di sbordare dal level data
        if (Xstart <= 0 ) Xstart = 0;
        if (Ystart <= 0 ) Ystart = 0;
        if (Xend >= levelData[0].length) Xend = levelData[0].length - 1;
        if (Yend >= levelData.length   ) Yend = levelData.length - 1;

        //Scorriamo il level data e controlliamo se i blocchi in questa zona quadrata attorno al ghost siano solidi
        for (int i = Xstart; i < Xend; i++) {
            for (int j = Ystart; j < Yend; j++) {
                //Se il blocco in questione è solido vengono calcolate le posizioni in x ed y scalate 
                //e vengono inserite nella lista di punti di spawn possibili
                if (isTileSolid(i, j, levelData) && !isTileSolid(i, j - 1, levelData) && !isTileSolid(i, j - 2, levelData)) {

                    Point2D possibleSpawnPoint = new Point2D.Float();
                    possibleSpawnPoint.setLocation(i * Game.TILES_SIZE + (Game.TILES_SIZE - hitbox.width)/2,
                                                   j * Game.TILES_SIZE - hitbox.height - 1);
                    spawnPoints.add((Point2D.Float)possibleSpawnPoint);

                }
            }
        }

    }

    

    //Viene eseguito l'update dei tick per il teletrasporto, è reso randomico tra un minimo di 700 tick ed un massimo di 1000
    //Quando il valore dei tick ha raggiunto la soglia di tempo massima, viene eseguito lo stato di teleport
    //e generato un nuovo tempo randomico per il prossimo teleport
    private void updateTeleportTick(){

        teleportTimer++;

        if (teleportTimer >= timeToTeleport) {

            teleportTimer = 0;
            timeToTeleport = random.nextInt(300) + 700;
            //Il ghost diventa invulnerabile quando si teletrasporta
            invulnerability = true;
            //Inoltre viene impedito al ghost di teletrasportarsi quando sta attaccando
            if(state != GHOST_ATTACK)newState(GHOST_TELEPORT);
        }
    }

    //Anche quì necessitiamo di un override poichè dobbiamo accedere ad una variabile di questa classe figlia
    //La variabile è first spawn
    @Override
    protected void updateAnimationTick(){
        aniTick++;
        if (aniTick >= aniSpeed) {

            aniTick = 0;
            aniIndex++;

            if (aniIndex >= getSpriteAmount(GHOST, state)) {
                
                aniIndex = 0;
                switch (state) {

                    case GHOST_HIT, GHOST_ATTACK -> state = GHOST_IDLE;
                    case GHOST_DIE -> this.active = false;
                    //Nel caso spawn viene settata una flag su falso, quella del first spawn
                    //Dato che non è mai spawnato prima, questa flag era vera e impediva il teletrasporto
                    //Una volta spawnato almeno una volta, potrà iniziare a teletrasportarsi
                    case GHOST_SPAWN -> {
                        state = GHOST_IDLE;
                        firstSpawn = false;
                    }
                    //Quest'altra flag impedice che il calcolo della posizione del teletrasporto
                    //venga fatta fuori controllo 180 volte al secondo facendo laggare il gioco
                    case GHOST_TELEPORT ->{
                        state = GHOST_SPAWN;
                        canTeleport = true;
                    }
                }
            }
        }
    }

    protected void checkEnemyHitEllipse(Ellipse2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.getHitbox()) && !player.getInvulnerability()) {
            //Il segno meno serve a mandare una somma negativa alla vita del player, non lo stiamo curando, lo stiamo picchindo
            player.changeHealth(-getEnemyDamage(enemyType));
            statusManager.applySlow(player, 2, 0.7f);
            attackChecked = true;
        }
    }

    /*
     * La funzione is in range in questo caso viene overridata perchè necessita di alcune modificeh specifiche delc caso
     * A diffrenza degli altri nemici, il ghost può vedere attraverso i muri ed attaccare attraverso di questi, pertanto
     * vengono effettuati nuovi calcoli anche sulla base della verticalità oltre che sulla orizzontalità
     */
    @Override
    protected boolean isPlayerInRangeOfVision(Player player){

        boolean isInRange = false;

        int absValueX = (int)Math.abs((player.getHitbox().x + player.getHitbox().width / 2) - (hitbox.x + hitbox.width / 2));
        int absValueY = (int)Math.abs((player.getHitbox().y - player.getHitbox().height / 2) - (hitbox.y - hitbox.height / 2));
        //Se una distanza è minore della vision distance in X o Y, viene ritornato vero, altrimenti falso
        if (absValueX <= visionDistance && absValueY <= visionDistance) {
            isInRange = true;
        }

        return isInRange;
    }

    /*
     * La funzione is in range in questo caso viene overridata perchè necessita di alcune modificeh specifiche delc caso
     * A diffrenza degli altri nemici, il ghost può vedere attraverso i muri ed attaccare attraverso di questi, pertanto
     * vengono effettuati nuovi calcoli anche sulla base della verticalità oltre che sulla orizzontalità
     */
    @Override
    protected boolean isPlayerCloseForAttack(Player player){

        boolean isInRange = false;

        int absValueX = (int)Math.abs((player.getHitbox().x + player.getHitbox().width / 2) - (hitbox.x + hitbox.width / 2));
        int absValueY = (int)Math.abs((player.getHitbox().y - player.getHitbox().height / 2) - (hitbox.y - hitbox.height / 2));
        //Se una distanza è minore della ATTACK distance in X o Y, viene ritornato vero, altrimenti falso
        if (absValueX <= attackDistance && absValueY <= attackDistance) {
            isInRange = true;
        }

        return isInRange;
    }

    @Override
    public void hurt(int amount){

        currentHealth -= amount;

        if (currentHealth <= 0) {
            newState(GHOST_DIE);
        } else {
            newState(GHOST_HIT);
        }
        
    }


    //Anche le funzioni di flip per il rendering sono peculiari, il ghost non si muove, si teletrasporta, quindi viene
    //Semlicemente generato un offset per la flipX ed un moltiplicatore per la flipW in base alla posizione in x del player
    @Override
    public int flipXP(Player player) {
        if (player.getHitbox().x + player.getHitbox().width/2 > hitbox.x + hitbox.width/2) {
            return (int)(GHOST_WIDTH + 5 * Game.SCALE);
        } else return 0;
    }
    @Override
    public int flipWP(Player player) {
        if (player.getHitbox().x + player.getHitbox().width/2 > hitbox.x + hitbox.width/2) {
            return -1;
        } else return 1;
    }

    @Override
    public int flipX() {
       return 0;
    }

    @Override
    public int flipW() {
        return 0;
    }

    


    
}



            
