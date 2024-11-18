package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

/**
 * Represents the minion "Disciple" with a special ability "God's Plan."
 */
public class Disciple extends Minions {

    /**
     * Constructs a new instance of the "Disciple" minion
     *
     * @param cardInput the card containing the minion's input data
     */
    public Disciple(final Cards cardInput) {
        super(cardInput);
    }

    /**
     * Applies the "God's Plan" ability to the specified target minion.
     * This ability increases the target minion's health by 2
     *
     * @param target the minion to which the ability will be applied
     */
    @Override
    public void ability(final Minions target) {
        int initialHealth = target.getCard().getHealth();
        target.getCard().setHealth(initialHealth + 2);
    }
}
