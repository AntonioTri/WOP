package Progetto_prog_3.entities.RenderChain;

import java.awt.Graphics;

import Progetto_prog_3.entities.Player;
import Progetto_prog_3.entities.enemies.AbstractEnemy;

public interface RenderInterface {

    public void setNextHandler(RenderInterface nextHandler);
    public void renderEntity(Graphics g, Player player, RenderingRequest[] requests, AbstractEnemy ab, int xLevelOffset, int yLevelOffset);

}
