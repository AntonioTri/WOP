package Progetto_prog_3.GameStates.Savings;

import Progetto_prog_3.entities.Player;

public class Originator {

    private Player player;

    public void setPlayer(Player player){
        this.player = player;
    }


    public Memento storeInMemento(){
        return new Memento(player);
    }


}
