package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class Disciple extends Minions {
    public Disciple(final Cards cardInput) {
        super(cardInput);
    }

    @Override
    public void ability(Minions target) {
        // God's Plan
        int initialHealth = target.getCard().getHealth();
        target.getCard().setHealth(target.getCard().getHealth() + 2);
    }
}
