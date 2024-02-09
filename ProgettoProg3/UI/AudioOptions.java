package Progetto_prog_3.UI;

import Progetto_prog_3.Game;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import static Progetto_prog_3.utils.Constants.UI.PauseButtons.*;
import static Progetto_prog_3.utils.Constants.UI.VolumeButton.SLIDER_WIDTH;
import static Progetto_prog_3.utils.Constants.UI.VolumeButton.VOLUME_HEIGHT;

public class AudioOptions implements MenusOverlayInterface {

    private SoundButton musicButon, sfxButton;
    private VolumeButton volumeButton;

    private Game game;

    public AudioOptions(Game game){
        this.game = game;
        createSoundButtons();
        createVolumeButton();
    }

    //Questa variabile costruisce il bottone del volume
    private void createVolumeButton() {

        int volumeX = (int)(309 * Game.SCALE);
        int volumeY = (int)(278 * Game.SCALE);

        volumeButton = new VolumeButton(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT);

    }

    //Questo crea i bottoni Per mettere il muto a effetti sonori e musica in generale 
    private void createSoundButtons() {
        int buttonX = (int) (450 * Game.SCALE);
        int musicX = (int) ( 140 * Game.SCALE);
        int sfxY = (int) ( 186* Game.SCALE);
        
        musicButon = new SoundButton(buttonX, musicX, SUOND_SIZE, SUOND_SIZE);
        sfxButton = new SoundButton(buttonX, sfxY, SUOND_SIZE, SUOND_SIZE);

    }



    @Override
    public void update() {
        musicButon.update();
        sfxButton.update();
        volumeButton.update();
    }



    @Override
    public void draw(Graphics g) {
        //Sound buttons
        sfxButton.draw(g);
        musicButon.draw(g);
        volumeButton.draw(g);
    }

    //Questo metodo si attiva quando il tasto del mouse viene premuto, 
    //Modifica lo stato del bottone a premuto permettendo di mostrare lo sprite corretto
    @Override
    public void mousePressed(MouseEvent e){

        if (mouseHovering(musicButon, e) ) {
            musicButon.setMousePressed(true);  
        } else if(mouseHovering(sfxButton, e)) {
            sfxButton.setMousePressed(true);
        } else if (mouseHovering(volumeButton, e)) {
            volumeButton.setMousePressed(true);
        }
        
    }

    //Questo metodo ci permette di osservare quando il tasto viene lasciato, dopo che è stato premuto
    @Override
    public void mouseReleased(MouseEvent e){

        //Questa serie di if else statements, serve a definire un evento specifico nella 
        //Schermata di pausa, se iol mouse si trova sopra ad un bottone e se questo viene premuto
        //Avvia il suo stato e modifica le componenti di gioco come il suono o il Game state

        //Music button
        if (mouseHovering(musicButon, e) ) {
            if (musicButon.getMousePressed()) {
                musicButon.setMuted(!musicButon.getMuted());
                game.getAudioPlayer().togleSongMute();
            }
        //Sound Effects button
        } else if(mouseHovering(sfxButton, e) ){
            if (sfxButton.getMousePressed()) {
                sfxButton.setMuted(!sfxButton.getMuted());
                game.getAudioPlayer().togleEffectsMute();
            }
        }
        //Si resetano i valori booleani per resettare gli sprite
        musicButon.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    //Questa funzione osserva se il mouse sta passando sopra ad un bottone, in tal caso
    //Setta il suo stato su Hover per mostrare lo sprite corretto
    @Override
    public void mouseMoved(MouseEvent e){

        musicButon.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (mouseHovering(musicButon, e)) {
            musicButon.setMouseOver(true);
        } else if(mouseHovering(sfxButton, e)){
            sfxButton.setMouseOver(true);
        } else if(mouseHovering(volumeButton, e)){
            volumeButton.setMouseOver(true);
        }
    }
 
    //Questa funzione serve ad aggiornare la posiizone del bottone del mouse
    @Override
    public void mouseDragged(MouseEvent e){
        if (volumeButton.getMousePressed()) {

            float valueBefore = volumeButton.getFloatValue();
            volumeButton.changeX(e.getX());
            float valueAfter = volumeButton.getFloatValue();

            if (valueBefore != valueAfter) {
                game.getAudioPlayer().setVolume(valueAfter);
            }
        }
    }
   
    //Si usa il polimorfismo per la classe PauseButton
    @Override
    public boolean mouseHovering(AbstractButtons pb, MouseEvent e ){
        return pb.getHitbox().contains(e.getX(), e.getY());
    }
}
