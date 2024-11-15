package gameutils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.heroes.Hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class StartGame {

    private Player[] player;
    private Hand[] hand;
    private ArrayList<ActionsInput> actionsinputs;
    private ObjectMapper mapper = new ObjectMapper();
    private ArrayNode output = mapper.createArrayNode();
    private ArrayNode deckNode = mapper.createArrayNode();
    private int checkIfGameEnded;
    private int winsP1;
    private int winsP2;
    private ArrayList<Cards> deckP1 = new ArrayList<>();
    private ArrayList<Cards> deckP2 = new ArrayList<>();

    private Table table;

    public ArrayNode runGame(Input input) {

        ArrayNode output = mapper.createArrayNode();
        winsP1 = 0;
        winsP2 = 0;
        for (int i = 0; i < input.getGames().size(); i++) {
            this.player = new Player[2];
            this.player[0] = new Player();
            this.player[1] = new Player();
            this.hand = new Hand[2];
            this.hand[0] = new Hand();
            this.hand[1] = new Hand();

            GameStats gameStats = new GameStats();


            ObjectMapper mapper = new ObjectMapper();

            int randSeed = input.getGames().get(i).getStartGame().getShuffleSeed();

            int deckIdx1 = input.getGames().get(i).getStartGame().getPlayerOneDeckIdx();
            int deckIdx2 = input.getGames().get(i).getStartGame().getPlayerTwoDeckIdx();


            StartGameInput startGame = new StartGameInput();
            startGame = input.getGames().get(i).getStartGame();

            deckP1.clear();
            deckP2.clear();

            for (int p1 = 0; p1 < input.getPlayerOneDecks().getNrCardsInDeck(); p1++) {
                CardInput originalCardInput = input.getPlayerOneDecks().getDecks().get(deckIdx1).get(p1);
                CardInput copiedCardInput = new CardInput(
                        originalCardInput.getMana(),
                        originalCardInput.getAttackDamage(),
                        originalCardInput.getHealth(),
                        originalCardInput.getDescription(),
                        new ArrayList<>(originalCardInput.getColors()),
                        originalCardInput.getName()
                );
                Cards card = new Cards(copiedCardInput);
                deckP1.add(card);
            }

            for (int p2 = 0; p2 < input.getPlayerTwoDecks().getNrCardsInDeck(); p2++) {
                CardInput originalCardInput = input.getPlayerTwoDecks().getDecks().get(deckIdx2).get(p2);
                CardInput copiedCardInput = new CardInput(
                        originalCardInput.getMana(),
                        originalCardInput.getAttackDamage(),
                        originalCardInput.getHealth(),
                        originalCardInput.getDescription(),
                        new ArrayList<>(originalCardInput.getColors()),
                        originalCardInput.getName()
                );
                Cards card = new Cards(copiedCardInput);
                deckP2.add(card);
            }

            player[0].setDeck(deckP1);
            player[1].setDeck(deckP2);

            Collections.shuffle(player[0].getDeck(), new Random(randSeed));
            Collections.shuffle(player[1].getDeck(), new Random(randSeed));

            gameStats.setPlayerTurn(startGame.getStartingPlayer() - 1);


            hand[0].addCard(player[0].getDeck().get(0));
            hand[1].addCard(player[1].getDeck().get(0));


            player[0].getDeck().remove(0);
            player[1].getDeck().remove(0);

            player[0].setHero(new Hero(input.getGames().get(i).getStartGame().getPlayerOneHero()));
            player[1].setHero(new Hero(input.getGames().get(i).getStartGame().getPlayerTwoHero()));

            actionsinputs = input.getGames().get(i).getActions();
            table = new Table();

            boolean checkWins = false;
            for (ActionsInput action : actionsinputs) {
                ObjectNode actionNode = mapper.createObjectNode();

                CommandHandler commandHandler = new CommandHandler();

                switch (action.getCommand()) {
                    case "getPlayerDeck":
                        commandHandler.getPlayerDeck(player[0].getDeck(), player[1].getDeck(), actionNode, action, output);
                        break;
                    case "getPlayerHero":
                        commandHandler.getPlayerHero(actionNode, output, action, input, player[0], player[1]);
                        break;
                    case "getPlayerTurn":
                        commandHandler.getPlayerTurn(actionNode, output, action, gameStats.getPlayerTurn());
                        break;
                    case "getCardsInHand":
                        commandHandler.getCardsInHand(action, actionNode, output, hand[0], hand[1]);
                        break;
                    case "getPlayerMana":
                        commandHandler.getPlayerMana(actionNode, output, action, player[0], player[1]);
                        break;
                    case "getCardsOnTable":
                        commandHandler.getCardsOnTable(action, actionNode, output, table);
                        break;
                    case "endPlayerTurn":
                        commandHandler.endPlayerTurn(table, player, hand, gameStats);
                        break;
                    case "placeCard":
                        boolean[] ok = {false};
                        commandHandler.placeCard(action, actionNode, output, player[0], player[1], hand, gameStats.getPlayerTurn(), table, action.getHandIdx(), ok);
                        break;
                    case "cardUsesAttack":
                        commandHandler.cardUsesAttack(action, actionNode, output, gameStats.getPlayerTurn(), table);
                        break;
                    case "getCardAtPosition":
                        commandHandler.getCardAtPosition(action, actionNode, output, table);
                        break;
                    case "cardUsesAbility":
                        commandHandler.cardUsesAbility(action, actionNode, output, gameStats.getPlayerTurn(), table);
                        break;
                    case "useAttackHero":
                        commandHandler.useAttackHero(action, actionNode, output, player[0], player[1], gameStats.getPlayerTurn(), table);
                        break;
                    case "useHeroAbility":
                        commandHandler.useHeroAbility(action, actionNode, output, player[0], player[1], gameStats.getPlayerTurn(), table);
                        break;
                    case "getFrozenCardsOnTable":
                        commandHandler.getFrozenCardsOnTable(action, actionNode, output, table);
                        break;
                    case "getPlayerOneWins":
                        CommandHandler.getPlayerWins(action, actionNode, output, winsP1);
                        break;
                    case "getPlayerTwoWins":
                        CommandHandler.getPlayerWins(action, actionNode, output, winsP2);
                        break;
                    case "getTotalGamesPlayed":
                        CommandHandler.getTotalGamesPlayed(action, actionNode, output, i + 1);
                        break;
                    default:
                        break;
                }
                if(player[0].getWinCnt() > 0 && checkWins == false) {
                    winsP1++;
                    checkWins = true;
                }
                if(player[1].getWinCnt() > 0 && checkWins == false) {
                    winsP2++;
                    checkWins = true;
                }
            }
        }
        return output;
    }
}



