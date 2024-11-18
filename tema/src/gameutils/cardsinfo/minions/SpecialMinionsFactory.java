package gameutils.cardsinfo.minions;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;

/**
 * A factory class for creating specific types of minions based on their card name
 */
public final class SpecialMinionsFactory {

    /**
     * for coding style
     */
    private SpecialMinionsFactory() {
    }

    /**
     * Creates a specific type of minion based on the card's name
     *
     * @param card the card information used to determine the minion type
     * @return a Minions object of the appropriate type based on the card's name
     */
    public static Minions createMinion(final Cards card) {
        return switch (card.getCard().getName()) {
            case "The Cursed One" -> new TheCursedOne(card);
            case "The Ripper" -> new TheRipper(card);
            case "Disciple" -> new Disciple(card);
            case "Miraj" -> new Miraj(card);
            default -> new Minions(card);
        };
    }
}
