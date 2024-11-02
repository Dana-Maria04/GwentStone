package gameutils;

import fileio.CardInput;

import java.util.ArrayList;

public class Hand {
    // Hand class for ony one player
    private ArrayList<CardInput> hand;

    public ArrayList<CardInput> getHand() {
        return hand;
    }

    public void setHand(ArrayList<CardInput> hand) {
        this.hand = hand;
    }

    public void addCard(CardInput card){
        this.hand.add(card);
    }
}
