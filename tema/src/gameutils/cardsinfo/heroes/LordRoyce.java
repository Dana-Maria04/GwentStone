package gameutils.cardsinfo.heroes;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

/**
 * Represents the hero Lord Royce with the special ability "Sub Zero."
 */
public class LordRoyce extends Hero {

    /**
     * Constructs a new Lord Royce hero
     *
     * @param card the card containing the hero's details
     */
    public LordRoyce(final Cards card) {
        super(card);
    }

    /**
     * Applies the hero's "Sub Zero" ability to the target row of minions.
     * This ability freezes all the minions in the row until next turn
     *
     * @param minionsRow the row of minions to which the ability will be applied
     */
    @Override
    public void ability(final LinkedList<Minions> minionsRow) {
        for (Minions minions : minionsRow) {
            minions.setIsFrozen(1);
        }
    }
}
