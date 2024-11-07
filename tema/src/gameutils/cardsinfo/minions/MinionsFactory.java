package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;
import gameutils.cardsinfo.minions.*;

public class MinionsFactory {
    public static Minions createMinion(Cards card) {
        String name = card.getCard().getName();

        switch (name) {
            case "The Cursed One":
                return new TheCursedOne(card);
            case "The Ripper":
                return new TheRipper(card);
            case "Disciple":
                return new Disciple(card);
            case "Miraj":
                return new Miraj(card);
            default:
                return new Minions(card);
        }
    }

}
