package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.GameConstants;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

import static gameutils.GameConstants.MAX_HEALTH;

public class Hero extends Cards {

    public Hero(CardInput card) {
        super(card);
        this.getCard().setHealth(MAX_HEALTH);
    }


    // for overriding
    public void ability(LinkedList<Minions> minionsRow) {
    }
}
