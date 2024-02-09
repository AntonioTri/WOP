package Progetto_prog_3.UI;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//Interfaccia per tutti i menu di overlay
public interface MenusOverlayInterface {
    
    public void update();
    public void draw(Graphics g);
    public void mousePressed(MouseEvent e);
    public void mouseReleased(MouseEvent e);
    public void mouseMoved(MouseEvent e);
    public boolean mouseHovering(AbstractButtons pb, MouseEvent e);
    public void mouseDragged(MouseEvent e);

}
