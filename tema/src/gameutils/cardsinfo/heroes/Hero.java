package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.GameConstants;

import static gameutils.GameConstants.MAX_HEALTH;

public class Hero extends Cards {



    public Hero(CardInput card) {
        super(card);
        this.getCard().setHealth(MAX_HEALTH);
    }
}
