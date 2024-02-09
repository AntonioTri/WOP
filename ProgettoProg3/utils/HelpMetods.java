package Progetto_prog_3.utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Progetto_prog_3.Game;
import Progetto_prog_3.entities.Player;
import Progetto_prog_3.objects.AbstractProjectile;
import Progetto_prog_3.objects.Cannon;
import Progetto_prog_3.objects.LootBox;
import Progetto_prog_3.objects.Potion;
import Progetto_prog_3.objects.Spike;
import static Progetto_prog_3.utils.Constants.ObjectConstants.*;


public class HelpMetods {
    
    //Questa funzione invece l'ho trovata su internet, non so come funzioni di preciso, utilizza i colori rgb per mappare le
    //caratteristiceh del livello dallàimmagine level_one_data.png, mappato il terreno poi possono essere posizionati i mattoncini giusti
    //per la costruzione del livello
    public static int[][] getLevelData(BufferedImage img){

        int[][] levelData = new int [img.getHeight()][img.getWidth()];

        for( int j = 0; j<img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++) {

                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                
                if(value >= 48)
                    value = 11;

                levelData[j][i] = value; 
            }
        }

        return levelData;

    }

    //Questa funzione restituisce il punto di spawn del player
    public static Point GetPlayerSpawnPoint(BufferedImage img){

        for( int j = 0; j<img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++) {

                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                
                if(value == 255){
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
                }
            }
        }

       return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);

    }

    //I Successivi due metodi sono quasi identici al precedente, viene di fatta l'aggiunta una variabile randomica per far spawnare
    //Una pozione rossa od una pozione blu per il primo metodo, e per il secondo un barile oppure una cassa, tutte e due con una probabilità
    //Al momento del 50%
    public static ArrayList<Potion> getPotions(BufferedImage img){

        ArrayList<Potion> list = new ArrayList<>();

        for( int j = 0; j<img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++) {
                
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();

                Random randValue = new Random();
                int x = randValue.nextInt(10);

                if(value == 124){
                    if (x<5) {
                        list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, RED_POTION));
                    } else {
                        list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, BLUE_POTION));
                    }
                } 
            }
        }

        return list;

    }

    public static ArrayList<LootBox> getLootBoxes(BufferedImage img){

        ArrayList<LootBox> list = new ArrayList<>();

        for( int j = 0; j<img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++) {

                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                Random randValue = new Random();
                int x = randValue.nextInt(10);

                if(value == 123){
                    if (x<5) {
                        list.add(new LootBox(i * Game.TILES_SIZE, j * Game.TILES_SIZE, BOX));
                    } else {
                        list.add(new LootBox(i * Game.TILES_SIZE, j * Game.TILES_SIZE, BARREL));
                    }
                } 
            }
        }

        return list;

    }

    //Il seguente metodo posiziona le spine dentro la mappa di gioco seguendo la stessa logica di tutti i metodi di questo tipo
    public static ArrayList<Spike> getSpikes(BufferedImage img) {
        
        ArrayList<Spike> list = new ArrayList<>();

        for( int j = 0; j<img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++) {

                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();

                if(value == SPIKE){
                    list.add(new Spike((int)(i * Game.TILES_SIZE), (int)(j * Game.TILES_SIZE), SPIKE));

                } 
            }
        }

        return list;
    }

    //Il seguente metodo posiziona i cannoni dentro la mappa di gioco seguendo la stessa logica di tutti i metodi di questo tipo
    public static ArrayList<Cannon> getCannons(BufferedImage img) {
        
        ArrayList<Cannon> list = new ArrayList<>();

        for( int j = 0; j<img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++) {

                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();

                if(value == CANNON_LEFT || value == CANNON_RIGHT){
                    list.add(new Cannon((int)(i * Game.TILES_SIZE), (int)(j * Game.TILES_SIZE), value));
                }
            }
        }

        return list;
    }

    //Questa funzione ci indica de gli spazi attorno alla nostra entità sono solidi oppure no
    //Nel caso siano disponibili spazi in cui muoversi, viene ritornato vero, altrimenti se viene
    //ritornato falso, significa che la hitbox del personagio si sta compenetrando con un muro e viene impedito il movimento
    //in quella direzione
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!isSolid(x, y, lvlData))
			if (!isSolid(x + width, y + height, lvlData))
				if (!isSolid(x + width, y, lvlData))
					if (!isSolid(x, y + height, lvlData))
						return true;
		return false;
	}

    

        /*
     * Questo metodo dà intelligenza ai nemici, vengono prese le loro posizioni orizzontali e ne viene fata la differenza
     * se il player si trova a destra del nemico vengono fatti i controlli sulla destra edl nemico e lo stesso vale per la sinistra
     */

    public static boolean isPathClear(int[][] levelData, Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox, int enemyY){

        int playerX = (int)(playerHitbox.x / Game.TILES_SIZE);
        int enemyX = (int)(enemyHitbox.x / Game.TILES_SIZE);
        int difference; 


            if (playerX > enemyX) {
                
                difference = playerX - enemyX;
                //Si esegue una scansione tra i blocchi del nemico ed i blocchi del player su due livelli 
                //(in entrambi i casi sono ritornate le condizioni di verifica come valori booleani).

                //Alla loro altezza, per controllare che i blocchi tra nemico e player siano di aria 
                for (int i = 0; i < difference; i++) {
                    if (isTileSolid(enemyX + i, enemyY, levelData)) {
                        return false;
                    }
                    
                }
                
                //E soto i piedi del nemico, per controllare che tutti i blocchi siano solidi
                for (int i = 0; i < difference; i++) {
                    if (!isTileSolid(enemyX + i, enemyY + 1, levelData)) {
                        return false;
                    }
                    
                }
                
            //Lo stesso criterio di sopra viene applicato sulla sinistra del nemico
            } else if (playerX < enemyX){
                
                difference = enemyX - playerX;
                for (int i = 0; i < difference; i++) {
                    if (isTileSolid(enemyX - i, enemyY, levelData)) {
                       return false;
                    }
                }
    
                for (int i = 0; i < difference; i++) {
                    if (!isTileSolid(enemyX - i, enemyY + 1, levelData)) {
                        return false;
                    }
                }
    
            }
    
        //In ogni caso favorevole, ovvero quello in cui non ci sono ostacoli o burroni, viene ritornato vero
        //Per segnalare che il percorso sia lbero
        return true;


    }

    //Simile alla precedente ma viene soltanto controllato che il percorso sia libero e non che sia camminabile
    //Il cannone sta fermo sul posto, non si muove, peretantoha solo bisogno di controllaree che il player sia visibile
    public static boolean canCannonSeePlayer(int[][] levelData, Rectangle2D.Float hitbox, Rectangle2D.Float hitbox2, int cannonTyleY) {
        int firstXTile = (int)(hitbox.x / Game.TILES_SIZE);
        int secondXTile = (int)(hitbox2.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {

            return areAllTilesClear(secondXTile + 1, firstXTile, cannonTyleY, levelData);

        } else {
            
            return areAllTilesClear(firstXTile, secondXTile, cannonTyleY, levelData);

        }
    }

    public static boolean isPlayereInFrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x) {
                return true;
            }
        } else if (c.getHitbox().x < player.getHitbox().x) {
            return true;
        }

        return false;

    }

    
    public static boolean projectileHittingWall(AbstractProjectile projectile, int[][] levelData) {
        
        return isSolid(projectile.getHitbox().x + projectile.getHitbox().width / 2, projectile.getHitbox().y + projectile.getHitbox().height / 2, levelData);
        
    }

    public static boolean isPlayereInRange(Cannon c, Player player) {
        int absValue = (int)Math.abs(player.getHitbox().x - c.getHitbox().x);
        //Se la distanza in orizzontale è minore di una lungheza di attacco che vale un blocco
        //per 5, la condizione è vera e ritora vero, altrimenti falso
        return absValue <= Game.TILES_SIZE * 8;
    }

    public static boolean areAllTilesClear(int xStart, int xEnd, int y, int[][] levelData){

        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileSolid(xStart + i, y , levelData)) {
                return false;
            }
        }
        return true;
    }

    //Viene controllato in questo metodo che il percorso tra un blocco ed un altro blocco sia libero e che non ci siano ostacoli
    public static boolean areAllTileWalkable(int xStart, int xEnd, int y, int[][] levelData){
        if (areAllTilesClear(xStart, xEnd, y, levelData)) {
            for (int i = 0; i < xEnd - xStart; i++) {    
                if (!isTileSolid(xStart + i, y + 1, levelData)) {
                    return false;
                }
            }
        } else
            return false;

        return false;
    }

    //Questa funzione viene utilizzata dalla precedente per osservare se il pixel della direzione in cui ci si muove
    //appartenga ad un muro oppure no, viene verificata la non appartenenza al level data corrente
    public static boolean isSolid(float x , float y, int[][] levelData){

        //Si prende la grandezza del livello in larghezza (almeno per ora) per vedere se il movimento sia possibilitato
        int maxWidth = levelData[0].length * Game.TILES_SIZE;
        int maxHeight = levelData.length * Game.TILES_SIZE;
        if (x< 0 || x>= maxWidth) {
            return true;
        }
        if (y<0 || y>= maxHeight) {
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return isTileSolid((int)xIndex, (int)yIndex, levelData);
    
    }

    //Controlliamo qui se il blocco in questione sia solido
    public static boolean isTileSolid(int xTile, int yTile, int[][] levelData){

        if (xTile <= 0 ) xTile = 0;
        if (yTile <= 0 ) yTile = 0;
        if (xTile >= levelData[0].length) xTile = levelData[0].length;
        if (yTile >= levelData.length   ) yTile = levelData.length;

        int value = levelData[yTile][xTile];

        if (value >= 48 || value <0 || value != 11) {
            return true;
        } 

        return false;
        
    }

    //Questa funzione serve per le colisioni sui muri destra e sinistra, vengono calcolati gli offset, ovvero la distanza tra gli elementi
    //player e muri, e vengono risommati al movimento se questo sta facendo overlapping con del terreno
    public static float getEntityXPosNextWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
       
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}

    //La successiva è identica alla precedente, soltanto che analizza la possibilità del movimento in verticale, anzicchè in orizzontale
	public static float getEntityYPosFloorRoofRelative(Rectangle2D.Float hitbox, float airSpeed) {

		int currentTile = (int) ((hitbox.y + hitbox.height - 1) / Game.TILES_SIZE);

		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset;

		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}

    //Questa qui invece osserva se il player o in generale l'entità, sta toccando il terreno
    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        
        //Controllo sul pixel di estrema destra ed estrema sinistra, se sono entrambi non blocco, allora siamo in aria
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData) && !isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) {
            return false;
        }
        return true;
    }

    //Questa funzione ci permette di riconoscere se dove si sta muovendo l'entità sia un blocco solido oppure no
    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        //Se la velocità è positiva, ci stiamo muovendo verso destra e controlliamo l'angolo destro
        if (xSpeed > 0) {
            return isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData);
        //Altrimenti controlliamo l'angolo sinistro
        } else {
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
        }
    }

    



}
