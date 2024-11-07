package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class Miraj extends Minions{
    public Miraj(Cards cardInput) {
        super(cardInput);
        super.getCard().setAttackDamage(cardInput.getCard().getAttackDamage());
    }

    @Override
    public void ability(final Minions target) {
        //Skyjack
        int swapHealth = this.getCard().getHealth();
        this.getCard().setHealth(target.getCard().getHealth());
        target.getCard().setHealth(swapHealth);
    }
}

