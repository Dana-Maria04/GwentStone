package gameutils.cardsinfo.heroes;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

/**
 * Represents the hero King Mudface with the special ability "Earth Born."
 */
public class KingMudface extends Hero {

    /**
     * Constructs a King Mudface hero
     *
     * @param card the card information used to initialize the hero
     */
    public KingMudface(final Cards card) {
        super(card);
    }

    /**
     * Applies King Mudface's special ability, "Earth Born." to the target row of minions.
     * This ability increases the health of each minion in the row by 1
     *
     * @param minionsRow the row of minions on which the ability will be applied
     */
    @Override
    public void ability(final LinkedList<Minions> minionsRow) {
        for (Minions minion : minionsRow) {
            minion.incHealth(1);
        }
    }
}
