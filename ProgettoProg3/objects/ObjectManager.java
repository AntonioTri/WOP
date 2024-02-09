package Progetto_prog_3.objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Progetto_prog_3.Game;
import Progetto_prog_3.GameStates.Playing;
import Progetto_prog_3.entities.Player;
import Progetto_prog_3.levels.Level;
import Progetto_prog_3.objects.Prototype.Cloningfactory;
import Progetto_prog_3.utils.LoadSave;
import static Progetto_prog_3.utils.Constants.ObjectConstants.*;
import static Progetto_prog_3.utils.Constants.Projectiles.CannonBall.CANNON_BALL_HEIGHT;
import static Progetto_prog_3.utils.Constants.Projectiles.CannonBall.CANNON_BALL_WIDTH;
import static Progetto_prog_3.utils.HelpMetods.canCannonSeePlayer;
import static Progetto_prog_3.utils.HelpMetods.isPlayereInFrontOfCannon;
import static Progetto_prog_3.utils.HelpMetods.isPlayereInRange;
import static Progetto_prog_3.utils.HelpMetods.projectileHittingWall;

public class ObjectManager {
    
    private Playing playing;
    private BufferedImage[][] potionImages, boxesImages;
    private BufferedImage[] cannonImages;
    private BufferedImage spikeImage, cannonBallImage;
    private ArrayList<Potion> potions;
    private ArrayList<LootBox> lootBoxes;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<CannonBall> cannonBalls = new ArrayList<>();
    //Cloning factory per il prototype pattern
    private Cloningfactory cloningfactory = new Cloningfactory();

    private Random rand;

    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImages();
        rand = new Random();
    }

    public void loadObjects(Level level) {
    
        potions = new ArrayList<>(level.getPotions());
        lootBoxes =  new ArrayList<>(level.getLootBoxes());
        spikes = new ArrayList<>(level.getSpike());
        cannons = new ArrayList<>(level.getCannons());
        cannonBalls.clear();

    }

    //Semplice metdo per osservare se il player sta toccando le spike, in tal caso, morte istantanea, il gioco è molto punitivo :D
    public void checkSpikesTouched(Player p){
        for (Spike s : spikes) {
            if (s.getHitbox().intersects(p.getHitbox())) {
                p.die();
            }
        }
    }

    //Questa funzione ci permette di controllare se il player hoverlaps, cammina sopra, una pozione, in tal caso, la pozione viene 
    //settata in stato di falso e quindi non sarà più raccoglibile e scomparirà dalla scena, ed al Player viene associato l'effetto
    //della pozione che ha appena raccolto
    public void checkPlayerTouchedPotions(Rectangle2D.Float hitbox){
        
        for (Potion potion : potions) {
            if (potion.isActive()) {
                if (hitbox.intersects(potion.hitbox)) {
                    applyEffectToPlayer(potion);
                    potion.setActive(false);

                }
            }
        }
    }

    //La funzione che applica al player l'effetto della pozione raccolta
    public void applyEffectToPlayer(Potion potion){

        if (potion.getObjType() == RED_POTION) {
            //Alla pozione rossa viene associato un recuper istantante di salute
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        } else {
            //Alla pozione bli viene associata una maggiore rigenerazione dell'energia
            playing.getPlayer().getStatusManager().fastEnergyRecharge(playing.getPlayer(), 5);
        }

    }

    /*
     * Questo metodi esegue una scansione tra tutte le lootbox presenti nella mappa, se una certa scatola è attiva, che significa che non è mai stata colpita, 
     * e sta intersecando la attackbox del player, allora viene settata la sua animazione a true, per mostrare che si rompe, e vengono instanziate delle 
     * variabili per gestire il tipo di pozione che deve spawnare.
     * 
     * Viene fatto un successivo controllo su una flag della lotbox, quella che permette lo spawn di un oggetto, è vera di default pertanto alla prima iterazione 
     * il codice vcerrà sempre eseguito, viene spawnata la pozione al posto della scatola appena rottae viene settata la flag di spawn pozione a falso
     * 
     */
    public void checkObjectHit(Rectangle2D.Float playerAttackBox, int areaAttack){

        for (LootBox box : lootBoxes) {
            if (box.isActive() && box.getHitbox().intersects(playerAttackBox)) {
        
                box.setAnimation(true);
                
                int percentile = rand.nextInt(10);
                int type = (percentile > 5) ? 0 : 1;
                int offset = 9;

                if (box.getObjType() == BARREL) {
                    offset = 0;
                }

                //Momento di spawn di una pozione al posto di una loot box a momento della distruzione di questa
                if (box.getCanSpawnPotion()) {
                    potions.add(new Potion( (int)(box.getHitbox().x - 5 + box.getHitbox().width / 2),
                                            (int)(box.getHitbox().y - offset),
                                            type));
                    box.setCanSpawnPotion(false);
                }

                //Nel caso arrivi una flag di attacco ad area, viene fatto il controllo su tutti gli oggetti
                //invece che fermarsi al primo oggetto colpito
                if(areaAttack == 0) return;
                
            }
        }
    }

    /*
     * In questo metodo troviamo tutta la logica di carica delle immagini di lootbox, pozioni e spine
     * Viene creata una buffered image per ogni elemento, e con due cicli for innestati si prendono le subimage dagli sprite
     */
    private void loadImages() {

        //Si carica l'immagine delle spine
        spikeImage = LoadSave.getSpriteAtlas(LoadSave.SPIKE_ATLAS);
        //Viene caricata l'immagine della palla di cannone
        cannonBallImage = LoadSave.getSpriteAtlas(LoadSave.CANNON_BALL);

        //Si caricanole immagini delle pozioni
        BufferedImage potionSprites = LoadSave.getSpriteAtlas(LoadSave.POTIONS);
        potionImages = new BufferedImage[2][7];

        for (int j = 0; j < potionImages.length; j++) {
            for (int i = 0; i < potionImages[j].length; i++) {
                potionImages[j][i] = potionSprites.getSubimage(12 * i, 16 * j, 12, 16);
            }
        }

        //Si caricanole immagini dei contenitori
        BufferedImage boxesSprites = LoadSave.getSpriteAtlas(LoadSave.BOXES_SPRITE);
        boxesImages = new BufferedImage[2][8];

        for (int j = 0; j < boxesImages.length; j++) {
            for (int i = 0; i < boxesImages[j].length; i++) {
                boxesImages[j][i] = boxesSprites.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }

        //Si caricanole immagini dei cannoni
        cannonImages = new BufferedImage[7];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImages.length; i++) {
            cannonImages[i] = temp.getSubimage(40 * i, 0, 40, 26);
        }
    }

    //La classica funzione update per eseguire l'incremento dell'animation tick dell'oggetto
    public void update(int[][] levelData, Player player){
        
        updateLootBoxes();
        updatePotions();
        updateCanons(levelData, player);
        updateProjectiles(levelData, player);
    }

    private void updatePotions(){

        for (Potion potion : potions) {
            if (potion.isActive()) {
                potion.update();
            }
        }
    }

    private void updateLootBoxes(){
        for (LootBox box : lootBoxes) {
            if (box.isActive()) {
                box.update();
            }
        }
    }

    /*
     * Se il cannone non sta faceendo l'animazione
     * Controlliamo se la y è la stessa
     * se il player è in range
     * se si trova di frontee
     * il canone deve sparare
     */

    private void updateCanons(int[][] levelData, Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation 
                && ( c.getCannonTyleY() == player.getPlayerTileY()) //Qua viene agiunto un + 1 peerchè la y del player si trova su un blocco più in alto
                && isPlayereInRange(c, player) 
                && isPlayereInFrontOfCannon(c, player)
                && canCannonSeePlayer(levelData, player.getHitbox(), c.getHitbox(), c.getCannonTyleY())) {
                
                    c.setAnimation(true);
            
            }
            
            c.update();
            //Il cannone spara soltanto quando il suo frame di animazione è a 4
            if (c.getAniIndex() == 4 && c.getAniTick() == 0) {
                shootCannon(c);
            }
        }
    }

    private void shootCannon(Cannon c) {

        c.setAnimation(true);

        CannonBall cannonBall = (CannonBall)cloningfactory.getClone(c.getCannonBall());
        //adjustCannonBall(cannonBall, c);
        //!!QUA PROVIAMO AD AGGIUNGERE UN PROTOTYPE
        cannonBalls.add(cannonBall);

    }

    //Questo metodo esegue i controlli sulla palla di cannone, se questa è attiva si esegue l'update dlla sua posizione
    //Si controlla successivamente se sta colpeendo il player, in tal caso gli si applica il danno
    //Altrimeenti si controlla se stia colpendo un muro
    private void updateProjectiles(int[][] levelData, Player player) {
        for (CannonBall cb : cannonBalls) {
            //Update della posizione
            if (cb.getActive()) {
                cb.updatePosition();
            }
            //Controllo della collisione con il player
            if (player.getHitbox().intersects(cb.getHitbox()) && cb.getCanDoDamage()) {
                player.changeHealth(0);
                cb.setActive(false);
                cb.setCanDoDamage(false);

            //Controllo della collisione con un muro
            } else if (projectileHittingWall(cb, levelData)) {
                cb.setActive(false);
                cb.setCanDoDamage(false);

            }
        }
    }

    //Classico metodo draw che richiama tutti i metodi draw per disegnare i singoli oggetti
    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){
        drawPotions(g, xLevelOffset, yLevelOffset);
        drawBoxes(g, xLevelOffset, yLevelOffset);
        drawTraps(g,xLevelOffset, yLevelOffset);
        drawCannons(g, xLevelOffset, yLevelOffset);
        drawCannonBall(g, xLevelOffset, yLevelOffset);
    }

    //Metodo per disegnare i cannoni
    private void drawCannons(Graphics g, int xLevelOffset, int yLevelOffset) {
        for (Cannon c : cannons) {

            int X = (int)(c.getHitbox().x - xLevelOffset);
            int Y = (int)(c.getHitbox().y - yLevelOffset);
            int width = CANNON_WIDTH;
            int height = CANNON_HEIGHT;

            if (c.getObjType() == CANNON_RIGHT) {
                X += width;
                Y += height - (25 * Game.SCALE);
                width *= -1;
                
            }

            g.drawImage(cannonImages[c.getAniIndex()], X, Y, width, CANNON_HEIGHT, null);
        
        }
    }

    //Metodo per disegnare le palle di cannone
    private void drawCannonBall(Graphics g, int xLevelOffset, int yLevelOffset) {
        for (CannonBall p : cannonBalls) {
            if (p.getActive()) {
                g.drawImage(cannonBallImage, (int)(p.getHitbox().x - xLevelOffset), (int)(p.getHitbox().y - yLevelOffset ), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    //Metodo per disegnare le scatole
    private void drawBoxes(Graphics g, int xLevelOffset, int yLevelOffset) {
        
        for (LootBox box : lootBoxes) {
            
            int type = 0;
            
            if (box.isActive() && box.getObjType() == BARREL) {
                type = 1;
            } 

            if (box.isActive()) {
                g.drawImage(boxesImages[type][box.getAniIndex()],
                    (int)(box.getHitbox().x - box.getxDrawOffset() - xLevelOffset),
                    (int)(box.getHitbox().y - box.getyDrawOffset() - yLevelOffset),
                    CONTAINER_WIDTH,
                    CONTAINER_HEIGHT, 
                    null);
    
                box.drawHitbox(g, xLevelOffset, yLevelOffset);

            }
        }
    }

    //Metodo per disegnare le pozioni
    private void drawPotions(Graphics g, int xLevelOffset, int yLevelOffset) {

        for (Potion potion : potions) {

            int type = 0;
            
            if (potion.isActive() && potion.getObjType() == RED_POTION) {
                type = 1;
            } 

            if (potion.isActive()) {
                g.drawImage(potionImages[type][potion.getAniIndex()],
                    (int)(potion.getHitbox().x - potion.getxDrawOffset() - xLevelOffset),
                    (int)(potion.getHitbox().y - potion.getyDrawOffset() - yLevelOffset),
                    POTION_WIDTH,
                    POTION_HEIGHT, 
                    null);
                
                potion.drawHitbox(g, xLevelOffset, yLevelOffset);

            }
        }
    }

    //Metodo per disegnare le spine
    private void drawTraps(Graphics g, int xLevelOffset, int yLevelOffset) {

        for (Spike spike : spikes) {
            g.drawImage(spikeImage, (int)(spike.getHitbox().x - xLevelOffset), (int) (spike.getHitbox().y - yLevelOffset) - spike.getyDrawOffset(), SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }

    }

    //Metodo reset per riportaare allo stato di partenza tutti gli oggetti interagibili, lootbox e pozioni
    public void resetAllObjects() {

        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion potion : potions)
            potion.reset();
        for (LootBox box : lootBoxes)
            box.reset();
        for (Cannon cannon: cannons) 
            cannon.reset();

    }

    public ArrayList<LootBox> getLootBoxs(){
        return lootBoxes;
    }

    public void setLootBoxs(ArrayList<LootBox> lootBoxes){
        this.lootBoxes = lootBoxes;
    }
    

}
