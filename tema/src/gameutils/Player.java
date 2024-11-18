package gameutils;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.heroes.Hero;
import java.util.ArrayList;

/**
 * Represents a player in the game, holding their deck, mana, hero, and win count
 */
public final class Player {

    private int mana;
    private int winCnt;
    private ArrayList<Cards> deck;
    private Hero hero;

    /**
     * Initializes a new player with default values
     */
    public Player() {
        this.deck = new ArrayList<>();
        this.mana = GameConstants.START_MANA;
        this.winCnt = 0;
    }

    /**
     * Updates the player's mana based on the round count
     *
     * @param roundCnt the current round count
     */
    public void updateMana(final int roundCnt) {
        int manaToAdd = Math.min(roundCnt, GameConstants.MAX_MANA);
        this.mana += manaToAdd;
    }

    /**
     * Decreases the player's mana by the amount specified
     *
     * @param manaToDec the amount of mana to decrease
     */
    public void decMana(final int manaToDec) {
        this.setMana(this.mana - manaToDec);
    }

    /**
     * Increases the player's win count by 1
     */
    public void incWinCnt() {
        this.setWinCnt(this.winCnt + 1);
    }

    /**
     * Gets the player's current mana
     *
     * @return the player's mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the player's mana
     *
     * @param mana the new mana value
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Gets the player's win count
     *
     * @return the player's win count
     */
    public int getWinCnt() {
        return winCnt;
    }

    /**
     * Sets the player's win count
     *
     * @param winCnt the new win count value
     */
    public void setWinCnt(final int winCnt) {
        this.winCnt = winCnt;
    }

    /**
     * Gets the player's deck
     *
     * @return the player's deck
     */
    public ArrayList<Cards> getDeck() {
        return deck;
    }

    /**
     * Sets the player's deck
     *
     * @param deck the new deck
     */
    public void setDeck(final ArrayList<Cards> deck) {
        this.deck = deck;
    }

    /**
     * Gets the player's hero
     *
     * @return the player's hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Sets the player's hero
     *
     * @param hero the new hero
     */
    public void setHero(final Hero hero) {
        this.hero = hero;
    }
}
