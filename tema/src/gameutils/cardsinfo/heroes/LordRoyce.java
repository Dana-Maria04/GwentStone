package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;
import java.util.List;

public class LordRoyce extends Hero {
    public LordRoyce(CardInput card) {
        super(card);
    }

    @Override
    public void ability(LinkedList<Minions> minionsRow) {
        // sub zero
        for (Minions minions : minionsRow) {
            minions.setIsFrozen(1);
        }
    }
}
