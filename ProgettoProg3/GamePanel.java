package Progetto_prog_3;

import Progetto_prog_3.Inputs.*;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import static Progetto_prog_3.Game.GAME_HEIGHT;
import static Progetto_prog_3.Game.GAME_WIDTH;


public class GamePanel extends JPanel {
    
    //Variabili di ambiente
    private MouseInputs mouseInputs;

    private Game game;

    //Costruttore che aggiunge alla creazione un mouseListener per osservare
    //i cambiamenti del mouse,un keyboardListener per ascoltare i tasti premuti dalla tastiera
    public GamePanel(Game game){

        this.game = game;

        mouseInputs = new MouseInputs(this);
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

    }


    //Questa invece serve solo a settare la grandezza del pannello di gioco
    private void setPanelSize() {

        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

    }

    /* ATTENZIONE! LA FUNZIONE REPAINT SERVE AD AGGIORNARE QUELLO CHE VEDIAMO A SCHERMO */

    //Qui invece disegnamo il rettangolo e gli elementi di gioco
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        game.render(g);

    }

    public Game getGame(){
        return game;
    }

}
