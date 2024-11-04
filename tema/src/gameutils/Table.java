package gameutils;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;
import gameutils.cardsinfo.heroes.Hero;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import static gameutils.GameConstants.*;

public class Table {

    private ArrayList<LinkedList<Minions>> table;

    public Table(){
        //intiliaze table
        table = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            table.add(new LinkedList<>());
        }
    }

    public ArrayList<LinkedList<Minions>> getTable() {
        return table;
    }

    public void setTable(ArrayList<LinkedList<Minions>> table) {
        this.table = table;
    }


//    public Table() {
//        // default constructor
//    }

//    public Hero getHeroP1(){
//        return deckP1.get(0).getHeroP1();
//    }
//
//    public Hero getHeroP2(){
//        return deckP2.get(0).getHeroP2();
//    }
}
