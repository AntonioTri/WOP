package Progetto_prog_3;
import java.awt.Graphics;

import Progetto_prog_3.Audio.AudioPlayer;
import Progetto_prog_3.GameStates.GameOptions;
import Progetto_prog_3.GameStates.GameState;
import Progetto_prog_3.GameStates.Menu;
import Progetto_prog_3.GameStates.Playing;
import Progetto_prog_3.GameStates.StateMethods;
import Progetto_prog_3.UI.AudioOptions;

public class Game implements Runnable{

    //Variabili di ambiente
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    private final int SET_FPS = 120;
    private final int SET_UPS = 180;
    private int frame = 0;
    private int update = 0;
    

    //Variabili per la mappa
    public final static float SCALE = 2.5f;
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = (TILES_SIZE * TILES_IN_WIDTH);
    public final static int GAME_HEIGHT = (TILES_SIZE * TILES_IN_HEIGHT);

    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    
    //State design pattern
    private StateMethods stateToUpdate;
    private GameState currentGameState;
    
    //Game States
    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;

    public Game(){

        initClasses();
        StartGameLoop();

    }

    private void StartGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //Funzione per inizializzare le classi delle entita presenti
    private void initClasses() { 
        
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        gameOptions = new GameOptions(this);
        menu = new Menu(this);
        playing = new Playing(this);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        
    }

    //Funzione per updatare lo stato degli elementi inizializzati correnti
    private void update() { 

        checkGameStateChanged();
        //Lo switch osserva il current game state ed eseguirà soltanto specifiche azioni, qusto ci permette si eseguire 
        //Specifici stati come il menù di pausa, di inizio oppure il gioco stesso
        stateToUpdate.update();

    }

    //Funzione per fare la paint degli elementi correnti
    public void render(Graphics g){ 
        
        try {
            stateToUpdate.draw(g);
        } catch (Exception e) { }

    }

    //Implementazione dello State pattern, una interfaccia polimorfa prende le sembianze di uno di tre macro stati
    //Playing, in cui si vede il player i nemici ed il gioco effettivo
    //Menu, dove l'utente può interfacciarsi con i bottoni per iniziare a giocare, le impostazioni ed il quit
    //Options, è il menù delle opzioni da cui làutente cambia principalmente i volumi
    private void checkGameStateChanged() {

        if (currentGameState != GameState.state) {
            currentGameState = GameState.state;

            switch (GameState.state) {
                case MENU:
                    stateToUpdate = this.menu;
                    break;
    
                case PLAYING:
                    stateToUpdate = this.playing;
                    break;
    
                case OPTION:
                    stateToUpdate = this.gameOptions;
                    break;

                case QUIT:
                    System.exit(0);
                    break;
    
                default:
                    //Esce dal programma, lo termina
                    System.exit(0);
                    break;

            }
        }
    }


    
    //Funzione per gestire la perdita del focus dalla finestra di gioco
    public void windowFocusLost() { 
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetMovement();
            playing.setGamePaused();
        }
    }

    //Funzione per gestire i frame per secondo e gli update per secondo
    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / SET_FPS;
        double timePerUpdate = 1000000000.0 / SET_UPS;

        int frames = 0;
        int updates = 0;
        
        long previousTime = System.nanoTime();
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            //Fps Counter
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                frame = frames;
                update = updates;
                frames = 0;
                updates = 0;
            }
        }
    }

    //Qusto metodo ritorna una stringa formattata che contiene gli FPS e gli UPS
    public String getFpsUps(){
        return "FPS: " + frame + " | UPS: " + update;
    }

    public Menu getMenu(){
        return menu;
    }

    public Playing getPlaying(){
        return playing;
    }

    public GameOptions getGameOptions(){
        return gameOptions;
    }

    public AudioOptions geAudioOptions(){
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer(){
        return audioPlayer;
    }

}
