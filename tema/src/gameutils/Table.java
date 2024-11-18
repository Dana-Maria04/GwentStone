package gameutils;

import gameutils.cardsinfo.Minions;

import java.util.ArrayList;
import java.util.LinkedList;

import static gameutils.GameConstants.NUM_ROWS;

/**
 * Represents the game table, which contains rows of minions
 */
public final class Table {

    private final ArrayList<LinkedList<Minions>> table;

    /**
     * Initializes the game table with empty rows of minions
     */
    public Table() {
        table = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            table.add(new LinkedList<>());
        }
    }

    /**
     * Verifies if there is a tank minion on the front row for the given player
     *
     * @param playerTurn the current player's turn (0 for player one, 1 for player two)
     * @return 1 if a Tank minion is found, otherwise 0.
     */
    public int verifyTankOnRow(final int playerTurn) {
        LinkedList<Minions> frontRowMinions;

        if (playerTurn == 0) {
            frontRowMinions = table.get(1); // Front row for player one.
        } else {
            frontRowMinions = table.get(2); // Front row for player two.
        }

        for (Minions minion : frontRowMinions) {
            if (minion.verifyTank(minion) == 1) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * Gets the current state of the table
     *
     * @return the table as an ArrayList of LinkedLists of Minions
     */
    public ArrayList<LinkedList<Minions>> getTable() {
        return table;
    }

}
