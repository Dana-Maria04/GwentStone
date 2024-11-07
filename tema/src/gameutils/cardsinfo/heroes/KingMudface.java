package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

public class KingMudface extends Hero {
    public KingMudface(Cards card) {
        super(card);
    }

    @Override
    public void ability(LinkedList<Minions> minionsRow) {
        // Earth Born
        for (Minions minions : minionsRow) {
            minions.incHealth(1);
        }
    }
}
