package Progetto_prog_3.entities.MementoSavings;

import java.util.ArrayList;

public class MementoManager {
    
    private ArrayList<PlayerMemento> playerMementoes;
    //Array list di array list, ogni elemento della arrya list coontiene una lista di nemici
    private ArrayList<ArrayList<EnemyMemento>> enemyMementos;


    public MementoManager(){
        playerMementoes = new ArrayList<>();
        enemyMementos = new ArrayList<>();
    }

    public void addPlayerMemento(PlayerMemento mementoToAdd){
        playerMementoes.add(mementoToAdd);
    }

    public PlayerMemento getPlayerMemento(int index){
        return playerMementoes.get(index);
    }

    public void addEnemyMemento(ArrayList<EnemyMemento> enemyList){
        enemyMementos.add(enemyList);
    }

    public ArrayList<EnemyMemento> getenemyMementoes(int index){
        return enemyMementos.get(index);
    }

}
