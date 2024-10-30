package gameutils;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.heroes.Hero;
import java.util.ArrayList;

public class Table {

    private ArrayList<Cards> deckP1 = new ArrayList<>();

    private ArrayList<Cards> deckP2 = new ArrayList<>();

    public ArrayList<Cards> getDeckP1() {
        return deckP1;
    }

    public void setDeckP1(ArrayList<Cards> deckP1) {
        this.deckP1 = deckP1;
    }

    public Table() {
        // default constructor
    }

    public Hero getHeroP1(){
        return deckP1.get(0).getHeroP1();
    }

    public Hero getHeroP2(){
        return deckP2.get(0).getHeroP2();
    }
}
