package gameutils.cardsinfo.minions;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class AbilityCard extends Minions {
    public AbilityCard(final Cards cardInput) {
        super(cardInput);
    }

    // to be overridden
    public void ability(Minions target) {
    }
}
