package gameutils;

public class GameStats {
    private int playerTurn;
    private int roundCnt;
    private int turnCycle;

    public GameStats() {
        this.playerTurn = 0;
        this.roundCnt = 1;
        this.turnCycle = 0;
    }


    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getRoundCnt() {
        return roundCnt;
    }

    public void setRoundCnt(int roundCnt) {
        this.roundCnt = roundCnt;
    }

    public int getTurnCycle() {
        return turnCycle;
    }

    public void setTurnCycle(int turnCycle) {
        this.turnCycle = turnCycle;
    }
}
