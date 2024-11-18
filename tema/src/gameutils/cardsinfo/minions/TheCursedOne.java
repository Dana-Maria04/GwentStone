package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

/**
 * Represents the minion "The Cursed One" with a special ability "Shapeshift."
 */
public class TheCursedOne extends Minions {

    /**
     * Constructs "The Cursed One" minion
     *
     * @param cardInput the card containing the minion's input data
     */
    public TheCursedOne(final Cards cardInput) {
        super(cardInput);
    }

    /**
     * Applies the "Shapeshift" ability to the specified target minion.
     * This ability swaps the health and attack damage values of the target minion
     *
     * @param target the minion to which the ability will be applied
     */
    @Override
    public void ability(final Minions target) {
        int swapHealth = target.getCard().getHealth();
        target.getCard().setHealth(target.getCard().getAttackDamage());
        target.getCard().setAttackDamage(swapHealth);
    }
}
