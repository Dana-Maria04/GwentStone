package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

/**
 * Represents the minion "The Ripper" with a special ability "Weak Knees."
 */
public class TheRipper extends Minions {

    /**
     * Constructs "The Ripper" minion
     *
     * @param cardInput the card containing the minion's details
     */
    public TheRipper(final Cards cardInput) {
        super(cardInput);
    }

    /**
     * Applies the "Weak Knees" ability to the specified target minion.
     * This ability reduces the target minion's attack damage by 2.
     * If the resulting attack damage is less than 2, it is set to 0
     *
     * @param target the minion to which the ability will be applied
     */
    @Override
    public void ability(final Minions target) {
        int targetDamage = target.getCard().getAttackDamage();
        target.getCard().setAttackDamage(targetDamage - 2);
        if (target.getCard().getAttackDamage() < 2) {
            target.getCard().setAttackDamage(0);
        }
    }
}
