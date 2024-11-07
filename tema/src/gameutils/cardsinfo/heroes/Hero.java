package gameutils.cardsinfo.heroes;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.GameConstants;
import gameutils.cardsinfo.Minions;

import java.util.LinkedList;

import static gameutils.GameConstants.MAX_HEALTH;

public class Hero extends Cards {

    private int hasAttacked = 0;

    public Hero(CardInput card) {
        super(card);
        this.getCard().setHealth(MAX_HEALTH);
    }

    public Hero(Cards card) {
        super(card.getCardInput());
        this.hasAttacked = 0;
    }

    // for overriding
    public void ability(LinkedList<Minions> minionsRow) {
    }

    public int verifyDefensive(){
        if(this.getCard().getName().equals("King Mudface") || this.getCard().getName().equals("General Kocioraw")){
            return 1;
        }
        return 0;
    }

    public int verifyOffensive(){
        if(this.getCard().getName().equals("Lord Royce") || this.getCard().getName().equals("Empress Thorina")){
            return 1;
        }
        return 0;
    }

    public void setHasAttacked(int hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public int getHasAttacked() {
        return this.hasAttacked;
    }
}
