package gameutils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CardInput;
import fileio.Input;

import java.util.ArrayList;


public class StartGame {
    private Player player1 = new Player();
    private Player player2 = new Player();
    private GameStats gameStats = new GameStats();

    private static StartGame game;

    public StartGame() {
    }

    public void runGame(final Input input, final ArrayNode output) {
        Input in = input;
        ArrayNode out = output;

        int gameIterator = 0;

        while(gameIterator < in.getGames().size()) {
            gameStats.setEndedCnt(0);
            gameStats.setTurnsCnt(0);
            int roundNr = 0;
            player1.setMana(1);
            player2.setMana(1);

            if (gameIterator == 0) {
                player1.setWinCnt(0);
                player2.setWinCnt(0);
                gameStats.setPlayedCnt(0);
            }

            // Get the players
            int player1Idx = in.getGames().get(gameIterator).getStartGame().getPlayerOneDeckIdx();
            gameStats.setIdxP1(player1Idx);

            int player2Idx = in.getGames().get(gameIterator).getStartGame().getPlayerTwoDeckIdx();
            gameStats.setIdxP2(player2Idx);

            ArrayList<CardInput> currentDeck1 = in.getPlayerOneDecks().getDecks().get(player1Idx);
            gameStats.setDeckCrrtP1(currentDeck1);

            ArrayList<CardInput> currentDeck2 = in.getPlayerOneDecks().getDecks().get(player1Idx);
            gameStats.setDeckCrrtP2(currentDeck2);

            gameStats.setCardsInDeckP1(currentDeck1.size());
            gameStats.setCardsInDeckP2(currentDeck2.size());

            gameIterator++; // Increment counter

        }
    }
}
