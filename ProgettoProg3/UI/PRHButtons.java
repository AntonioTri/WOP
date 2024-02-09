package Progetto_prog_3.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.UI.PhrButtons.*;

public class PRHButtons extends AbstractButtons {

    private BufferedImage[] imgs;

    public PRHButtons(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImages();
    }

    private void loadImages(){
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.PRH_BUTTONS);

        imgs = new BufferedImage[3];

        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * PRH_BUTTONS_DEFAULT_SIZE, rowIndex * PRH_BUTTONS_DEFAULT_SIZE, PRH_BUTTONS_DEFAULT_SIZE, PRH_BUTTONS_DEFAULT_SIZE);
        }
    }

    public void draw(Graphics g){
        g.drawImage(imgs[columnIndex], x, y, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, null);
    }

    
}
