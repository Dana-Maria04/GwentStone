package gameutils;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.heroes.Hero;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import static gameutils.GameConstants.*;

public class Table {

    private ArrayList<Cards> deckP1 = new ArrayList<>();

    private ArrayList<Cards> deckP2 = new ArrayList<>();


    private ArrayList<LinkedList<CardInput>> table;

    public Table(){
        //intiliaze table
        table = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            table.add(new LinkedList<>());
        }
    }

    public ArrayList<Cards> getDeckP2() {
        return deckP2;
    }

    public void setDeckP2(ArrayList<Cards> deckP2) {
        this.deckP2 = deckP2;
    }

    public ArrayList<LinkedList<CardInput>> getTable() {
        return table;
    }

    public void setTable(ArrayList<LinkedList<CardInput>> table) {
        this.table = table;
    }

    public ArrayList<Cards> getDeckP1() {
        return deckP1;
    }

    public void setDeckP1(ArrayList<Cards> deckP1) {
        this.deckP1 = deckP1;
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
