package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class TheCursedOne extends Minions {
    public TheCursedOne (Cards cardInput) {
        super(cardInput);
        super.getCard().setAttackDamage(0);
    }

    @Override
    public void ability(final Minions target) {
        //Shapeshift

        int swapHealth = target.getCard().getHealth();
        target.getCard().setHealth(target.getCard().getAttackDamage());
        target.getCard().setAttackDamage(swapHealth);
    }
}

