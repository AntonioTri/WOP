package Progetto_prog_3.Audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Progetto_prog_3.entities.Player;

import static Progetto_prog_3.utils.Constants.PlayerConstants.JUMPING_UP;

public class AudioPlayer {
    
    //Lista delle soundtrack numerate
    public static int MENU_MUSIC = 0;
    public static int LEVEL_MUSIC = 1;

    //Lista di suoni numerati
    public static final int PLAYER_HURT = 0;
    public static int PLAYER_HURT_1 = 0;
    public static int PLAYER_HURT_2 = 1;
    public static int PLAYER_HURT_3 = 2;

    public static final int PLAYER_ATTACK = 12;
    public static int PLAYER_ATTACK_1 = 12;
    public static int PLAYER_ATTACK_2 = 13;
    public static int PLAYER_ATTACK_3 = 14;

    public static final int PLAYER_JUMPING = 3;
    public static int PLAYER_JUMPING_1 = 3;
    public static int PLAYER_JUMPING_2 = 4;

    public static final int PLAYER_LANDING = 5;
    public static int PLAYER_LANDING_1 = 5;
    public static int PLAYER_LANDIN_2 = 6;


    public static int WALKING_ON_GRASS = 7;
    public static int NIGHTBORRNE_DIE = 8;

    public static final int NIGHTBORNE_HURT = 9;
    public static int NIGHTBORNE_HURT_1 = 9; 
    public static int NIGHTBORNE_HURT_2 = 10;
    public static int NIGHTBORNE_HURT_3 = 11;

    public static int LEVEL_COMPLITED = 15;
    public static int GAME_OVER = 16;

    public static final int PLAYER_DASHING = 17;
    public static final int PLAYER_DASHING_1 = 17;
    public static final int PLAYER_DASHING_2 = 18;
    public static final int PLAYER_DASHING_3 = 19;

    public static final int PLAYER_EXPLOSION = 20;

    public static final int GHOST_ATTACK = 21;
    public static final int GHOST_ATTACK_1 = 21;
    public static final int GHOST_ATTACK_2 = 22;
    public static final int GHOST_ATTACK_3 = 23;
    
    //La clip è il modo di java di eseguirre suoni in un programma, è un contenitore capace di storare file .WAV
    private Clip[] songs, effects;

    //Variabili di ambiente
    private int currentSongId;
    private float volume = 0.5f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer(){
        loadSong();
        loadSoundEffects();
        playSong(MENU_MUSIC);
        
    }

    private void loadSong(){

        String[] names = {"game_sounds/menuMusic", "game_sounds/backgroundAudio"};
        songs = new Clip[names.length];

        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }

    }

    private void loadSoundEffects(){

        String[] effectsName = {"player_sounds/hurt1", "player_sounds/hurt2", "player_sounds/hurt3", "player_sounds/jumping1", "player_sounds/jumping2",
                                "player_sounds/landing", "player_sounds/landing2", "player_sounds/walking_on_grass",
                                "nightborne_sounds/nightBorneDie", "nightborne_sounds/nightBorneHurt1","nightborne_sounds/nightBorneHurt2","nightborne_sounds/nightBorneHurt3",
                                "player_sounds/attack1", "player_sounds/attack2", "player_sounds/attack3",
                                "game_sounds/lvlcompleted", "game_sounds/gameover",
                                "player_sounds/dash1", "player_sounds/dash2", "player_sounds/dash3", "player_sounds/playerFireExplosion",
                                "ghost_sounds/ghost_attack", "ghost_sounds/ghost_attack2", "ghost_sounds/ghost_attack3"};

        effects = new Clip[effectsName.length];

        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(effectsName[i]);
        }

        updateEffectsVolume();
    }

    private Clip getClip(String name){

        URL url = getClass().getResource("/Progetto_prog_3/res/audio/" + name + ".wav");
        AudioInputStream audio;

        try {

            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        
        return null;

    }


    private void updateSongVolume(){

        FloatControl gaincoControl = (FloatControl)songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gaincoControl.getMaximum() - gaincoControl.getMinimum();
        float gane = (range * volume) + gaincoControl.getMinimum();
        gaincoControl.setValue(gane);

    }


    private void updateEffectsVolume(){

        for (Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
    }



    public void togleSongMute(){
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl)(c.getControl(BooleanControl.Type.MUTE));
            booleanControl.setValue(songMute);
        }
        
    }


    public void togleEffectsMute(){
        this.effectMute = !effectMute;
        for (Clip sfx : effects) {
            BooleanControl booleanControl = (BooleanControl)(sfx.getControl(BooleanControl.Type.MUTE));
            booleanControl.setValue(effectMute);
        }

        if (!effectMute) {
            playEffect(JUMPING_UP);
        }

    }

    public void setVolume(float volume){
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong(){
        if (songs[0].isActive()) {
            songs[0].stop();
        }
        if (songs[1].isActive()) {
            songs[1].stop();
        }
    }

    public void setLevelSong(int lvlIndex){
        playSong(lvlIndex);
    }

    public void levelComplited(){
        stopSong();
        playEffect(LEVEL_COMPLITED);
    }
    
    public void playSetOfEffect(int EFFECT){

        int choosen = EFFECT + rand.nextInt(getNumberOfSounds(EFFECT));
        playEffect(choosen);

    }

    public void playattack(){

        if (effects[PLAYER_ATTACK].isActive() || effects[PLAYER_ATTACK + 1].isActive() || effects[PLAYER_ATTACK + 2].isActive()) {
            return;
        }

        int choosen = PLAYER_ATTACK + rand.nextInt(getNumberOfSounds(PLAYER_ATTACK));
        playEffect(choosen);

    }

    public void resetPlayerSoundBools(Player player){

        if (!effects[PLAYER_ATTACK].isActive() && !effects[PLAYER_ATTACK + 1].isActive() && !effects[PLAYER_ATTACK + 2].isActive()) {
            player.setCanPlayAtacksooound(true);
        }

    }

    public void playWalkingSound(boolean moving, boolean inAir, int currentHealth){

        if (inAir || !moving || currentHealth <= 0) {
            effects[WALKING_ON_GRASS].stop();
            return;

        } else if (!inAir && moving) {
            effects[WALKING_ON_GRASS].start();
        }

        if (effects[WALKING_ON_GRASS].isRunning()) {
            return;
        }
        
        effects[WALKING_ON_GRASS].setMicrosecondPosition(0);
        effects[WALKING_ON_GRASS].start();            
        
    }


    public void playEffect(int EFFECT) {
        
        effects[EFFECT].setMicrosecondPosition(0);
        effects[EFFECT].start();

    }

    public void playSong(int SONG){

        stopSong();

        currentSongId = SONG;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);

    }


    private int getNumberOfSounds(int SOUND){

        switch (SOUND) {
            case PLAYER_ATTACK: return 3;
            case PLAYER_JUMPING: return 2;
            case PLAYER_LANDING: return 2;
            case PLAYER_HURT: return 3;
            case NIGHTBORNE_HURT: return 3;
            case PLAYER_DASHING: return 3;
            case GHOST_ATTACK: return 3; 
            default: return SOUND;
        }
    }


}
