package Progetto_prog_3.entities.RenderChain;

import java.awt.image.BufferedImage;

//La rendering request è una richiesta atta alla responsability chain che esegue il rendering dei nemici
//Coontiene tutti i campi necessari affinchè i rendeered riescano ad eseguire la richiesta in modo completo ed esaustivo
public class RenderingRequest {
    
    //Tipo del nemico
    private int enemyType;
    //Sprite del nemico
    private BufferedImage[][] sprites;
    //Buffer per gli attacchi speciali o particolari come quelli del ghost
    private BufferedImage[] specialAttack;

    //vi soono due costruttoori per rendere dinamica la possibilità di inseireo o no attacchi speciali
    public RenderingRequest(BufferedImage[][] image, int enemyType){
        this.sprites = image;
        this.enemyType = enemyType;
    }

    public RenderingRequest(BufferedImage[][] image, BufferedImage[] specialAttack, int enemyType){
        this.sprites = image;
        this.specialAttack = specialAttack;
        this.enemyType = enemyType;

    }

    //Getters utili ai renderer della chain per ottenere informazioni della richiesta
    public int getEnemyType() {
        return enemyType;
    }

    public BufferedImage[][] getSprites() {
        return sprites;
    }

    public BufferedImage[] getSpecialAttack() {
        return specialAttack;
    }

}
