package gameutils.cardsinfo;

import fileio.CardInput;
import java.util.ArrayList;

/**
 * Represents a card in the game
 */
public class Cards {

    private CardInput card;

    /**
     * Constructs a card with the given card's input data
     *
     * @param card the new card
     */
    public Cards(final CardInput card) {
        this.card = card;
    }

    /**
     * Constructs a card with the specified attributes
     *
     * @param name        the name of the card
     * @param mana        the mana cost of the card
     * @param description the description of the card
     * @param colors      the colors associated with the card
     */
    public Cards(final String name, final int mana, final String description,
                 final ArrayList<String> colors) {
        this.card = new CardInput(mana, 0, 0, description, colors, name);
    }

    /**
     * Decreases the card's health by the amount specified
     *
     * @param health the amount to decrease
     */
    public void decHealth(final int health) {
        this.card.setHealth(this.card.getHealth() - health);
    }

    /**
     * Increases the card's health by the amount specified
     *
     * @param health the amount to increase the health by
     */
    public void incHealth(final int health) {
        this.card.setHealth(this.card.getHealth() + health);
    }

    /**
     * Sets the CardInput of the card
     *
     * @param card the new card (input data)
     */
    public void setCard(final CardInput card) {
        this.card = card;
    }

    /**
     * Retrieves the card input data for this card
     *
     * @return the card input data
     */
    public CardInput getCardInput() {
        return this.card;
    }


    /**
     * Gets the card (its input data)
     *
     * @return the CardInput
     */
    public CardInput getCard() {
        return card;
    }
}
