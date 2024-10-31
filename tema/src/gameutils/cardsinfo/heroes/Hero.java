package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.GameConstants;

public class Hero extends Cards {

    private int health;



    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Hero(CardInput card) {
        super(card);
        this.setHealth(GameConstants.MAX_HEALTH);
    }
}
