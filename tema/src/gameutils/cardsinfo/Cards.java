package gameutils.cardsinfo;

import fileio.CardInput;
import gameutils.cardsinfo.heroes.Hero;

import java.util.ArrayList;

public class Cards {

    private CardInput card;

    private Hero HeroP1;
    private Hero HeroP2;

    public void setCard(CardInput card) {
        this.card = card;
    }

    public Hero getHeroP2() {
        return HeroP2;
    }

    public void setHeroP2(Hero heroP2) {
        HeroP2 = heroP2;
    }

    public Hero getHeroP1() {
        return HeroP1;
    }

    public void setHeroP1(Hero heroP1) {
        HeroP1 = heroP1;
    }

    public Cards(CardInput card) {
        this.card = card;
    }

    public CardInput getCard() {
        return card;
    }
}