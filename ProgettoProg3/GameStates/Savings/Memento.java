package Progetto_prog_3.GameStates.Savings;

import Progetto_prog_3.entities.Player;

public class Memento {

    private final Player player;

    public Memento(Player player){

        this.player = player;

    }

    public Player getPlayer(){
        return player;
    }

}
