package Progetto_prog_3.GameStates.Savings;

import java.util.ArrayList;

public class Manager {

    ArrayList<Memento> savedStates = new ArrayList<>();

    public void addMemento(Memento m){
        savedStates.add(m);
    }

    public Memento getMemento(int index){
        return savedStates.get(index);
    }


}
