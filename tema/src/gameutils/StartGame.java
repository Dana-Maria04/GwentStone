package gameutils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Input;
import fileio.ActionsInput;
import fileio.StartGameInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.heroes.Hero;
import fileio.DecksInput;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Manages the logic for initializing and running a game instance.
 */
public class StartGame {


    private final ObjectMapper mapper = new ObjectMapper();
    private int winsP1;
    private int winsP2;
    private final ArrayList<Cards> deckP1 = new ArrayList<>();
    private final ArrayList<Cards> deckP2 = new ArrayList<>();

    private boolean checkWins;

    /**
     * Runs the game based on the input data provided.
     *
     * @param input the input data for the game, including player decks, actions,
     * and starting configurations.
     * @return an {@code ArrayNode} containing the output of the game actions.
     */
    public ArrayNode runGame(final Input input) {

        Player[] player;
        Hand[] hand;
        ArrayList<ActionsInput> actionsinputs;
        Table table;

        ArrayNode output = mapper.createArrayNode();
        winsP1 = 0;
        winsP2 = 0;

        for (int i = 0; i < input.getGames().size(); i++) {
            player = new Player[2];
            player[0] = new Player();
            player[1] = new Player();

            hand = new Hand[2];
            hand[0] = new Hand();
            hand[1] = new Hand();

            GameStats gameStats = new GameStats();

            int randSeed = input.getGames().get(i).getStartGame().getShuffleSeed();
            int deckIdx1 = input.getGames().get(i).getStartGame().getPlayerOneDeckIdx();
            int deckIdx2 = input.getGames().get(i).getStartGame().getPlayerTwoDeckIdx();

            StartGameInput startGame = input.getGames().get(i).getStartGame();

            deckP1.clear();
            deckP2.clear();

            makePlayerDecks(input, deckIdx1, deckP1, player[0], randSeed, true);
            makePlayerDecks(input, deckIdx2, deckP2, player[1], randSeed, false);

            gameStats.setPlayerTurn(startGame.getStartingPlayer() - 1);

            hand[0].addCard(player[0].getDeck().get(0));
            hand[1].addCard(player[1].getDeck().get(0));

            player[0].getDeck().remove(0);
            player[1].getDeck().remove(0);

            player[0].setHero(new Hero(startGame.getPlayerOneHero()));
            player[1].setHero(new Hero(startGame.getPlayerTwoHero()));

            actionsinputs = input.getGames().get(i).getActions();
            table = new Table();

            checkWins = false;

            for (ActionsInput action : actionsinputs) {
                ObjectNode actionNode = mapper.createObjectNode();
                CommandHandler commandHandler = new CommandHandler();

                switch (action.getCommand()) {
                    case "getPlayerDeck":
                        commandHandler.getPlayerDeck(player[0].getDeck(), player[1].getDeck(),
                                actionNode, action, output);
                        break;
                    case "getPlayerHero":
                        commandHandler.getPlayerHero(actionNode, output, action,
                                player[0], player[1]);
                        break;
                    case "getPlayerTurn":
                        commandHandler.getPlayerTurn(actionNode, output, action,
                                gameStats.getPlayerTurn());
                        break;
                    case "getCardsInHand":
                        commandHandler.getCardsInHand(action, actionNode, output,
                                hand[0], hand[1]);
                        break;
                    case "getPlayerMana":
                        commandHandler.getPlayerMana(actionNode, output, action,
                                player[0], player[1]);
                        break;
                    case "getCardsOnTable":
                        commandHandler.getCardsOnTable(action, actionNode, output, table);
                        break;
                    case "endPlayerTurn":
                        commandHandler.endPlayerTurn(table, player, hand, gameStats);
                        break;
                    case "placeCard":
                        boolean[] ok = {false};
                        commandHandler.placeCard(action, actionNode, output, player[0], player[1],
                                hand, gameStats.getPlayerTurn(), table, action.getHandIdx(), ok);
                        break;
                    case "cardUsesAttack":
                        commandHandler.cardUsesAttack(action, actionNode, output,
                                gameStats.getPlayerTurn(), table);
                        break;
                    case "getCardAtPosition":
                        commandHandler.getCardAtPosition(action, actionNode, output, table);
                        break;
                    case "cardUsesAbility":
                        commandHandler.cardUsesAbility(action, actionNode, output,
                                gameStats.getPlayerTurn(), table);
                        break;
                    case "useAttackHero":
                        commandHandler.useAttackHero(action, actionNode, output,
                                player[0], player[1], gameStats.getPlayerTurn(), table);
                        break;
                    case "useHeroAbility":
                        commandHandler.useHeroAbility(action, actionNode, output,
                                player[0], player[1], gameStats.getPlayerTurn(), table);
                        break;
                    case "getFrozenCardsOnTable":
                        commandHandler.getFrozenCardsOnTable(action, actionNode, output, table);
                        break;
                    case "getPlayerOneWins":
                        CommandHandler.getTotalGamesPlayedOrPlayerWins(action, actionNode,
                                output, winsP1);
                        break;
                    case "getPlayerTwoWins":
                        CommandHandler.getTotalGamesPlayedOrPlayerWins(action, actionNode,
                                output, winsP2);
                        break;
                    case "getTotalGamesPlayed":
                        CommandHandler.getTotalGamesPlayedOrPlayerWins(action, actionNode,
                                output, i + 1);
                        break;
                    default:
                        break;
                }
                updateWins(player);
            }
        }

        return output;
    }

    /**
     * Creates and shuffles the player's deck.
     *
     * @param input      the input data for the game.
     * @param deckIndex  the index of the deck to use for the player.
     * @param playerDeck the deck to populate with cards.
     * @param currentPlayer     the player for whom the deck is being created.
     * @param randSeed   the random seed used for shuffling the deck.
     * @param isPlayerOne true if the deck belongs to player one, false otherwise.
     */
    private void makePlayerDecks(final Input input, final int deckIndex,
                                 final ArrayList<Cards> playerDeck, final Player currentPlayer,
                                 final int randSeed, final boolean isPlayerOne) {
        playerDeck.clear();
        DecksInput decks;

        if (isPlayerOne) {
            decks = input.getPlayerOneDecks();
        } else {
            decks = input.getPlayerTwoDecks();
        }

        for (int i = 0; i < decks.getNrCardsInDeck(); i++) {
            CardInput originalCardInput = decks.getDecks().get(deckIndex).get(i);
            CardInput copiedCardInput = new CardInput(
                    originalCardInput.getMana(),
                    originalCardInput.getAttackDamage(),
                    originalCardInput.getHealth(),
                    originalCardInput.getDescription(),
                    new ArrayList<>(originalCardInput.getColors()),
                    originalCardInput.getName()
            );
            Cards card = new Cards(copiedCardInput);
            playerDeck.add(card);
        }
        currentPlayer.setDeck(playerDeck);
        Collections.shuffle(currentPlayer.getDeck(), new Random(randSeed));
    }

    /**
     * Updates the number of wins for each player
     *
     * @param currentPlayer the array containing the two players
     */
    private void updateWins(final Player[] currentPlayer) {
        if (currentPlayer[0].getWinCnt() > 0 && !checkWins) {
            winsP1++;
            checkWins = true;
        }
        if (currentPlayer[1].getWinCnt() > 0 && !checkWins) {
            winsP2++;
            checkWins = true;
        }
    }
}
