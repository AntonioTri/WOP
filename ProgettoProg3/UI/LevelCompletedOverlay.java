package Progetto_prog_3.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import Progetto_prog_3.Game;
import Progetto_prog_3.GameStates.GameState;
import Progetto_prog_3.GameStates.Playing;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.UI.PhrButtons.*;

public class LevelCompletedOverlay implements MenusOverlayInterface {
    
    Playing playing;
    private PRHButtons menuButton, nextButton;
    private BufferedImage img;
    private int bgX, bgY, bgWidth, bgHeight;

    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        loadImages();
        initButtons();
    }

    private void initButtons() {

        int menuX = (int) (330 * Game.SCALE);
		int nextX = (int) (445 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		nextButton = new PRHButtons(nextX, y, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 0);
		menuButton = new PRHButtons(menuX, y, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 2);

    }

    private void loadImages() {

        img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_COMPLITED);
		bgWidth = (int) (img.getWidth() * Game.SCALE);
		bgHeight = (int) (img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
		bgY = (int) (75 * Game.SCALE);

    }

    @Override
    public void draw(Graphics g){
        // Added after youtube upload
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, bgX, bgY, bgWidth, bgHeight, null);
		menuButton.draw(g);
		nextButton.draw(g);

    };

    @Override
    public void update(){
        menuButton.update();
        nextButton.update();
    }

    @Override
    public boolean mouseHovering(AbstractButtons button, MouseEvent e){
        return button.getHitbox().contains(e.getX(), e.getY());
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        nextButton.setMouseOver(false);
        menuButton.setMouseOver(false);
        
        if (mouseHovering(menuButton, e)) {
            menuButton.setMouseOver(true);
        } else if(mouseHovering(nextButton, e)){
            nextButton.setMouseOver(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e){
        if (mouseHovering(menuButton, e)) {
            menuButton.setMousePressed(true);
        } else if(mouseHovering(nextButton, e)){
            nextButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if (mouseHovering(menuButton, e) && menuButton.getMousePressed()) {
            playing.resetAll();
            playing.setGameState(GameState.MENU);

        } else if (mouseHovering(nextButton, e) && nextButton.getMousePressed()) {
            //In questa sezione andiamo aa salvare la vita corrente del player quando carichiamo un nuovo livello
            playing.loadNextLevel();
            playing.getGame().getAudioPlayer().playSong(1);

        }

        menuButton.resetBools();
        nextButton.resetBools();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }


}
