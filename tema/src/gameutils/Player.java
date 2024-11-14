package gameutils;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.heroes.Hero;

import java.util.ArrayList;

import static gameutils.GameConstants.*;

public class Player {
    private int mana;
    private int winCnt=0;
    private ArrayList<Cards> deck;
    private Hero hero;

    public Player(){
        this.deck = new ArrayList<>();
        this.mana = START_MANA;
        this.winCnt = 0;
    }

    public void updateMana(int roundCnt){
        int manaToAdd;

        if (roundCnt > MAX_MANA) {
            manaToAdd = MAX_MANA;
        } else {
            manaToAdd = roundCnt;
        }
        this.mana += manaToAdd;
    }

    public void decMana(int mana){
        this.setMana(this.getMana() - mana);
    }


    public void incWinCnt() {
        this.setWinCnt(this.getWinCnt() + 1);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getWinCnt() {
        return winCnt;
    }

    public void setWinCnt(int winCnt) {
        this.winCnt = winCnt;
    }

    public ArrayList<Cards> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Cards> deck) {
        this.deck = deck;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
