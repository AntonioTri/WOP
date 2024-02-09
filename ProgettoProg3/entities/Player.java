package Progetto_prog_3.entities;

import static Progetto_prog_3.utils.Constants.PlayerConstants.*;
import static Progetto_prog_3.utils.HelpMetods.*;
import static Progetto_prog_3.utils.Constants.GRAVITY;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import Progetto_prog_3.Game;
import Progetto_prog_3.Audio.AudioPlayer;
import Progetto_prog_3.GameStates.Playing;
import Progetto_prog_3.entities.MementoSavings.Memento;
import Progetto_prog_3.entities.MementoSavings.PlayerMemento;
import Progetto_prog_3.utils.LoadSave;

public class Player extends Entity {

    //Variabili per la gestione dei frame
    private int aniSpeed = 15;

    //Variabile per definire l'azione del player
    private boolean left, right, up, down, jump;
    private boolean moving = false, attacking = false;

    //Variabili per la memorizzazione di frame
    private BufferedImage[][] animations;
    private int[][] levelData;

    //Variabili per le hitbox
    private float XOffset = 12 * Game.SCALE;
    private float YOffset = 25 * Game.SCALE;

    //Variabili per il salto
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    // StatusBarUI
	private BufferedImage statusBarImg;
    //Ultimate
    private BufferedImage[] ultimate;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

    //Variabili per definire la health bar
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	private int healthWidth = healthBarWidth;

    //Variabili per definire la power bar
    private int powerBarWidth = (int) (104 * Game.SCALE);
	private int powerBarHeight = (int) (2 * Game.SCALE);
	private int powerBarXStart = (int) (44 * Game.SCALE);
	private int powerBarYStart = (int) (34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;

	private int flipX = 0;
	private int flipW = 1;

    //Posizione verticale
    private int tyleY = 0;

    //Variabili per l'abilità speciale e per gli attacchi normali
    private boolean powerAttackActive, ultimateActive;
    private int powerAttackTick, powerGrowSpeed = 15, powerGrowTick;
    // private int ultimateTick, ultimateGrowSpeed = 15, ultimateGrowTick;
	private boolean attackChecked, canPlayAttackSound, hurted = false;
    
    
    private Rectangle2D.Float ultimateAttackBox;
    private int damage = 5;
    private Playing playing;
    

    //Costruttore richiamante la classe estesa
    public Player(float x, float y, int width, int height, Playing playing){
        super(x, y, width, height);
        this.playing = playing;
        initStates();
        loadAnimations();
        initHitbox(x, y, 25 * Game.SCALE, 31 * Game.SCALE);
        initAttackBoxes();
    }

    private void initStates(){
        this.walkSpeed = 1.7f;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.invulnerability = false;
    }

    private void initAttackBoxes(){
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(30 * Game.SCALE));
        ultimateAttackBox = new Rectangle2D.Float(x, y, (int)(PLAYER_EXPLOSION_WIDTH * 0.85 ), (int)(PLAYER_EXPLOSION_HEIGHT * 0.85));
    }

    //funzione per fare l'update delle caratterisctiche del personaggio
    public void update(){

        //Vengono aggiornate la vita e la barra delle abilità
        updateHealthBar();
        updatePowerBar();

        //Se il player è appena stato ucciso avveengono alcune cose
        if (currentHealth <= 0) {
            
            //Viene impostato il suo stato in quello di morte e vengono impostati i valori di animazione corretti
            if (state != DIE) {
                state = DIE;
                aniSpeed = 50;
                aniTick = 0;
                aniIndex = 0;                
                playing.setPlayerDying(true);
                
                //A momento in cui l'animazione finisce, si mette in sleep il gioco peer dare un effetto pathos e si invia un seegnale 
                //Alla classe playing peer impostar ilGameState a Deathscreen
            } else if((aniIndex == getSpriteAmount(DIE) - 1) && (aniTick >= aniSpeed - 1)){

                //Questo trry catch con uno sleep serve a rirtardare la comparsa del menù di deathScreen
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                //Si imposta il gioco neello stato di GameOver
                playing.setGameOver(true);
                //SFX
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAME_OVER);
        
            //in tutti gli altri casi si manda avanti l'animazione e si ritorna
            } else updateAnimationTick();    
        
                return;
        
        }

        updateAttackBox();
        updatePosition();

        //Se il player si sta muovendo può interagire con gli oggetti della mappa
        if (moving) {

            checkPotionTouched();
            checkSpikesTouched();

            tyleY = (int)((hitbox.y + hitbox.height - 1) / Game.TILES_SIZE);

            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }


        }

        //Se il player ha la flag di attacco o di ultimate attiva, e non sta subendo danno, viene fatto il controllo sull'attacco
        if ((attacking || ultimateActive)  && state != HURT) checkAttack();
        
        updateAnimationTick();
        setAnimation();
        
    }

        
    //Questa funzione fa avanzare il frame di animazione del personaggio ogni 40 tick del programma
    //Se l'indice diventa magiore del numero di frame viene ripristinato a 0 e si riparte da capo
    private void updateAnimationTick() {

        aniTick++;
        if (aniTick >= aniSpeed) {

            aniTick = 0;
            aniIndex++;

            if (aniIndex >= getSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
                ultimateActive = false;
                hurted = false;
            }
        }
    }

    //In questa funzione decidiamo la posizione della attackbox in base al movimento del giocatore e relativamente alla posizione dello stesso    
    private void updateAttackBox() {

        if (right || (powerAttackActive && flipW == 1)) {
            attackBox.x = hitbox.x + hitbox.width;

        } else if (left || (powerAttackActive && flipW == -1)) {
            attackBox.x = hitbox.x - hitbox.width + (int)(Game.SCALE * 4);

        }

        attackBox.y = hitbox.y + (Game.SCALE * 1);

        ultimateAttackBox.x = hitbox.x + hitbox.width - PLAYER_EXPLOSION_WIDTH/2;
        ultimateAttackBox.y = hitbox.y + hitbox.height - PLAYER_EXPLOSION_HEIGHT/2;
    }

    //Dato che il programma viene refreshato 120 volte al secondo dato il game loop, aniIndex verrà modificato 
    //mano mano che avanzano i tick di gioco e verra' quindi mostrata una immagine differente ogni 40 tick
    public void render(Graphics g, int xLevelOffset, int yLevelOffset){

        //In questa draw vi scorrono diverse logiche, la prima è la scelta dello sprite da utilizzare
        //La seconda è che l'immagine viene disegnata con uno spostamento
        //La terza è che  vengono aggiunte variabili di "flip", se il personagio cammina verso destra è tutto apposto
        //Se va verso sinistra, l'immagine viene mostrata riflessa al contrario rispetto al suo asse y, e per ovviare a questo problema
        //Le viene sommato un offset agiuntivo per spostarla di nuovo nella posizione corretta 
        

        g.drawImage(animations[state][aniIndex], 
                        (int)(hitbox.x - XOffset) - xLevelOffset + flipX, 
                        (int)(hitbox.y - YOffset) - yLevelOffset, 
                        hitBoxWidth * flipW, hitBoxHeight - (int)(3 * Game.SCALE), null);
        

    
        if (state == USING_ULTIMATE){
            g.drawImage(ultimate[aniIndex], 
                    (int)( hitbox.x - hitbox.width * 1.1 - xLevelOffset - PLAYER_EXPLOSION_WIDTH/2),
                    (int)( hitbox.y - hitbox.height/ 1.1 - yLevelOffset - PLAYER_EXPLOSION_HEIGHT/2),
                    PLAYER_EXPLOSION_DRAW_WIDTH, PLAYER_EXPLOSION_DRAW_HEIGHT, null);

        }

        //Queste due funzioni possono essere decommentate per mostrare la posizione delle aree di collisione
        //drawHitbox(g, xLevelOffset, yLevelOffset);
        //drowAttackBox(g, xLevelOffset, yLevelOffset);
        
        drawUI(g);


    }

    @Override
    public void drowAttackBox(Graphics g, int levelOffsetX, int yLevelOffset) {
        g.setColor(Color.RED);
        g.drawRect((int)attackBox.x - levelOffsetX, (int)attackBox.y - yLevelOffset, (int)attackBox.width, (int)attackBox.height);

        g.setColor(Color.GREEN);
        g.drawRect((int)ultimateAttackBox.x - levelOffsetX, (int)ultimateAttackBox.y - yLevelOffset, (int)ultimateAttackBox.width, (int)ultimateAttackBox.height);
    }

    //Disegna la UI di gioco, per ora soltanto la health bar e la power bar
    private void drawUI(Graphics g) {

        //Si disegna la gui per lo stato della vita e dei punti abilità 
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        //Si disegna la barra della vita
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        //Si disegna la barra dei punti abilità
        g.setColor(Color.YELLOW);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    private void updatePowerBar(){
        powerWidth = (int)((powerValue / (float)powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    //Viene fatto un controllo ogni volta che il player attacca per vdere se sta colpendo un nemico
    //in tal caso deve applicare i danni a quel nemico
    private void checkAttack() {

        if (!attackChecked && aniIndex != 1) {
            return;
        }

        //Viene fatto un controllo 
        if (!ultimateActive) this.damage = 5; 

        //Viene settato l'attacco a true se il controllo precedente fallisce ( vuol dire che si sta attaccando perchè la flag è !vera ),
        //il game loop lo resetterebbe a falso in tutti i casi con una velocità quasi istantanea, allora serve tenerlo a vero per
        //evitare che la chiamata di questa funzione ritorni nel'update successivo
        attackChecked = true;
        
        //Viene fatto il controllo sul danno solo quando l'animazione si trova in un certo indice
        if (aniIndex == 1 && !ultimateActive){
            playing.checkPlayerHitEnemy(attackBox, 0);
            playing.checkObjectHit(attackBox, 0);
            attackChecked = false;
            
        }

        if (aniIndex >= 7 && aniIndex <= 13 && ultimateActive) {
            playing.checkPlayerHitEnemy(ultimateAttackBox, 1);
            playing.checkObjectHit(ultimateAttackBox, 1);
            attackChecked = false;
        }

    }

    //Questo metodo ci permette di usare la ultimate ability, se il player ha tutta l'energia viene tolta tutta
    //e successivamente eseguita la ultimate, se questa è già attiva viene impedito di riutilizzarla
    public void ultimateAbility(){
        if (ultimateActive) {
            return;
        }
        if (powerValue == powerMaxValue) {

            ultimateActive = true;
            aniTick = 0;
            aniIndex = 0;

            this.damage = 20;

            changePower(-powerMaxValue);
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_EXPLOSION);

        }
    }

    //Metodo che esegue il dash, ne viene fatto uno solo se si ha abbastanza energia
    public void dash() {
        if (powerAttackActive) {
            return;
        }
        if (powerValue > 60) {
            powerAttackActive = true;
            changePower(-60);
            playing.getGame().getAudioPlayer().playSetOfEffect(AudioPlayer.PLAYER_DASHING);
        }
    }
    
    //Funzione che controlla se il player sta toccando una pozione
    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    //Funzione che controlla se il player sta toccando una Spina
    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    //Qui viene settata l'animazione in base agli imput del giocatore, per ogni azione viene settata una velocità di animazione unica
    private void setAnimation() {
        
        int startAnimation = state;
        
        playing.getGame().getAudioPlayer().playWalkingSound(moving, inAir, currentHealth);
        
        //Per ogni flag che viene attivata, uno stato viene impostato, ad ogni stato corrisponde una velocità di animazione diversa
        if (moving) {
            aniSpeed = 15;
            state = RUNNING;

        } else {
            aniSpeed = 20;
            state = IDLE;
        }

        if (hurted || gainingKnockack) {
            aniSpeed = 20;
            state = HURT;
            //Se il player viene colpito con il return viene impedito di fare qualsiasi
            //azione sottostante
            return;
        }

        if (inAir) {
            if (airSpeed<0) {
                state = JUMPING_UP;
            } else {
                state = JUMPING_DOWN;
            }
        }

        if (ultimateActive) {
            state = USING_ULTIMATE;
            aniSpeed = 23;
            return;
        }
        
        if (powerAttackActive) {
            state = RUNNING;
            aniIndex = 0;
            aniTick = 0;
            return;
        }

        
        if (attacking && state != HURT) {
            aniSpeed = 14;
            state = LIGHT_ATTACK;

            //Per evitare che venga eseguito ad ogni tick dell'animazione dell'attacco il sound effect di questo
            //c'è una flg che funge da switch che blocca l'ingresso alla funzione, dentro di questa viene passato player
            //In modo che quando il suono sia finito, la flag venga riportata a true
            if (canPlayAttackSound) {
                canPlayAttackSound = false;
                playing.getGame().getAudioPlayer().playattack();
            } else {
                playing.getGame().getAudioPlayer().resetPlayerSoundBools(this);
            }

        }

        /*Se la animazione di arrivo e' diversa dalla animazione di fine funzione
            allora si e' creato un cambiamento di stato e vengono resettati i valori
            di scelta fotogramma e di tick di animazione per permettere alla animazione
            di incominciare dall'inizio e di non fare glitch strani
        */
        if (startAnimation != state) {
            resetAnimationTick();
        }

    }

    //Vengono impostati i valori dell'animazione che deve essere eseguita a 0
    private void resetAnimationTick() {
        aniIndex = 0;
        aniTick = 0;
    }

    //Ancora, all'interno di questa funzione viene gestito il movimento, impedendo quelli
    //concorrenti
    private void updatePosition() {
		moving = false;

        //Salto
		if (jump && state != HURT) jump();
    
        //Impedimento di movimenti e azioni concorrenti
        if ( ultimateActive || (!inAir && !powerAttackActive && ((!left && !right) || (right && left)))) {
            return;
        }

        //Cambi del movimento destra e sinistra, si aggiunge una quantità alla velocità
        //e vengono settate le variabili per girare l'immagine, per far voltare il player nella direzione della camminata
		float xSpeed = 0;

        //Questi due if servono a settare delle variabili oltre che al movimento anche al modo in cui vengono disegnati gli sprite
        //Variabili che poi vengono utilizzata funzione draw come addendi o moltiplicatori per flipare le immagini e riposizionarle sull'asse giusto
        //Se il player sta attaccando gli viene impedito il moovimento 
		if (left && !right && !attacking && !gainingKnockack){
			xSpeed -= walkSpeed;
            flipX = hitBoxWidth - (int)(22.5f * Game.SCALE);
            flipW = -1;
        }
        
        if (right && !left && !attacking && !gainingKnockack){
			xSpeed += walkSpeed;
            flipX = (int)(8 * Game.SCALE);
            flipW = 1;
        }

        if (powerAttackActive) {
            if (!left && ! right) {
                if (flipW == -1) {
                    xSpeed = -walkSpeed;
                } else {
                    xSpeed = walkSpeed;
                }
            }

            xSpeed *=3;
        }

		if (!inAir){
			if (!isEntityOnFloor(hitbox, levelData) && !gainingKnockack){
				inAir = true;
            }
        }


		if (inAir && !powerAttackActive) {
			if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			
            } else {
                
                playing.getGame().getAudioPlayer().playSetOfEffect(AudioPlayer.PLAYER_LANDING);

				hitbox.y = getEntityYPosFloorRoofRelative(hitbox, airSpeed);
                if (airSpeed > 0) resetInAir();
				else airSpeed = fallSpeedAfterCollision;
				
                updateXPos(xSpeed);
			}

		} else updateXPos(xSpeed);
        
		moving = true;
	}

    private void updateXPos(float xSpeed){

        if (!gainingKnockack) {
            if (canMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
                hitbox.x += xSpeed;
            } else {
                hitbox.x = getEntityXPosNextWall(hitbox, xSpeed);
                if (powerAttackActive) {
                    powerAttackActive = false;
                    powerAttackTick = 0;
                }
            }
        }
    }

    //Metodo che ci permette di modifdicare il valore degli HP e sotto quello che ci permette di cambiare valore agli AP( Ability Points )
    public void changeHealth(int value){

        currentHealth += value;
        //Vengono dati dei frame di invulnerabilità dopo essere stati colpiti
        //Viene eseguito il suono del danno del player
        if (value <= 0) {
            hurted = true;
            playing.getGame().getAudioPlayer().playSetOfEffect(AudioPlayer.PLAYER_HURT);
            getStatusManager().giveInvulnerability(this, 1f);
        }

        if (currentHealth <= 0) {
            currentHealth = 0;
            //GAME OVER HAPPENS

        } else if (currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }

    } 

    public void changePower(int value) {
        powerValue += value;
        
        if (powerValue >= powerMaxValue) {
            powerValue = powerMaxValue;
        } else if (powerValue <= 0) {
            powerValue = 0;
        }

    }

    public void burn(int value){

        currentHealth +=value;
        if (currentHealth <=0) {
            currentHealth = 0;
        }

    }

    //Questo metodo ci serve a resettare tutte le caratteristiche del giocatore se ne si trova il bisogno
    public void resetAll() {

        resetMovement();
        inAir = true;
        attacking = false;
        moving = false;
        state = IDLE;

        //Tramite il pattern del memento vengono resettate le posizioni e la vita di partenza del livello
        int levelIndex = playing.getLevelManager().getLevelIndex();
        restoreState(playing.getMementoManager().getPlayerMemento(levelIndex));

        if (!isEntityOnFloor(hitbox, levelData)) {
            inAir = true;
        }

    }
    
    public void restoreState(PlayerMemento playerMemento){

        this.hitbox.x = playerMemento.getHitboxX();
        this.hitbox.y = playerMemento.getHitboxY();
        this.attackBox.x = playerMemento.getAttackBoxX();
        this.attackBox.y = playerMemento.getAttackBoxY();
        this.currentHealth = playerMemento.getCurrentHealth();

    }

    //Questa funzione invece fa il load dei frame di una animazione e li carica in un buffer di immagini
    //La funzione precedente fa uso di 'img', ovvero l'intera immagine che viene importata nel programma come un 
    //file stream gtramite questa funzione
    private void loadAnimations() {

        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[11][15];

            for(int j=0; j< animations.length ; j++){
                for(int i=0; i<animations[j].length; i++){                
                    animations[j][i] = img.getSubimage(i*128, j*128, 128, 128);
                }
            }
        
        //Si carica inoltre l'ultimate e la barra della vita/punti abilità
        img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_EXPLOSION);
        ultimate = new BufferedImage[15];
            
        for (int i = 0; i < ultimate.length; i++) {
            ultimate[i] = img.getSubimage(i * PLAYER_EXPLOSION_DEFAULT_WIDTH, 0, PLAYER_EXPLOSION_DEFAULT_WIDTH, PLAYER_EXPLOSION_DEFAULT_HEIGHT);
        }

        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.HEALT_POWER_BAR);

    }

    public void loadLevelData(int [][] levelData){
        this.levelData = levelData;
    }
    
    //Funzione per settare il movimento a 0 quando viene chiamata
    public void resetMovement() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    //Funzioni get e set per ottenere e settare lo stato attuale degli attributi del player
    public void setSpawnPoint(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    //Metodo per saltare
    private void jump() {

        if (inAir) return;
        playing.getGame().getAudioPlayer().playSetOfEffect(AudioPlayer.PLAYER_JUMPING);
        inAir = true;
        airSpeed = jumpSpeed;
        
    }

    //Metodo che resetta la flag di salto e velocità di caduta a 0 quando il player atterra
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    //Questa funzione fa morire il player
    public void die() {
        currentHealth = 0;
        jump = false;
    }

    public Memento saveState(){
        return new PlayerMemento(this);
    }

    public boolean getLeft() {
        return left;
    }

    public Playing getPlaying() {
        return playing;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean getUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean getDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }

    public void setAttck(boolean attacking){
        this.attacking = attacking;
    }

    public void setUltimateActive(boolean ultimateActive){
        this.ultimateActive = ultimateActive;
    }

    public int getDamage(){
        return damage;
    }

    public int getPlayerTileY(){
        return tyleY;
    }

    public void setCurrentHealth(int currentPlayerHealth) {
        this.currentHealth = currentPlayerHealth;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public void setCanPlayAtacksooound(boolean canPlayAttackSound) {
        this.canPlayAttackSound = canPlayAttackSound;
    }

}
