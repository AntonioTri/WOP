package Progetto_prog_3.GameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Progetto_prog_3.Game;
import Progetto_prog_3.UI.MenuButton;
import Progetto_prog_3.utils.LoadSave;

/*  Questa classe rappresenta il menù di gioco con i suoi bottoni, gestisce dei gamestate, per cambare da
    stato all'altro, renderizza i bottoni
*/
public class Menu extends State implements StateMethods {

    //Variabili di ambiente, come i tasti del menù ed il background dello stesso 
    //(il contenitore che contiene i bottoni, non làeffettivo background)
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImage, actualBackgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;

    //Costruttore, che carica i bottoni ed il background di questi
    public Menu(Game game) {
        super(game);
        loadBackground();
        loadButtons();
        actualBackgroundImage = LoadSave.getSpriteAtlas(LoadSave.HOME_BACKGROUND_IMAGE);
    }

    /*
     * Metodo che inizializza il backgroun
     */
    private void loadBackground() {
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int)(backgroundImage.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundImage.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    /*
     * Metodo che inizializa i 3 bottoni del menù, ad ognuno è accegnato un indice dell'array, un diverso GAME_STATE
     * ed un suo indice di riga utilizzato nel suo metodo draw che lo disegna a schermo
     */
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, GameState.PLAYING );
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, GameState.OPTION );
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, GameState.QUIT );
    }

    @Override
    public void update() {
        for (MenuButton mb: buttons){
            mb.update();
        }
    }

    //Funzione che ci permette di disegnare i bottoni
    @Override
    public void draw(Graphics g) {
        g.drawImage(actualBackgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT,  null);
        g.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight, null);

       for (MenuButton menuButton : buttons) {
            menuButton.draw(g);
       }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e);
    }

    /* 
     * In questa funzione andiamo a capire se il bottone viene premuto, tramite un for analizziamo gli hover
     * su ogni bottone, se il bottone in questione viene premuto, andiamo a settare il suo stato a true
     * e modificherà semplicemente il suo sprite per mostrarlo premuto
    */

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
			if (buttonHovered(e, mb)) {
				mb.setMousePressed(true);
			}
		}
    }

    /*
     * Anche questa funzione non serve altro che a far cambiare lo sprite di presentazione se il bottone viene hoverato
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        
        for (MenuButton mb : buttons) {
            if (buttonHovered(e, mb)) {
                mb.setMouseOver(true);
                break;
            } else{
                mb.setMouseOver(false);
            }
        }
    }

    /* !!!!!!!!!!!
     * Qui dentro avviene l'effetico cambio del CURRENT GAME STATE.
     * Se il bottone in questione viene premuto, il bottone applica il suo GAME STATE al gioco, andando a cambiare la schermata
     * !!!!!!!!!!!
     */

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb: buttons){
           if (buttonHovered(e, mb)) {
                if (mb.getMousePressed()) {
                    mb.applyGameState();
                }
                if (mb.getState() == GameState.PLAYING) {
                    game.getAudioPlayer().playSong(1);
                }
                break;
           }
        }
        resetButtons();
    }

    
    //Se si preme invio nella schermata Home, viene settato lo stato di PLAYING in modo automatico senza passare per i bottoni
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    };

    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

}
