package gameutils;

import fileio.CardInput;
import gameutils.cardsinfo.heroes.Hero;

import java.util.ArrayList;

public class GameStats {
    private int endedCnt=0;
    private int playedCnt=0;
    private int turnsCnt=0;
    private int gameEnded=0;

    private int idxP1;
    private int idxP2;

    private ArrayList<CardInput> deckCrrtP1;
    private ArrayList<CardInput> deckCrrtP2;

    private int cardsInDeckP1;
    private int cardsInDeckP2;

    public ArrayList<CardInput> getDeckCrrtP1() {
        return deckCrrtP1;
    }

    public void setDeckCrrtP1(ArrayList<CardInput> deckCrrtP1) {
        this.deckCrrtP1 = deckCrrtP1;
    }

    public ArrayList<CardInput> getDeckCrrtP2() {
        return deckCrrtP2;
    }

    public void setDeckCrrtP2(ArrayList<CardInput> deckCrrtP2) {
        this.deckCrrtP2 = deckCrrtP2;
    }

    public int getCardsInDeckP1() {
        return cardsInDeckP1;
    }

    public void setCardsInDeckP1(int cardsInDeckP1) {
        this.cardsInDeckP1 = cardsInDeckP1;
    }

    public int getCardsInDeckP2() {
        return cardsInDeckP2;
    }

    public void setCardsInDeckP2(int cardsInDeckP2) {
        this.cardsInDeckP2 = cardsInDeckP2;
    }

    public int getEndedCnt() {
        return endedCnt;
    }

    public int getIdxP1() {
        return idxP1;
    }

    public void setIdxP1(int idxP1) {
        this.idxP1 = idxP1;
    }

    public int getIdxP2() {
        return idxP2;
    }

    public void setIdxP2(int idxP2) {
        this.idxP2 = idxP2;
    }

    public void setEndedCnt(int endedCnt) {
        this.endedCnt = endedCnt;
    }

    public int getPlayedCnt() {
        return playedCnt;
    }

    public void setPlayedCnt(int playedCnt) {
        this.playedCnt = playedCnt;
    }

    public int getGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(int gameEnded) {
        this.gameEnded = gameEnded;
    }

    public int getTurnsCnt() {
        return turnsCnt;
    }

    public void setTurnsCnt(int turnsCnt) {
        this.turnsCnt = turnsCnt;
    }

}
