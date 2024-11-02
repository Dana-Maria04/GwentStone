package gameutils;

import fileio.CardInput;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.LinkedList;

public class Hand {
    // Hand class for ony one player
    private LinkedList<CardInput> hand;

    public LinkedList<CardInput> getHand() {
        return hand;
    }

    public void setHand(LinkedList<CardInput> hand) {
        this.hand = hand;
    }

    public void addCard(CardInput card){
        this.hand.add(card);
    }

    public void removeCard(CardInput card){
        this.hand.remove(card);
    }
}
