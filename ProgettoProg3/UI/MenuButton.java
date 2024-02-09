package Progetto_prog_3.UI;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import Progetto_prog_3.GameStates.GameState;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.UI.Buttons.*;


public class MenuButton extends AbstractButtons {
    
    //Variabili di ambiente, posizione e grandezza
    private int xPos, yPos;
    private int xOffsetCenter = BUTTON_WIDTH / 2;
    
    private Rectangle buttonHitbox;
    private BufferedImage [] imgs;
    
    //Variabile per settare lo stato del gioco
    private GameState state;

    //Costruttore
    public MenuButton(int xPos, int yPos, int rowIndex, GameState state){

        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;

        loadImages();
        initButtonHitbox();
    }

    //Metodo per inizializzare la hitbox indicata
    private void initButtonHitbox() {

        buttonHitbox = new Rectangle(xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT);

    }

    //Metodo per caricare gli sprite del bottone
    private void loadImages() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.MENU_BUTTONS);

        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * BUTTON_DEFAULT_WIDTH , rowIndex * BUTTON_DEFAULT_HEIGHT, BUTTON_DEFAULT_WIDTH, BUTTON_DEFAULT_HEIGHT);
        }

    }

    //Disegno del bottone, con l'indice
    public void draw(Graphics g){

        g.drawImage(imgs[columnIndex], xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT, null);

    }

    //Getters e Setters
    public void applyGameState(){
        GameState.state = state;
    }

    public Rectangle getHitbox(){
        return this.buttonHitbox;
    }

    public GameState getState(){
        return state;
    }

}
