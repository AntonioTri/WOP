package Progetto_prog_3.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Progetto_prog_3.Game;
import Progetto_prog_3.GameStates.GameState;
import Progetto_prog_3.GameStates.Playing;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.UI.PhrButtons.*;

//Overlay di morte ancora da fare, per adesso scrive due scritte a schermo per indicare la morte
// e riporta l'utente nella schermata home
public class GameOverOverlay implements MenusOverlayInterface{

    private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private PRHButtons menu, play;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		loadImagee();
		createButtons();
	}

	private void createButtons() {
		int menuX = (int) (335 * Game.SCALE);
		int playX = (int) (440 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		play = new PRHButtons(playX, y, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 0);
		menu = new PRHButtons(menuX, y, PRH_BUTTONS_SIZE, PRH_BUTTONS_SIZE, 2);
	}

	private void loadImagee() {
		img = LoadSave.getSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(img, imgX, imgY, imgW, imgH, null);

		menu.draw(g);
		play.draw(g);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			GameState.state = GameState.MENU;
		}
	}

	@Override
    public void update(){
        menu.update();
        play.update();
    }

    @Override
    public boolean mouseHovering(AbstractButtons button, MouseEvent e){
        return button.getHitbox().contains(e.getX(), e.getY());
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        play.setMouseOver(false);
        menu.setMouseOver(false);
        
        if (mouseHovering(menu, e)) {
            menu.setMouseOver(true);
        } else if(mouseHovering(play, e)){
            play.setMouseOver(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e){
        if (mouseHovering(menu, e)) {
            menu.setMousePressed(true);
        } else if(mouseHovering(play, e)){
            play.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if (mouseHovering(menu, e) && menu.getMousePressed()) {
			playing.resetAll();
            playing.setGameState(GameState.MENU);
        } else if (mouseHovering(play, e) && play.getMousePressed()) {
            playing.resetAll();

        }

        menu.resetBools();
        play.resetBools();

    }

	@Override
	public void mouseDragged(MouseEvent e) {
	}


}
