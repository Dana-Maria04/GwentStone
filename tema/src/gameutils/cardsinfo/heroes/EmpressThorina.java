package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

public class EmpressThorina extends Hero {
    public EmpressThorina(Cards card) {
        super(card);
    }

    @Override
    public void ability(LinkedList<Minions> minionsRow) {
        //LowBlow
        Minions minionsMaxHealth = minionsRow.get(0); // get the first element
        int copyIdx = 0;
        int idx = 0;

        for (Minions minions : minionsRow) {
            if (minions.getCard().getHealth() > minionsMaxHealth.getCard().getHealth()) {
                minionsMaxHealth = minions; // compare health with the first element
                copyIdx = idx; // update the max health index of the minion
            }
            idx++;
        }
        minionsRow.remove(copyIdx);
    }
}
