package Progetto_prog_3.GameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import Progetto_prog_3.Game;
import Progetto_prog_3.UI.AbstractButtons;
import Progetto_prog_3.UI.AudioOptions;
import Progetto_prog_3.UI.MenusOverlayInterface;
import Progetto_prog_3.UI.PRHButtons;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.UI.PhrButtons.PRH_BUTTONS_SIZE;

public class GameOptions extends State implements MenusOverlayInterface, StateMethods {

    private AudioOptions audioOptions;
    private BufferedImage backgroundImage, optionsBackgroundImage;
    private int bgX, bgY, bgW, bgH;
    private PRHButtons menuButton;

    public GameOptions(Game game) {
        super(game);
        loadImages();
        loadButton();
        audioOptions = game.geAudioOptions();
    }

    private void loadImages() {
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.HOME_BACKGROUND_IMAGE);
        optionsBackgroundImage = LoadSave.getSpriteAtlas(LoadSave.OPTIONS_MENU);

		bgY = (int) (33 * Game.SCALE);
        bgW = (int) (optionsBackgroundImage.getWidth() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgH = (int) (optionsBackgroundImage.getHeight() * Game.SCALE);
    }

    private void loadButton() {
        int menuX = (int)(387 * Game.SCALE);
        int menuY = (int)(325 * Game.SCALE);

        menuButton = new PRHButtons(menuX, menuY, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 2);

    }

    @Override
    public void update() {
        menuButton.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImage, bgX, bgY, bgW, bgH, null);
        menuButton.draw(g);
        audioOptions.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mouseHovering(menuButton, e)) {
            menuButton.setMousePressed(true);
        } else audioOptions.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mouseHovering(menuButton, e)) {
            GameState.state = GameState.MENU;
        } else audioOptions.mouseReleased(e);

        menuButton.setMousePressed(false);
        menuButton.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);

        if (mouseHovering(menuButton, e)) {
            menuButton.setMouseOver(true);
        } else audioOptions.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }
    
    @Override
    public boolean mouseHovering(AbstractButtons pb, MouseEvent e) {
        return pb.getHitbox().contains(e.getX(), e.getY());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}
    
}
