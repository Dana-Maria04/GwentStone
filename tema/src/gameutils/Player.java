package gameutils;

import gameutils.cardsinfo.Cards;

import java.util.ArrayList;

public class Player {
    private int health; // ??
    private int mana;
    private Deck deck;
    private ArrayList<Cards> hand;
    private int manaPerRound;

    public Player(Deck deck) {
        this.health = 30;
        this.mana = 0;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.manaPerRound = 1;
    }

    public void starNextRound() {
        if(manaPerRound < 10) {
            mana += manaPerRound;
        }
        drawCard();
    }

    public void drawCard() {
        Cards card = deck.drawCard();
        if(card != null) {
            hand.add(card);
        }
    }

    public void playCard(int cardIdx){
        if( cardIdx < hand.size() && mana >= hand.get(cardIdx).getMana()) {
            Cards cardToPlay= hand.remove(cardIdx);
            mana -= cardToPlay.getMana();
            // todo

        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public ArrayList<Cards> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Cards> cards) {
        this.hand = cards;
    }

    public int getManaPerRound() {
        return manaPerRound;
    }

    public void setManaPerRound(int manaPerRound) {
        this.manaPerRound = manaPerRound;
    }
}
