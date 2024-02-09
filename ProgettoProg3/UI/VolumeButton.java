package Progetto_prog_3.UI;

import Progetto_prog_3.utils.LoadSave;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static Progetto_prog_3.utils.Constants.UI.VolumeButton.*;

public class VolumeButton extends AbstractButtons {

    private BufferedImage[] imgs;
    private BufferedImage slider;

    private int index = 0;
    private int buttonX, minX, maxX;

    //Il valore effettivo del volume
    private float floatValue = 0;

    public VolumeButton(int x, int y, int width, int height) {
        //Modificare cosìil richiamo al costruttore della superclasse serve a posizionare il tasto del volume 
        //esattamente in mezzo alla barra del volume quando il gioco parte
        super(x + width/2 , y, VOLUME_WIDTH, height);
        hitbox.x -= VOLUME_WIDTH / 2;
        buttonX = x + width/2;
        this.x = x;
        this.width = width;
        this.minX = x + VOLUME_WIDTH / 2;
        this.maxX = x + width - VOLUME_WIDTH / 2;
        loadImages(); 

    }
    
    private void loadImages(){
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTON);
        imgs = new BufferedImage[3];

        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAUT_WIDTH, 0, VOLUME_DEFAUT_WIDTH, VOLUME_DEFAUT_HEIGHT);
        }

        slider = temp.getSubimage(3 * VOLUME_DEFAUT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAUT_HEIGHT);

    }

    @Override
    public void update(){

        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }

        hitbox.x = buttonX - VOLUME_WIDTH / 2;

    }

    public void draw(Graphics g){
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x){
        if (x < minX) {
            buttonX = minX;
        } else if (x> maxX) {
            buttonX = maxX;
        } else {
            buttonX = x;
        }

        updateFloatValue();

    }

    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
    
        floatValue = value / range;
    
    
    }

    public float getFloatValue(){
        return floatValue;
    };

}
