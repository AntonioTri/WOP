package Progetto_prog_3.levels;

import static Progetto_prog_3.Game.TILES_SIZE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Progetto_prog_3.Game;
import Progetto_prog_3.GameStates.GameState;
import Progetto_prog_3.entities.MementoSavings.EnemyMemento;
import Progetto_prog_3.entities.MementoSavings.PlayerMemento;
import Progetto_prog_3.entities.enemies.AbstractEnemy;
import Progetto_prog_3.utils.LoadSave;

public class LevelManager {
 
    Game game;
    BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0;
    private boolean gameStarted = true;

    public LevelManager(Game game){
        this.game = game;
        importSprite();
        buildAllLevels();
        
    }
    
    private void buildAllLevels() {
        levels = new ArrayList<>();
        BufferedImage[] alllevels = LoadSave.getAllLevels();
        for (BufferedImage img : alllevels) {
            levels.add(new Level(img));
        }
    }

    //Il seguente metodo importa i blochetti del terreno da posizionare poi nella mappa
    private void importSprite() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int j = 0; j<4; j++){
            for (int i = 0; i < 12; i++) {
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*Game.TILES_DEFAULT_SIZE, j * Game.TILES_DEFAULT_SIZE,Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
            }
        }
    }

    //Qui disegnamo la mapa di gioco
    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){

        for(int j = 0; j< levels.get(levelIndex).getLD().length; j++){
            //Questo è un array list, possiamo accedere ad un elemento della lista tramite indice e con il metodo get()
            for (int i = 0; i < levels.get(levelIndex).getLD()[0].length; i++){
                int index = levels.get(levelIndex).getSpriteIndex(i, j);
                //Quì viene aggiunto l'offset in x per spostare il rendering dlla mappa a dare l'illusione del movimento
                g.drawImage(levelSprite[index], TILES_SIZE*i - xLevelOffset, TILES_SIZE*j - yLevelOffset, TILES_SIZE, TILES_SIZE, null);
                
            }
        }
    }

    //Questa funzione viene eseguita una sola volta in tutto il gioco. 
    //Crea un punto di salvataggio nel memento con lo stato iniziale di tutte le entità per il primo livello
    public void update(){
        if (gameStarted) {
            createSavingPoint();
            gameStarted = false;
        }
    }

    /*
     * Il seguente metodo quando chiamato carica il prossimo livello
     * Esegue diversi passaggi, aumenta l'indice del livello, così da poter scorrere al successivo
     * Se il numero dell'indice è maggiore o uguale al numero di livello disponibili, viene resettato a 0
     * e si viene riportati al menù di partenza.
     * 
     * Se non fosse così verrebbe caricato il livello successivo andando a prendere dall'array list,
     * l'elemento di indice 'levelIndex' tramite il metodo get(), dal game scendiamo allo stato di Playing e
     * aggiungiamo i nemici in base ai nuovi dati del livello ottenuti, diamo al player le informazioni del livello
     * per permettergli di interagire con l'ambiente, e viene settato il massimo livello di spostamento della telecamerta
     * 
    */
    public void loadNextLevel() {
        levelIndex++;
        if (levelIndex >= levels.size()) {
            levelIndex = 0;
            System.out.println("You completed all the levels!");
            GameState.state = GameState.MENU;
        }
        
        //Si passa al livello successivo
        Level newLevel = levels.get(levelIndex);

        //Si caricano gli offset per la telecamera
        game.getPlaying().setMaxLevelOffsetX(newLevel.getLevelOffset());
        game.getPlaying().setMaxLevelOffsetY(newLevel.getLevelOffsetY());

        //Si caricano le informazioni nel player
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLD());
        game.getPlaying().getPlayer().setSpawnPoint(getCurrentLevel().getPlayerSpawnPoint());

        //si aggiungoono nemici e lootboxes
        game.getPlaying().getEnemyManager().addEnemies(newLevel);
        game.getPlaying().getObjectManager().loadObjects(newLevel);

        createSavingPoint();

        

    }

    //La funzione che crea un punto di salvataggio
    public void createSavingPoint(){
        
        //Con il seguente codice andiamo a salvare lo stato di tutti i nemici ad inizio di un livello
        //Conserviamo lo stato uno ad uno dei nemici in un nuvo memento che aggiungiamo ad una array list di memento 
        AbstractEnemy currentEnemy;
        ArrayList<EnemyMemento> enemyMemento = new ArrayList<>();
        int numberOfCurrentEnemyes = game.getPlaying().getEnemyManager().getEnemyList().size();
        
        for (int i = 0; i < numberOfCurrentEnemyes; i++) {
            
            currentEnemy = game.getPlaying().getEnemyManager().getEnemyList().get(i);
            enemyMemento.add(new EnemyMemento(currentEnemy));
            
        }
        
        //Creiamo poi il memento del player e dei nemici presenti all'inizio del livello
        game.getPlaying().getMementoManager().addPlayerMemento(new PlayerMemento(game.getPlaying().getPlayer()));
        game.getPlaying().getMementoManager().addEnemyMemento(enemyMemento);

    }

    //Getters e Setters
    public Level getCurrentLevel(){
        return levels.get(levelIndex);
    }

    public int getAmountOfLevels(){
        return levelIndex;
    }

    public int getLevelIndex(){
        return levelIndex;
    }

}
