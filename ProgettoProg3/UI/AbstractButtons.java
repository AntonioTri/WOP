package Progetto_prog_3.UI;

import java.awt.Rectangle;

//Questa superclasse permette la crazione di qualsivoglia bottone
//Contiene diversi metodi ed attributi utili alla creazione di un bottne come
//la hitbox rettangolare, la posizione, la larghezza e l'altezza
public abstract class AbstractButtons {
    
    protected int x, y, width, height;
    protected boolean mouseOver, mousePressed;
    protected int rowIndex, columnIndex;
    protected Rectangle hitbox;

    public AbstractButtons(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    
        createHitbox();

    }

    public AbstractButtons(){};

    public void update(){

        columnIndex = 0;
        if (mouseOver) {
            columnIndex = 1;
        }
        if (mousePressed) {
            columnIndex = 2;
        }

    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    //Getters e Setters
    private void createHitbox() {
        hitbox = new Rectangle(x, y, width, height);
    }

    public boolean getMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean getMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }


}
