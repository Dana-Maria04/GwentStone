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

//    public LinkedList<CardInput> copyCards(LinkedList<Cards> cards) {
//        LinkedList<CardInput> copy = new LinkedList<>();
//        for (Cards card : cards) {
//            Cards cardCopy = new Cards(card.getCardInput());
//            copy.add(cardCopy);
//        }
//        return copy;
//    }

//    private Hero HeroP1;
//    private Hero HeroP2;

    public void setCard(CardInput card) {
        this.card = card;
    }
//
//    public Hero getHeroP2() {
//        return HeroP2;
//    }
//
//    public void setHeroP2(Hero heroP2) {
//        HeroP2 = heroP2;
//    }
//
//    public Hero getHeroP1() {
//        return HeroP1;
//    }
//
//    public void setHeroP1(Hero heroP1) {
//        HeroP1 = heroP1;
//    }

    public Cards(CardInput card) {
        this.card = card;
    }

    public CardInput getCard() {
        return card;
    }
}