package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class TheRipper extends Minions {
    public TheRipper(Cards cardInput) {
        super(cardInput);
    }

    @Override
    public void ability(final Minions target) {
        //Weak Knees
        int targetDamage = target.getCard().getAttackDamage();
        target.getCard().setAttackDamage(targetDamage - 2);
        if(target.getCard().getAttackDamage() < 0) {
            target.getCard().setAttackDamage(0);
        }

    }
}
