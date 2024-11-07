package gameutils.cardsinfo.heroes;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.minions.Disciple;
import gameutils.cardsinfo.minions.Miraj;
import gameutils.cardsinfo.minions.TheCursedOne;
import gameutils.cardsinfo.minions.TheRipper;

public class HeroFactory {
    public static Hero createHero(Cards card) {
        String name = card.getCard().getName();

        switch (name) {
            case "Empress Thorina":
                return new EmpressThorina(card);
            case "General Kocioraw":
                return new GeneralKocioraw(card);
            case "King Mudface":
                return new KingMudface(card);
            case "Lord Royce":
                return new LordRoyce(card);
            default:
                return new Hero(card);
        }
    }
}
