package Progetto_prog_3.utils;
import static Progetto_prog_3.utils.Constants.Projectiles.CannonBall.*;
import static Progetto_prog_3.utils.Constants.EnemtConstants.NightBorne.*;
import static Progetto_prog_3.utils.Constants.EnemtConstants.HellBound.*;
import static Progetto_prog_3.utils.Constants.EnemtConstants.Ghost.*;

import Progetto_prog_3.Game;

public class Constants {

    //Questa classe contiene dei valori costanti e statici che rappresentano 
    //il numero della riga della matrice contenente i frame della azione specificata
    //Ed un metodo che in base alla azione, o alla riga scelta, restituisce il numero
    //di frame ad esso associata

    //Alla chiamata in una clase, creiamo una variabile che contiene uno di questi valori
    //costanti ed utilizziamo la funzione getSpriteAmount per otenere il numero di
    //frame associati alla azione selezionata, così da poter dare una animazione fluida
    //in base alla azione scelta, senza andare a mostrare frame vuoti che comunque verranno inizializzati

    //Gravità
    public static final float GRAVITY = 0.04f * Game.SCALE;

    //Velocità dell'animazione
    public static final int ANI_SPEED = 25;


    //Variabili statiche per definire le caratteristiche delle loot boxes e delle pozioni
    public static class ObjectConstants {

		public static final int RED_POTION = 0;
		public static final int BLUE_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
        public static final int SPIKE = 52;

        public static final int CANNON_LEFT = 4;
        public static final int CANNON_RIGHT = 5;

		public static final int RED_POTION_VALUE = 15;
		public static final int BLUE_POTION_VALUE = 10;

		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
		public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
		public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int SPIKE_DEFAULT_WIDTH = 32;
        public static final int SPIKE_DEFAULT_HEIGHT = 32;
        public static final int SPIKE_WIDTH = (int)(Game.SCALE * SPIKE_DEFAULT_WIDTH);   
        public static final int SPIKE_HEIGHT = (int)(Game.SCALE * SPIKE_DEFAULT_HEIGHT);     
        
        public static final int CANNON_WIDTH_DEFAULT = 40;
		public static final int CANNON_HEIGHT_DEFAULT = 26;
		public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * Game.SCALE);
		public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * Game.SCALE);

        //Metodo per ottenere il numero di sprite giusto per ogni animazione di un nemico
		public static int getSpriteAmount(int objectType) {

			switch (objectType) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case BARREL, BOX:
				return 8;
            case CANNON_LEFT, CANNON_RIGHT:
                return 7;
			}
			return 1;
		}
	}


    //Variabili statiche per definire I nemici, per ora soltanto i nightbornes
    public static class EnemtConstants{

        public static class NightBorne {
            
            //Variabili per il nightborne
            public static final int NIGHT_BORNE = 0;

            public static final int NIGHT_BORNE_IDLE = 0;
            public static final int NIGHT_BORNE_RUN = 1;
            public static final int NIGHT_BORNE_ATTACK = 2;
            public static final int NIGHT_BORNE_HITTED = 3;
            public static final int NIGHT_BORNE_DIE = 4; 


            public static final int NIGHT_BORNE_DEFAULT_WIDHT = 80;
            public static final int NIGHT_BORNE_DEFAULT_HEIGHT = 80;

            public static final int NIGHT_BORNE_WIDHT = (int)(NIGHT_BORNE_DEFAULT_WIDHT * Game.SCALE);
            public static final int NIGHT_BORNE_HEIGHT = (int)(NIGHT_BORNE_DEFAULT_HEIGHT * Game.SCALE);
            
            //Questo è l'offset di posizionamento della hitbox del naightborne
            public static final int NIGHT_BORNE_DROW_OFFSET_X = (int)(20 * Game.SCALE);
            public static final int NIGHT_BORNE_DROW_OFFSET_Y = (int)(33 * Game.SCALE);
            
        }


        public static class HellBound {
            //Variabili per l'hell bound
            public static final int HELL_BOUND = 1;

            public static final int HELL_BOUND_JUMP = 0;
            public static final int HELL_BOUND_RUN = 1;
            public static final int HELL_BOUND_WALK = 2;
            public static final int HELL_BOUND_HIT = 3;
            public static final int HELL_BOUND_SLIDE = 4;
            public static final int HELL_BOUND_DIE = 5;

            public static final int HELL_BOUND_DEAFULT_WIDTH = 64;
            public static final int HELL_BOUND_DEAFULT_HEIGHT = 48;

            public static final int HELL_BOUND_WIDTH = (int)(HELL_BOUND_DEAFULT_WIDTH * Game.SCALE);
            public static final int HELL_BOUND_HEIGHT = (int)(HELL_BOUND_DEAFULT_HEIGHT * Game.SCALE);
            
            public static final int HELL_BOUND_DROW_OFFSET_X = (int)(9 * Game.SCALE);
            public static final int HELL_BOUND_DROW_OFFSET_Y = (int)(20 * Game.SCALE);
            
        }
        

        public static class Ghost {
            // Variabili per il ghost
            public static final int GHOST = 2;

            public static final int GHOST_SPAWN = 0;
            public static final int GHOST_IDLE = 1;  
            public static final int GHOST_ATTACK = 2;
            public static final int GHOST_TELEPORT = 3;
            public static final int GHOST_HIT = 4;
            public static final int GHOST_DIE = 5;
            public static final int GHOST_NOT_SPAWNED = 6;

            public static final int GHOST_DEFAULT_WIDTH = 64;
            public static final int GHOST_DEFAULT_HEIGHT = 52;

            public static final int GHOST_WIDTH = (int) (GHOST_DEFAULT_WIDTH * Game.SCALE);
            public static final int GHOST_HEIGHT = (int) (GHOST_DEFAULT_HEIGHT * Game.SCALE);

            public static final int GHOST_DRAW_OFFSET_X = (int) (23 * Game.SCALE);
            public static final int GHOST_DRAW_OFFSET_Y = (int) (5 * Game.SCALE);

            //Sono 15 frame
            public static final int GHOST_ELECTRIC_BALL_DEFAULT_LENGHT = 250;
            public static final int GHOST_ELECTRIC_BALL_LENGHT = (int)(250 * Game.SCALE);

        }



        //Metodo per ottenere il numero di sprite giusto per ogni animazione di un nemico
        //con uno switch sul tipo di nemico fa il return adeguato
        public static int getSpriteAmount(int enemyType, int enemyState){

            switch (enemyType) {
                case NIGHT_BORNE:
                    
                    switch (enemyState) {
                        case NIGHT_BORNE_IDLE: return 9;
                        case NIGHT_BORNE_RUN: return 6;
                        case NIGHT_BORNE_ATTACK: return 12;
                        case NIGHT_BORNE_HITTED: return 5;
                        case NIGHT_BORNE_DIE: return 23;
                        default:return 9;
                            
                    }

                case HELL_BOUND:
                    
                    switch (enemyState) {
                        case HELL_BOUND_WALK: return 12;
                        case HELL_BOUND_RUN: return 5;
                        case HELL_BOUND_JUMP: return 6;
                        case HELL_BOUND_HIT: return 5;
                        case HELL_BOUND_SLIDE: return 9;
                        case HELL_BOUND_DIE: return 10;
                        default: return 12;
                    }

                case GHOST:
                    
                    switch (enemyState) {
                        case GHOST_SPAWN: return 6;
                        case GHOST_IDLE: return 7;
                        case GHOST_ATTACK: return 15;
                        case GHOST_TELEPORT: return 7;
                        case GHOST_HIT: return 5;
                        case GHOST_DIE: return 8;
                        default: return 1;
                        
                    }
            
                default:
                return 0;
            }



        }

        //Metodo che ci permette di ritornare il valore di HP ( Punti vita ) dato uno specifico nemico
        public static int getMaxHealth(int enemyType){
            switch (enemyType) {
                case NIGHT_BORNE:
                    return 20;
                case HELL_BOUND:
                    return 10;
                case GHOST:
                    return 15;
                default:
                    return 0;
            }
        }

        //Metodo che ci permette di ritornare il valore di DANNO dato uno specifico nemico
        public static int getEnemyDamage(int enemyType){

            switch (enemyType) {
                case NIGHT_BORNE:
                    return 20;
                case HELL_BOUND:
                    return 5;
                case GHOST:
                    return 15;
                default:
                    return 0;
            }

        }

         //Metodo per ottenre la distanza di attacco
        public static int getAttackDistance(int enemyType){

            float distance;

            switch (enemyType) {
                case NIGHT_BORNE:
                    distance = 1.68f; 
                    break;
                case HELL_BOUND:
                    distance = 3f;
                    break;
                case GHOST:
                    distance = 4.5f;
                    break;
                default:
                    distance = 1f;
                    break;
            }

            return (int)(distance * Game.TILES_SIZE);

        }

        public static int getVisionDistance(int enemyType) {
            float distance;

            switch (enemyType) {
                case NIGHT_BORNE:
                    distance = 6f; 
                    break;
                case HELL_BOUND:
                    distance = 10f;
                    break;
                case GHOST:
                    distance = 7f;
                    break;
                default:
                    distance = 1f;
                    break;
            }

            return (int)(distance * Game.TILES_SIZE);

        }


    }

    //Variabili statiche per definire la grandezza dei proiettili
    public static class Projectiles{

        public static final int CANNON_BALL = 0;


        public static class CannonBall{
            public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
            public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
    
            public static final int CANNON_BALL_WIDTH = (int)(CANNON_BALL_DEFAULT_WIDTH * Game.SCALE);
            public static final int CANNON_BALL_HEIGHT = (int)(CANNON_BALL_DEFAULT_HEIGHT * Game.SCALE);
    
            public static final float CANNON_BALL_SPEED = 1f * Game.SCALE;
        }

        public static int getProjectileWidth(int objType){
            switch (objType) {
                case CANNON_BALL:
                    return CANNON_BALL_WIDTH;
            
                default:
                    return 10;
            }
        }

        public static int getProjectileHeight(int objType){
            switch (objType) {
                case CANNON_BALL:
                    return CANNON_BALL_HEIGHT;
            
                default:
                    return 10;
            }
        }

        public static float getProjectileSpeed(int objType){
            switch (objType) {
                case CANNON_BALL:
                    return CANNON_BALL_SPEED;
            
                default:
                    return 2;
            }
        }
    }

    public static class UI{
        //Variabili statiche per definire la grandezza dei bottoni
        public static class Buttons{
            public static final int BUTTON_DEFAULT_WIDTH = 140;
            public static final int BUTTON_DEFAULT_HEIGHT = 56;
            public static final int BUTTON_WIDTH = (int)(BUTTON_DEFAULT_WIDTH * Game.SCALE);
            public static final int BUTTON_HEIGHT = (int)(BUTTON_DEFAULT_HEIGHT * Game.SCALE);
        }

        //Variabili statiche per definire la grandezza dei bottoni di pausa
        public static class PauseButtons{

            public static final int SUOND_SIZE_DEFAULT = 42;
            public static final int SUOND_SIZE = (int)(SUOND_SIZE_DEFAULT * Game.SCALE);

        }

        //Variabili statiche per definire la grandezza dei bottoni di reset, home e resume
        public static class PhrButtons{

            public static final int PRH_BUTTONS_DEFAULT_SIZE = 56;
            public static final int PRH_BUTTONS_SIZE = (int) (PRH_BUTTONS_DEFAULT_SIZE * Game.SCALE);

        } 

        //Variabili statiche per definire la grandezza della barra del volume
        public static class VolumeButton{

            public static final int VOLUME_DEFAUT_HEIGHT = 44;
            public static final int VOLUME_DEFAUT_WIDTH = 28;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) ( VOLUME_DEFAUT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) ( VOLUME_DEFAUT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) ( SLIDER_DEFAULT_WIDTH * Game.SCALE);


        }

    }

    //Variabili statiche per definire i movimenti standard globali
    public static class Directions{

        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }

    //Variabili statiche per definire gli stati del player
    public static class PlayerConstants{

        public static final int IDLE = 0;
        public static final int WALKING = 1;
        public static final int RUNNING = 2;
        public static final int JUMPING_UP = 3;
        public static final int HEAVY_ATTACK = 4;
        public static final int LIGHT_ATTACK = 5;
        public static final int TROW_ATTACK = 6;
        public static final int JUMPING_DOWN = 7;
        public static final int HURT = 8;
        public static final int DIE = 9;
        public static final int USING_ULTIMATE = 10;

        public static final int PLAYER_DEFAULT_WIDTH = 128;
        public static final int PLAYER_DEFAULT_HEIGHT = 128;

        public static final int PLAYER_WIDTH = (int)(PLAYER_DEFAULT_WIDTH * Game.SCALE);
        public static final int PLAYER_HEIGHT = (int)(PLAYER_DEFAULT_HEIGHT * Game.SCALE);

        public static final int PLAYER_EXPLOSION_DEFAULT_WIDTH = 262;
        public static final int PLAYER_EXPLOSION_DEFAULT_HEIGHT = 252;
        public static final int PLAYER_EXPLOSION_HEIGHT = (int)(PLAYER_EXPLOSION_DEFAULT_HEIGHT * Game.SCALE);
        public static final int PLAYER_EXPLOSION_WIDTH = (int)(PLAYER_EXPLOSION_DEFAULT_WIDTH * Game.SCALE);
        public static final int PLAYER_EXPLOSION_DRAW_HEIGHT = (int)((PLAYER_EXPLOSION_DEFAULT_HEIGHT + 100) * Game.SCALE);
        public static final int PLAYER_EXPLOSION_DRAW_WIDTH = (int)((PLAYER_EXPLOSION_DEFAULT_WIDTH + 100) * Game.SCALE);

        //Metodo per ottenere il numero di sprite corrispetivo in base all'azione del player
        public static int getSpriteAmount(int PLAYER_ACTION){

            switch (PLAYER_ACTION) {
                case RUNNING:
                    return 8;

                case IDLE:
                    return 6; 

                case WALKING:
                    return 7; 

                case JUMPING_UP:
                    return 6; 

                case TROW_ATTACK:
                    return 7; 

                case LIGHT_ATTACK:
                    return 6; 

                case HEAVY_ATTACK:
                    return 10; 

                case JUMPING_DOWN:
                    return 5; 

                case HURT:
                    return 7; 

                case DIE:
                    return 4;
                
                case USING_ULTIMATE:
                    return 15;
            
                default:
                    return 1;
            }

        }

    }



}
