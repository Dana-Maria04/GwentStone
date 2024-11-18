package gameutils;

/**
 * Represents the game statistics and state, including the current player's turn,
 * the round count, and the turn cycle.
 */
public class GameStats {

    private int playerTurn;
    private int roundCnt;
    private int turnCycle;

    /**
     * Initializes a new instance of the game statistics with default values
     * - Player turn starts at 0 (player one)
     * - Round count starts at 1
     * - Turn cycle starts at 0
     */
    public GameStats() {
        this.playerTurn = 0;
        this.roundCnt = 1;
        this.turnCycle = 0;
    }

    /**
     * Gets the current player's turn
     *
     * @return the index of the current player's turn
     * (0 for player one, 1 for player two)
     */
    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Sets the current player's turn
     *
     * @param playerTurn the index of the player whose turn it is
     * (0 for player one, 1 for player two)
     */
    public void setPlayerTurn(final int playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * Gets the current round count
     *
     * @return the current round count
     */
    public int getRoundCnt() {
        return roundCnt;
    }

    /**
     * Sets the current round count
     *
     * @param roundCnt the new round count
     */
    public void setRoundCnt(final int roundCnt) {
        this.roundCnt = roundCnt;
    }

    /**
     * Gets the current turn cycle
     *
     * @return the current turn cycle
     */
    public int getTurnCycle() {
        return turnCycle;
    }

    /**
     * Sets the current turn cycle
     *
     * @param turnCycle the new turn cycle
     */
    public void setTurnCycle(final int turnCycle) {
        this.turnCycle = turnCycle;
    }
}
