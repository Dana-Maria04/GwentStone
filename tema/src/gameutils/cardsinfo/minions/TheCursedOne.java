package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class TheCursedOne extends Minions {
    public TheCursedOne (Cards cardInput) {
        super(cardInput);
    }

    @Override
    public void ability(Minions target) {
        //Shapeshift
        int swapHealth = this.getCard().getHealth();
        target.getCard().setHealth(target.getCard().getAttackDamage());
        target.getCard().setAttackDamage(swapHealth);
    }
}

