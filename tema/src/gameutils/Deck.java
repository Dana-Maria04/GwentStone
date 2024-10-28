package gameutils;

import gameutils.cardsinfo.Cards;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Deck { //final?
    private ArrayList<Cards> cards;

    public Deck() {
        //default constructor
        cards = new ArrayList<>();
    }

    //getter
    public ArrayList<Cards> getCards() {
        return cards;
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
