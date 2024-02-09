package Progetto_prog_3.UI;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import Progetto_prog_3.Game;
import Progetto_prog_3.GameStates.GameState;
import Progetto_prog_3.GameStates.Playing;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.UI.PhrButtons.*;

//Classe che definisce la schermata di ausa, nella quale coesistono diversi bottoni per diverse
//Funzionalità legate al gameplay, come il volume, reset del livello, ritorno alla schermata iniziale
public class PauseOverlay implements MenusOverlayInterface{

    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;

    //Bottoni presenti nell schermata
    private PRHButtons homeB, replayB, unpauseB;
    private AudioOptions audioOptions;
    //Variabile per accedere al game state di "Playing"
    Playing playing;

    //Il costruttore inizializza tutti i bottoni presenti
    public PauseOverlay(Playing playing){

        this.playing = playing;
        audioOptions = playing.getGame().geAudioOptions();
        loadBackground();
        createPRHButtons();
        
    }

    //Questo metodo invece chrea i tre bottoni di pausa, reset e ritorno a schermata home
    private void createPRHButtons() {

        //Differenti posizioni in x
        int homeX = (int)(313 * Game.SCALE);
        int replayX = (int)(387 * Game.SCALE);
        int unpauseX = (int)( 461 * Game.SCALE);
        //Posizione uguale in y
        int buttonY = (int)(325 * Game.SCALE);

        homeB = new PRHButtons(homeX, buttonY, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 2);
        replayB = new PRHButtons(replayX, buttonY, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 1);
        unpauseB = new PRHButtons(unpauseX, buttonY, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 0);


    }

    //Questo metodo come gli altri gia incontrati durante il progetto, carica un png per l'immagine del nostro Pause Menu
    private void loadBackground() {
        
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE); 
        bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);

        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (25 * Game.SCALE);
    
    }

    @Override
    public void update(){
        homeB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g){
        //Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);
        
        //Home Resume unpause button
        homeB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        audioOptions.draw(g);

    }

    //Questa funzione serve ad aggiornare la posiizone del bottone del mouse
    @Override
    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    //Questo metodo si attiva quando il tasto del mouse viene premuto, 
    //Modifica lo stato del bottone a premuto permettendo di mostrare lo sprite corretto
    @Override
    public void mousePressed(MouseEvent e){

        if (mouseHovering(homeB, e)) {
            homeB.setMousePressed(true);
        } else if (mouseHovering(replayB, e)) {
            replayB.setMousePressed(true);
        } else if (mouseHovering(unpauseB, e)) {
            unpauseB.setMousePressed(true);
        } else audioOptions.mousePressed(e);
        
    }

    //Questo metodo ci permette di osservare quando il tasto viene lasciato, dopo che è stato premuto
    @Override
    public void mouseReleased(MouseEvent e){

        //Questa serie di if else statements, serve a definire un evento specifico nella 
        //Schermata di pausa, se iol mouse si trova sopra ad un bottone e se questo viene premuto
        //Avvia il suo stato e modifica le componenti di gioco come il suono o il Game state
        
        //Home Button
        if(mouseHovering(homeB, e) ){
            if (homeB.getMousePressed()) {
                playing.resetAll();
                playing.setGameState(GameState.MENU);
                playing.unpauseGame();
            }
        //Replay Button
        } else if(mouseHovering(replayB, e) ){
            if (replayB.getMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        //Unpause Button
        } else if(mouseHovering(unpauseB, e) ){
            if (unpauseB.getMousePressed()) {
                playing.unpauseGame();
            }

        } else audioOptions.mouseReleased(e);
        
        //Si resetano i valori booleani per resettare gli sprite
        homeB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    //Questa funzione osserva se il mouse sta passando sopra ad un bottone, in tal caso
    //Setta il suo stato su Hover per mostrare lo sprite corretto
    @Override
    public void mouseMoved(MouseEvent e){

        homeB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if(mouseHovering(homeB, e)){
            homeB.setMouseOver(true);
        } else if(mouseHovering(replayB, e)){
            replayB.setMouseOver(true);
        } else if(mouseHovering(unpauseB, e)){
            unpauseB.setMouseOver(true);
        } else audioOptions.mouseMoved(e);
    }

    //Si usa il polimorfismo per la classe PauseButton
    @Override
    public boolean mouseHovering(AbstractButtons pb, MouseEvent e ){
        return pb.getHitbox().contains(e.getX(), e.getY());
    }



}
