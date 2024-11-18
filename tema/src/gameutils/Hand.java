package gameutils;



import gameutils.cardsinfo.Cards;
import java.util.ArrayList;

/**
 * Represents the hand of a single player, which holds their cards during the game
 */
public class Hand {
    private final ArrayList<Cards> hand;

    /**
     * Gets the list of cards currently in the player's hand
     *
     * @return an ArrayList of Cards representing the player's hand
     */
    public ArrayList<Cards> getHand() {
        return hand;
    }

    /**
     * Initializes an empty hand for the player
     */
    public Hand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Adds a card to the player's hand
     *
     * @param card the card to be added to the hand
     */
    public void addCard(final Cards card) {
        this.hand.add(card);
    }

    /**
     * Removes a card from the player's hand
     *
     * @param card the card to be removed from the hand
     */
    public void removeCard(final Cards card) {
        this.hand.remove(card);
    }
}
