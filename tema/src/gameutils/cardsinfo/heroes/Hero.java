package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

import static gameutils.GameConstants.MAX_HEALTH;

/**
 * Represents a hero in the game, extending the functionality of a basic card
 */
public class Hero extends Cards {

    private int hasAttacked = 0;

    /**
     * Constructs a Hero and initializes its health to the maximum value
     *
     * @param card the card input data to initialize the hero
     */
    public Hero(final CardInput card) {
        super(card);
        this.getCard().setHealth(MAX_HEALTH);
    }

    /**
     * Constructs a Hero using an existing card
     *
     * @param card the card object to initialize the hero
     */
    public Hero(final Cards card) {
        super(card.getCardInput());
        this.hasAttacked = 0;
    }

    /**
     * Represents the ability of the hero. Meant to be overridden by subclasses for
     * specific hero abilities
     *
     * @param minionsRow the row of minions that the hero's ability targets
     */
    public void ability(final LinkedList<Minions> minionsRow) {
    }

    /**
     * Verifies if the hero is defensive
     *
     * @return 1 if the hero is defensive, otherwise 0
     */
    public int verifyDefensive() {
        if (this.getCard().getName().equals("King Mudface")
                || this.getCard().getName().equals("General Kocioraw")) {
            return 1;
        }
        return 0;
    }

    /**
     * Verifies if the hero is offensive
     *
     * @return 1 if the hero is offensive, otherwise 0
     */
    public int verifyOffensive() {
        if (this.getCard().getName().equals("Lord Royce")
                || this.getCard().getName().equals("Empress Thorina")) {
            return 1;
        }
        return 0;
    }

    /**
     * Sets if the hero has attacked in the current turn, or not
     *
     * @param hasAttacked 1 if the hero has attacked, 0 otherwise
     */
    public void setHasAttacked(final int hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Gets if the hero has attacked in the current turn, or not
     *
     * @return 1 if the hero has attacked, 0 otherwise
     */
    public int getHasAttacked() {
        return this.hasAttacked;
    }
}
