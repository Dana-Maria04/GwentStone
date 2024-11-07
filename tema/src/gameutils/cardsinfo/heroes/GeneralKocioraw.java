package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;
import java.util.Map;

public class GeneralKocioraw extends Hero {
    public GeneralKocioraw(Cards card) {
        super(card);
    }

    @Override
    public void ability(LinkedList<Minions> minionsRow) {
        // Blood Thirst
        for (Minions minions : minionsRow) {
            minions.incAttackDamage(minions, 1);
        }
    }

}
