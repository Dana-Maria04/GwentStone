package gameutils.cardsinfo.heroes;

import gameutils.cardsinfo.Cards;

/**
 * A factory class for creating hero instances based on their names
 */
public final class HeroFactory {


    /**
     * for coding style
     */
    private HeroFactory() {

    }

    /**
     * Creates a hero instance based on the name of the given card
     *
     * @param card the card containing input about the hero
     * @return a Hero object of the appropriate type based on the card's name
     */
    public static Hero createHero(final Cards card) {
        return switch (card.getCard().getName()) {
            case "Empress Thorina" -> new EmpressThorina(card);
            case "General Kocioraw" -> new GeneralKocioraw(card);
            case "King Mudface" -> new KingMudface(card);
            case "Lord Royce" -> new LordRoyce(card);
            default -> new Hero(card);
        };
    }
}
