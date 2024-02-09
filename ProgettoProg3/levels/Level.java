package Progetto_prog_3.levels;

import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import Progetto_prog_3.Game;
import java.awt.image.BufferedImage;
import Progetto_prog_3.objects.Spike;
import Progetto_prog_3.objects.Potion;
import Progetto_prog_3.objects.Cannon;
import Progetto_prog_3.objects.LootBox;
import Progetto_prog_3.utils.HelpMetods;
import Progetto_prog_3.entities.EnemyFactory;
import Progetto_prog_3.entities.enemies.AbstractEnemy;
import static Progetto_prog_3.utils.HelpMetods.getLevelData;
import static Progetto_prog_3.utils.HelpMetods.GetPlayerSpawnPoint;
import static Progetto_prog_3.utils.Constants.EnemtConstants.Ghost.GHOST;
import static Progetto_prog_3.utils.Constants.EnemtConstants.HellBound.HELL_BOUND;
import static Progetto_prog_3.utils.Constants.EnemtConstants.NightBorne.NIGHT_BORNE;

//Classe Level, memorizza le informazioni utili per la creazione di un livello e la gestione di alcuen sue caratteristiche
public class Level {

    //Istanza della enemyFacttoory
    private EnemyFactory enemyFactory;
    //L'immagine conserva il level data
    private BufferedImage image;
    //Il seguente array, conserva i nemici
    private ArrayList<AbstractEnemy> enemyList;
    //I seguenti 2 le pozioni e le loot box
    private ArrayList<Potion> potions;
    private ArrayList<LootBox> lootBoxes;
    //Il seguente per le trappole
    private ArrayList<Spike> spikes;
    //Il seeguentee array per i cannoni
    private ArrayList<Cannon> cannons;

    //Questa variabile tramite il level data, ci permete di accedere alla lunghezza del livello
    private int levelTileWide, levelTileHeight;
    //Queste servono a definire entro quale limite non bisonga più spostare la telecamera
    private int maxTileOffset;
    private int maxLevelOffsetX;
    private int maxTileOffsetY;
    private int maxLevelOffsetY;


    //Punto di spawn del player
    Point playerSpawnPoint;

    private int[][] levelData;
    
    public Level(BufferedImage image){
        this.image = image;
        this.enemyFactory = new EnemyFactory();
        createLevelData();
        createEnemies(image);
        createPotions();
        createLootBoxes();
        createSpikes();
        calculateCannons();
        calculateLevelOffsets();
        calcualtePlayerSpawnPoint();

    }

    //Funzione per calcolare la posiizone dei cannoni
    private void calculateCannons() {
        cannons = HelpMetods.getCannons(image);
    }

    //Funzione per calcolare la posizione delle Spine trappola
    private void createSpikes() {
        spikes = HelpMetods.getSpikes(image);
    }

    //Funzione per calcolare la posizione delle loot box
    private void createLootBoxes() {
        lootBoxes = HelpMetods.getLootBoxes(image);
    }

    //Funzione per calcolare la posizione delle pozioni
    private void createPotions() {
        potions = HelpMetods.getPotions(image);
    }

    //Funzione per calcolare il punto di spawn del player
    private void calcualtePlayerSpawnPoint() {
        playerSpawnPoint = GetPlayerSpawnPoint(image);
    }

    //Funzione per ottenere i dati del livello
    private void createLevelData() {
        levelData = getLevelData(image);
    }
    
    //Funzione per creare i nemici, ogni array viene associato ad una specifica funzione che genera quel tipo di nemico
    //Utilizza la factory per creare i nemici
    private void createEnemies(BufferedImage img){

        enemyList = new ArrayList<>();

        for( int j = 0; j<img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++) {

                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                
                if(value == NIGHT_BORNE || value == HELL_BOUND || value == GHOST){
                    enemyList.add(enemyFactory.MakeEnemy(value, i * Game.TILES_SIZE, j * Game.TILES_SIZE, levelData));
                }
            }
        }
    }

    //Metodo che sposta la camera dipendentemente dalla posiizone del player
    private void calculateLevelOffsets() {
        levelTileWide = image.getWidth();
        maxTileOffset = levelTileWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE * maxTileOffset;
        
        levelTileHeight = image.getHeight();
        maxTileOffsetY = levelTileHeight - Game.TILES_IN_HEIGHT;
        maxLevelOffsetY = Game.TILES_SIZE * maxTileOffsetY;

    }

    //Funzione che ritorna il valore RGB riconosciuto durante l'estrazione delle informazioni dalle immagini levelData
    //della posizione scelta
    public int getSpriteIndex(int x, int y){
        return levelData[y][x];
    }

    //Getters e setters
    public int[][] getLD(){
        return levelData;
    }

    public int getLevelOffset(){
        return maxLevelOffsetX;
    }

    public int getLevelOffsetY() {
        return maxLevelOffsetY;
    }

    public ArrayList<AbstractEnemy> getEnemies(){
        return enemyList;
    }

    public ArrayList<Potion> getPotions(){
        return potions;
    }

    public ArrayList<LootBox> getLootBoxes(){
        return lootBoxes;
    }

    public ArrayList<Spike> getSpike(){
        return spikes;
    }

    public ArrayList<Cannon> getCannons(){
        return cannons;
    }

    public Point getPlayerSpawnPoint(){
        return playerSpawnPoint;
    }


    


}
