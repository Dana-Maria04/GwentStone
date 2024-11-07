package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class Disciple extends Minions {
    public Disciple(final Cards cardInput) {
        super(cardInput);
        super.getCard().setAttackDamage(0);
    }

    @Override
    public void ability(final Minions target) {
        // God's Plan
        int initialHealth = target.getCard().getHealth();
        target.getCard().setHealth(initialHealth + 2);
    }
}
