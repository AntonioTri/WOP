package Progetto_prog_3.GameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import Progetto_prog_3.Game;
import Progetto_prog_3.UI.GameOverOverlay;
import Progetto_prog_3.UI.LevelCompletedOverlay;
import Progetto_prog_3.UI.PauseOverlay;
import Progetto_prog_3.entities.EnemyManager;
import Progetto_prog_3.entities.Player;
import Progetto_prog_3.entities.MementoSavings.MementoManager;
import Progetto_prog_3.levels.LevelManager;
import Progetto_prog_3.objects.ObjectManager;
import Progetto_prog_3.utils.LoadSave;

public class Playing extends State implements StateMethods{
    
    private Player player;
    private EnemyManager enemyManager;
    private LevelManager levelManager;
    private ObjectManager objectManager;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;

    private boolean paused = false;
    private PauseOverlay pauseOverlay;
    
    //Queste variabili mi servono a gestire il movimento della telecamera nel mondo
    private int xLevelOffset, yLevelOffset; 
    
    //Queste due variabili definiscono il bordo dopo il quale la visuale viene regolata e spostata di conseguenza
    private int leftBorder = (int)(0.5 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.5 * Game.GAME_WIDTH);
    private int bottomBorder = (int)(0.7 * Game.GAME_HEIGHT);
    private int upperBorder = (int)(0.3 * Game.GAME_HEIGHT);
    
    //Questa variabile tramite il level data, ci permete di accedere alla lunghezza del livello
    //private int levelTileWide = LoadSave.getLevelData()[0].length;
    //Queste servono a definire entro quale limite non bisonga più spostare la telecamera
    //private int maxTileOffset = levelTileWide - Game.TILES_IN_WIDTH;
    private int maxLevelOffsetX;
    private int maxLevelOffsetY;
    
    //Immagini di background
    private BufferedImage backgroundImage, layer1, layer2;
    
    //La seguente variabile definisce se il player sta morendo, così da fermare tutti gli update
    private boolean playerDying = false;
    //Variabile pre identificare il Game Over
    private boolean gameOver = false;
    
    //level complited
    private boolean levelCompleted;

    //Variabili per il Memento
    private MementoManager mementoManager;

    public Playing(Game game) {
        super(game);
        initClasses();
        calculateLevelOffset();
        loadStartLevel();
    }

    //Funzione per inizializzare le classi delle entita presenti
    private void initClasses() { 

        mementoManager = new MementoManager();
        
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 400, (int) (64*Game.SCALE), (int)(64*Game.SCALE), this); 
        player.loadLevelData(levelManager.getCurrentLevel().getLD());
        player.setSpawnPoint(levelManager.getCurrentLevel().getPlayerSpawnPoint());

        //Salvo lo stato iniziale del livello del player

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        
        loadBackground();

    }

    //Override dei metodi dell'interfaccia implementata
    @Override
    public void update() {
        //Se il gioco è in pausa viene fatto l'update solo del pause ovelray
        if (paused) {
            pauseOverlay.update();
        
        //Se il livello è stato completato si esegue l'update del level completed overlay
        } else if (levelCompleted) {
            levelCompletedOverlay.update();

        } else if (gameOver) {
            gameOverOverlay.update();

        } else if (playerDying) {
            player.update();

        //Altrimenti viene fatto l'update del player e del livello, e di tutti gli oggetti all'interrno di esso
        } else if (!gameOver){

            levelManager.update();
            enemyManager.update(levelManager.getCurrentLevel().getLD(), player);
            objectManager.update(levelManager.getCurrentLevel().getLD(), player);
            player.update();
            checkCloseToBorder();
    
        }
    }

    @Override
    public void draw(Graphics g) {
        //Viene disegnato il background
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        //Vengono agiunti i livelli in movimento
        drowLayer1(g);
        drowLayer2(g);

        //Durante il draw, vengono aggiunti gli offset per disegnare la parte di mappa corretta
        levelManager.draw(g, xLevelOffset, yLevelOffset);
        objectManager.draw(g, xLevelOffset, yLevelOffset);
        enemyManager.draw(g, xLevelOffset, yLevelOffset);
        player.render(g, xLevelOffset, yLevelOffset);

        //Vengono scritti gli FPS e gli UPS a schermo
        g.setColor(Color.white);
		g.drawString(game.getFpsUps(), 30, Game.GAME_HEIGHT - 20);
        
        if (paused) {
            //Se il gioco viene messo in pausa, viene disegnato un rettangolo tra il game ed il menù di pausa
            //Per dare un effetto piacevole alla vista
            g.setColor(new Color(0,0,0, 140));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            //Si disegna il menù di pausa sopra al rettangolo opaco precedente
            pauseOverlay.draw(g);

            //Se avviene un gameo over, si diegna il game over overlay
        } else if (gameOver) {
            gameOverOverlay.draw(g);
            
            //Se il livello viene completato si diegna il level completed overlay
        } else if (levelCompleted) {
            levelCompletedOverlay.draw(g);
            levelCompletedOverlay.update();
        }

    }

    private void drowLayer1(Graphics g) {
        for (int i = 0; i < 3; i++) {
            //g.drawImage(smallClouds, SMALL_CLOUD_WIDTH * 4 * i -(int) ( xLevelOffset * 0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
            g.drawImage(layer1, Game.GAME_WIDTH * i -(int) ( xLevelOffset * 0.25), 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        }

    }

    private void drowLayer2(Graphics g) {
        for (int i = 0; i < 3; i++) {
            //g.drawImage(bigClouds, i*BIG_CLOUD_WIDTH -(int) ( xLevelOffset * 0.3) , (int)(204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
            g.drawImage(layer2, (Game.GAME_WIDTH - 100) * i -(int) ( xLevelOffset * 0.6), 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        }

    }

    private void loadBackground(){

        //Vengono caricate le immagini dlle nuvole
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.FOREST_LAYER_1);
        layer1 = LoadSave.getSpriteAtlas(LoadSave.FOREST_LAYER_2);
        layer2 = LoadSave.getSpriteAtlas(LoadSave.FOREST_LAYER_3);

    }

    
    //Questa funzione serve a definire la distanza tra il player ed il bordo,
    //In caso avvengano alcune verita' vengono definiti i valori per spostare la videocamera
    private void checkCloseToBorder() {

        //Si estrae la posizione in x del player
        int playerX = (int) player.getHitbox().x;
        //Si calcola la differenza tra la posizione attuale e la variabile offset del livello
        int difference = playerX - xLevelOffset;

        //Se la differenza è maggiore del bordo di destra, al level offset viene aggiunta la differenza
        //più i lvalore del bordo destro/sinistro 
        if (difference > rightBorder) {
            xLevelOffset += difference - rightBorder;
        } else if(difference < leftBorder){
            xLevelOffset += difference - leftBorder;
        }

        //Se il valore invece supera la grandezza massima dei bordi del livello, l'offset viene
        //settato come la posizione del muro che sta provando ad atraversare così da nono mostrare 
        //una parte del livello vuota
        if (xLevelOffset > maxLevelOffsetX) {
            xLevelOffset = maxLevelOffsetX;
        } else if (xLevelOffset < 0) {
            xLevelOffset  = 0;
        }

        //Lo stesso si fa per la y
        int playerY = (int)player.getHitbox().y;
        int differenceY = playerY - yLevelOffset;

        if (differenceY > bottomBorder) {
            yLevelOffset += differenceY - bottomBorder;
        } else if(differenceY < upperBorder){
            yLevelOffset += differenceY - upperBorder;
        }

        if (yLevelOffset >= maxLevelOffsetY) {
            yLevelOffset = maxLevelOffsetY;
        } else if (yLevelOffset <= 0) {
            yLevelOffset = 0;
        }
    }

    //I seguenti metodi in override servono a passare le keyevent, i tasti premuti della tastiera e del mouse
    //a diversi oggetti, come il giocatore, oppure ai vari menù di overlay quando sono presenti 

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttck(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);

            } else if(levelCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        } else{
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
                
            } else if(levelCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        } else{
            gameOverOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if(levelCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }
        } else{
            gameOverOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e){
        if (!gameOver && paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setUp(true);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_SHIFT:
                    player.dash();
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
                case KeyEvent.VK_E:
                    player.ultimateAbility();
                    break;
                case KeyEvent.VK_UP:
                    player.setAttck(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        if (!gameOver) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setUp(false);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
                default:
                    break;

            }
        }
    }

    //Getters e Setters
    public void resetAll(){
        
        resetBools();
        
        //Si utilizza il Memento design pattern per resettare il livello al suo stato di partenza/Di crezione
        //All'interno di ogni reset dedicato ai singoli manager
        player.resetAll();
        enemyManager.resetAllEnemyes();
        objectManager.resetAllObjects();

    }

    //Funzione per resettare tutti i valori booleani
    public void resetBools(){

        gameOver = false;
        paused = false;
        levelCompleted = false;
        playerDying = false;

    }

    private void calculateLevelOffset() {
        maxLevelOffsetX = levelManager.getCurrentLevel().getLevelOffset();
        maxLevelOffsetY = levelManager.getCurrentLevel().getLevelOffsetY();
    }

    public void loadNextLevel(){
        resetBools();
        levelManager.loadNextLevel();
    }

    private void loadStartLevel() {
        enemyManager.addEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    //I seguenti 4 metodi invece controllano se il player sta toccando determinati oggetti nela scena, se sta attaccando un nemico
    //Una pozione, una spina, tante cose
    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkPlayerTouchedPotions(hitbox);
    }

    public void checkPlayerHitEnemy(Rectangle2D.Float attackBox, int areaAttack) {
        enemyManager.checkPlayerHitEnemy(attackBox, areaAttack);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox, int areaAttack) {
        objectManager.checkObjectHit(attackBox, areaAttack);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(player);
    }

    public void setMaxLevelOffsetX(int levelOffset){
        this.maxLevelOffsetX = levelOffset;
    }

    public void setMaxLevelOffsetY(int yLevelOffset){
        this.maxLevelOffsetY = yLevelOffset;
    }

    public void unpauseGame(){
        paused = false;
    }

    public void setGamePaused(){
        paused = true;
    }
    
    public void windowFocusLost() { 
        player.resetMovement(); 
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public Player getPlayer(){
        return player;
    }
 
    public void setLevelComplited(boolean levelCompleted){
        this.levelCompleted = levelCompleted;
        if (levelCompleted) {
            game.getAudioPlayer().levelComplited();
        }
    }
   
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public LevelCompletedOverlay getLevelCompletedOverlay(){
        return levelCompletedOverlay;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

    public boolean getPlayerDying() {
        return playerDying;
    }

    public MementoManager getMementoManager(){
        return mementoManager;
    }
    

    

}
