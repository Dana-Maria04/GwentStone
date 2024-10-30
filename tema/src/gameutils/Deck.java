package gameutils;

import gameutils.cardsinfo.Cards;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Deck { //final?
    private ArrayList<Cards> cards;

    private ArrayList<Cards> deckP1 = new ArrayList<>();

    private ArrayList<Cards> deckP2 = new ArrayList<>();

    public Deck() {
        //default constructor
        cards = new ArrayList<>();
    }

    //getter
    public ArrayList<Cards> getCards() {
        return cards;
    }

    public ArrayList<Cards> getDeckP1() {
        return deckP1;
    }

    public void setDeckP1(ArrayList<Cards> deckP1) {
        this.deckP1 = deckP1;
    }

    public ArrayList<Cards> getDeckP2() {
        return deckP2;
    }

    public void setDeckP2(ArrayList<Cards> deckP2) {
        this.deckP2 = deckP2;
    }

    //setter
    public void setCards(ArrayList<Cards> cards) {
        this.cards = cards;
    }

    public void shuffleDeck(int seed){
        Random shuffle = new Random(seed);
        Collections.shuffle(cards, shuffle);
    }

    public Cards drawCard(){
        if(!cards.isEmpty()){
            return cards.remove(0); // return first card
        }
        return null; // empty deck
    }

    public void addCard(Cards card){
        cards.add(card);
    }

    public void resetDeck(ArrayList<Cards> initialCards){
        cards = new ArrayList<>(initialCards);
    }
}
