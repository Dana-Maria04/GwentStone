package gameutils;



import gameutils.cardsinfo.Cards;
import java.util.ArrayList;

public class Hand {
    // Hand class for ony one player
    private ArrayList<Cards> hand;

    public ArrayList<Cards> getHand() {
        return hand;
    }

    public Hand() {
        this.hand = new ArrayList<>();
    }


    public void addCard(Cards card){
        this.hand.add(card);
    }

    public void removeCard(Cards card){
        this.hand.remove(card);
    }
}
