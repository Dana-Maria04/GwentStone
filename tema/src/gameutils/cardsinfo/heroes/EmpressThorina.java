package gameutils.cardsinfo.heroes;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

/**
 * Represents the hero Empress Thorina with the special ability "Low Blow."
 */
public class EmpressThorina extends Hero {

    /**
     * Constructs a new Empress Thorina hero
     *
     * @param card the card containing the hero's details
     */
    public EmpressThorina(final Cards card) {
        super(card);
    }

    /**
     * Applies the hero's "Low Blow" ability to a specified row of minions.
     * This ability removes the minion with the highest health from the row
     *
     * @param minionsRow the row of minions to which the ability will be applied
     */
    @Override
    public void ability(final LinkedList<Minions> minionsRow) {
        Minions minionsMaxHealth = minionsRow.getFirst(); // Start with the first minion
        int maxHealthIdx = 0;
        int currentIdx = 0;

        for (Minions minions : minionsRow) {
            if (minions.getCard().getHealth() > minionsMaxHealth.getCard().getHealth()) {
                minionsMaxHealth = minions; // Update max health minion
                maxHealthIdx = currentIdx; // Update index of max health minion
            }
            currentIdx++;
        }
        minionsRow.remove(maxHealthIdx); // Remove the minion with the highest health
    }
}
