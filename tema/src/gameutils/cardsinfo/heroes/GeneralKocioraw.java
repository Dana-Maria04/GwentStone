package gameutils.cardsinfo.heroes;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

/**
 * Represents the hero General Kocioraw with the special ability "Blood Thirst."
 * This hero increases the attack damage of all minions in a specified row.
 */
public class GeneralKocioraw extends Hero {

    /**
     * Constructs a new General Kocioraw hero.
     *
     * @param card the card containing the hero's details.
     */
    public GeneralKocioraw(final Cards card) {
        super(card);
    }

    /**
     * Applies the hero's "Blood Thirst" ability to a specified row of minions.
     * This ability increases the attack damage of all minions in the row by 1.
     *
     * @param minionsRow the row of minions to which the ability will be applied.
     */
    @Override
    public void ability(final LinkedList<Minions> minionsRow) {
        for (Minions minions : minionsRow) {
            minions.incAttackDamage(minions, 1);
        }
    }
}
