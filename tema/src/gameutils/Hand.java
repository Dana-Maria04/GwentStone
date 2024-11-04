package gameutils;

import fileio.CardInput;


import gameutils.cardsinfo.Cards;
import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.LinkedList;

public class Hand {
    // Hand class for ony one player
    private ArrayList<Cards> hand;

    public ArrayList<Cards> getHand() {
        return hand;
    }

    public Hand() {
        this.hand = new ArrayList<>();
    }

    public void setHand(ArrayList<Cards> hand) {
        this.hand = hand;
    }

    public void addCard(Cards card){
        this.hand.add(card);
    }

    public void removeCard(Cards card){
        this.hand.remove(card);
    }
}
