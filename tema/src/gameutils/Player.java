package gameutils;

public class Player {
    private int mana=1;
    private int winCnt=0;

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
}
