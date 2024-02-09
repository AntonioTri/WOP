package Progetto_prog_3.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;

public class LoadSave {

    //Stringe rapparesentative per ottenere una specifica immagine png da caricare
    //public static final String LEVEL_1_DATA = "level_one_data.png";
    
    //Enviroment
    public static final String LEVEL_ATLAS = "enviroment/Terrain.png";
    public static final String PLAYING_BACKGROUND_IMAGE = "enviroment/playing_bkgd_img.png";
    public static final String SMALL_CLOUDS = "enviroment/small_clouds.png";
    public static final String BIG_CLOUDS = "enviroment/big_clouds.png";
    public static final String SPIKE_ATLAS = "enviroment/trap_atlas.png";
    public static final String FOREST_LAYER_1 = "enviroment/background_layer_1.png";
    public static final String FOREST_LAYER_2 = "enviroment/background_layer_2.png";
    public static final String FOREST_LAYER_3 = "enviroment/background_layer_3.png";

    //UI
    public static final String MENU_BUTTONS = "ui/button_atlas.png";
    public static final String PAUSE_BACKGROUND = "ui/pause_menu.png";
    public static final String MENU_BACKGROUND = "ui/menu_background.png";
    public static final String SOUND_BUTTON = "ui/sound_button.png";
    public static final String VOLUME_BUTTON = "ui/volume_buttons.png";
    public static final String PRH_BUTTONS = "ui/prh_buttons.png";
    public static final String HOME_BACKGROUND_IMAGE = "ui/background_menu.png";
    public static final String HEALT_POWER_BAR = "ui/health_power_bar.png";
    public static final String LEVEL_COMPLITED = "ui/level_completed_sprite.png";
    public static final String DEATH_SCREEN = "ui/death_screen.png";
    public static final String OPTIONS_MENU = "ui/options_background.png";

    //Entity
    public static final String PLAYER_ATLAS = "entity/MageAnimations.png";
    public static final String NIGHT_BORNE_ATLAS = "entity/NightBorne.png";
    public static final String HELL_BOUND_ATLAS = "entity/HellBound.png";
    public static final String GHOST_ATLAS = "entity/Ghost.png";
    public static final String GHOST_ATTACK_BALL = "entity/Ghost_attack.png";
    public static final String CANNON_ATLAS = "entity/cannon_atlas.png";
    public static final String CANNON_BALL = "entity/ball.png";

    //Loot
    public static final String BOXES_SPRITE = "loot/objects_sprites.png";
    public static final String POTIONS = "loot/potions_sprites.png";
    
    //Special abilities
    public static final String PLAYER_EXPLOSION = "entity/circle_explosion.png";

    public static BufferedImage[] getAllLevels(){

        //Dichiariamo un URL contenente le risorse per il livello
        URL url = LoadSave.class.getResource("/Progetto_prog_3/res/lvls");
        File file = null;

        //Try catch per gestire gli errori
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //Dichiariamo due array di File, il primo importa tramite il metodo listFile, i file dell'url dichiarato sopra
        //Il secondo è semplicemente vuoto
        File[] files = file.listFiles();
        File[] sortedFiles = new File[files.length];

        //Viene eseguito un algoritmo poco efficiente per ordinare i file nel caso non vengano ordinati di default
        //In ogni caso fa 6 iterazioni
        for (int i = 0; i < sortedFiles.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    sortedFiles[i] = files[j];
                }
            }
        }

        //Memorizziamo i file come immagini in un aray di immagini
        BufferedImage[] imgs = new BufferedImage[sortedFiles.length];
        
        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(sortedFiles[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return imgs;

    }


    //Questa funzione carica i dati di un png in una immagine, date delle varibili, quelle sopra, sceglie quale immagine caricare
    //Questa dovrebbe essere una factory 
    public static BufferedImage getSpriteAtlas(String fileName){

        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/Progetto_prog_3/res/" + fileName);

        try {

            img = ImageIO.read(is);

        } catch (IOException e) {
            System.out.println("Immagine non caricata");
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return img;
    }

}
