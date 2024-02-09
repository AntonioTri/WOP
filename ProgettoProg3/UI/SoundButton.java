package Progetto_prog_3.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.UI.PauseButtons.*;

public class SoundButton extends AbstractButtons{

    private BufferedImage[][] soundImgs;
    //Questa flag serve a capire se siamo nella riga del volumepresente o nella riga del volume mutato;
    private boolean muted;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        
        loadSoundImages();
    }

    private void loadSoundImages() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SOUND_BUTTON);

        soundImgs = new BufferedImage[2][3];

        for(int j = 0; j < soundImgs.length; j++){
            for (int i = 0; i < soundImgs[j].length; i++) {
                soundImgs[j][i] = temp.getSubimage(i * SUOND_SIZE_DEFAULT, j* SUOND_SIZE_DEFAULT ,SUOND_SIZE_DEFAULT, SUOND_SIZE_DEFAULT);
            }
        }
    }

    //In baso allo stato modificato nel PauseOverlay, vengono settati gli indici
    //Della matrice degli sprite, permetttendo di mostrare quello giusto di volta in volta
    @Override
    public void update(){

        if (muted) {
            rowIndex = 1;
        } else {
            rowIndex = 0;
        }

        columnIndex = 0;

        if (mouseOver) {
            columnIndex = 1;
        }
        if (mousePressed) {
            columnIndex = 2;
        }
    }

    //Getters e Setters
    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][columnIndex], x, y, width, height, null);
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean getMuted(){ 
        return muted;
    }

}
