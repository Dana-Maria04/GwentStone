package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

public class MinionsFactory {
    public static Minions createMinion(Cards cardInput) {
        switch (cardInput.getCard().getName()) {
            case "Miraj":
                return new Miraj(cardInput);
            case "TheCursedOne":
                return new TheCursedOne(cardInput);
            case "TheRipper":
                return new TheRipper(cardInput);
            case "Disciple":
                return new Disciple(cardInput);
            case "Warden":
                return new Warden(cardInput);
            case "Sentinel":
                return new Sentinel(cardInput);
            case "Berserker":
                return new Berserker(cardInput);
            case "Goliath":
                return new Goliath(cardInput);
            default:
                throw new IllegalArgumentException("Unknown minion type: " + cardInput.getCard().getName());
        }
    }
}
