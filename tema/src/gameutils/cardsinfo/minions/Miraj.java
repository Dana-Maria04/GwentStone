package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

/**
 * Represents the minion "Miraj" with a special ability "Skyjack."
 */
public class Miraj extends Minions {

    /**
     * Constructs a new instance of "Miraj" minion
     *
     * @param cardInput the card containing the minion's input data
     */
    public Miraj(final Cards cardInput) {
        super(cardInput);
    }

    /**
     * Applies the "Skyjack" ability to the specified target minion.
     * This ability swaps the health of this minion with the health of the target minion
     *
     * @param target the minion to which the ability will be applied
     */
    @Override
    public void ability(final Minions target) {
        int swapHealth = this.getCard().getHealth();
        this.getCard().setHealth(target.getCard().getHealth());
        target.getCard().setHealth(swapHealth);
    }
}
