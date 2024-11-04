package gameutils;

import fileio.CardInput;

import java.util.ArrayList;

import static gameutils.GameConstants.*;

public class Player {
    private int mana;
    private int winCnt=0;
    protected int DeckIdx;
    private ArrayList<CardInput> deck;

    public Player(){
        this.deck = new ArrayList<>();
        this.mana = START_MANA;
    }
//    public void addMana(int newMana){
//        this.setMana(this.getMana() + newMana);
//    }

    public void updateMana(int roundCnt){
        int manaToAdd;

        if (roundCnt > MAX_MANA) {
            manaToAdd = MAX_MANA;
        } else {
            manaToAdd = roundCnt;
        }
        this.mana += manaToAdd;

//        if (this.mana + manaToAdd > MAX_MANA) {
//            this.mana = MAX_MANA;
//        } else {
//        }
    }

    public void decMana(int mana){
        this.mana -= mana;
    }


    public void incWinCnt() {
        this.winCnt++;
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

    public int getDeckIdx() {
        return DeckIdx;
    }

    public void setDeckIdx(int deckIdx) {
        DeckIdx = deckIdx;
    }

    public ArrayList<CardInput> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<CardInput> deck) {
        this.deck = deck;
    }
}
