package gameutils;

public class Player {
    private int mana=1;
    private int winCnt=0;
    protected int DeckIdx;
    private Deck deck;

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getDeckIdx() {
        return DeckIdx;
    }

    public void setDeckIdx(int deckIdx) {
        DeckIdx = deckIdx;
    }

    public void addWin(){
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

    public void incWinCnt() {
        this.winCnt++;
    }

}
