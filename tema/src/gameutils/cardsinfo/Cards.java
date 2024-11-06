package gameutils.cardsinfo;

import fileio.CardInput;
import fileio.Coordinates;
import gameutils.cardsinfo.heroes.Hero;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.LinkedList;

public class Cards {

    private CardInput card;


    public CardInput getCardInput() {
        return this.card;
    }

    public Cards(String name, int mana, String description, ArrayList<String> colors) {
        this.card = new CardInput(mana, 0, 0, description, colors, name);
    }


    public void decHealth(int health) {
        this.card.setHealth(this.card.getHealth() - health);
    }

    public void setCard(CardInput card) {
        this.card = card;
    }

    public Cards(CardInput card) {
        this.card = card;
    }

    public CardInput getCard() {
        return card;
    }
}