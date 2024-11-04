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

    protected Player[] player;
    protected Hand[] hand;
    protected ArrayList<ActionsInput> actionsinputs;
    protected ObjectMapper mapper = new ObjectMapper();
    protected ArrayNode output = mapper.createArrayNode();
    protected ArrayNode deckNode = mapper.createArrayNode();
    private int WinP1;
    private int WinP2;
    private int checkIfGameEnded;
    private int roundCnt;

    private ArrayList<CardInput> deckP1 = new ArrayList<>();
    private ArrayList<CardInput> deckP2 = new ArrayList<>();

    private Table table;

    public ArrayNode runGame(Input input) {

        ArrayNode output = mapper.createArrayNode();
        // todo initialise the players , decks , cards , and heroes
        WinP1 = 0;
        WinP2 = 0;// initialize wins

        this.roundCnt = 1;

        this.player = new Player[2];
        this.player[0] = new Player();
        this.player[1] = new Player();


        this.hand = new Hand[2];
        this.hand[0] = new Hand();
        this.hand[1] = new Hand();


        int playerTurn = 0;
        int turnCycle = 0;
        for(int i = 0 ; i < input.getGames().size(); i++ ){
            ObjectMapper mapper = new ObjectMapper();
            CommandHandler commandsHandler = new CommandHandler();
            // get the seed for the shuffle
            int randSeed = input.getGames().get(i).getStartGame().getShuffleSeed();
            // get the decks for the players
            int deckIdx1 = input.getGames().get(i).getStartGame().getPlayerOneDeckIdx();
            int deckIdx2 = input.getGames().get(i).getStartGame().getPlayerTwoDeckIdx();


            StartGameInput startGame = new StartGameInput();
            startGame = input.getGames().get(i).getStartGame();

            for(int p1 = 0 ; p1 < input.getPlayerOneDecks().getNrCardsInDeck(); p1++){
                CardInput cardInput = input.getPlayerOneDecks().getDecks().get(deckIdx1).get(p1);
                deckP1.add(new Cards(cardInput).getCardInput());
                player[0].getDeck().add(new Cards(cardInput).getCardInput());
            }
            for(int p2 = 0 ; p2 < input.getPlayerTwoDecks().getNrCardsInDeck(); p2++){
                CardInput cardInput = input.getPlayerTwoDecks().getDecks().get(deckIdx2).get(p2);
                deckP2.add(new Cards(cardInput).getCardInput());
                player[1].getDeck().add(new Cards(cardInput).getCardInput());
            }

            // shuffle the decks
//            Collections.shuffle(deckP1, new Random(randSeed));
//            Collections.shuffle(deckP2, new Random(randSeed));

//            player[0].setDeck(deckP1);
//            player[1].setDeck(deckP2);

            Collections.shuffle(player[0].getDeck(), new Random(randSeed));
            Collections.shuffle(player[1].getDeck(), new Random(randSeed));

            playerTurn = startGame.getStartingPlayer() - 1;
        }
        // todo handle commands

        hand[0].addCard(player[0].getDeck().get(0));
        hand[1].addCard(player[1].getDeck().get(0));


        player[0].getDeck().remove(0);
        player[1].getDeck().remove(0);

        actionsinputs = input.getGames().get(0).getActions();
        table = new Table();
        for(ActionsInput action : actionsinputs){
            ObjectNode actionNode = mapper.createObjectNode();

            CommandHandler commandHandler = new CommandHandler();
            switch(action.getCommand()){
                case "getPlayerDeck":
                    commandHandler.getPlayerDeck(player[0].getDeck(), player[1].getDeck(), actionNode, action, output);
                    break;
                case "getPlayerHero":
                    commandHandler.getPlayerHero(actionNode, output, action, input);
                    break;
                case "getPlayerTurn":
                    commandHandler.getPlayerTurn(actionNode, output, action, playerTurn);
                    break;
                case "getCardsInHand":
                    commandHandler.getCardsInHand(action,actionNode,output, hand[0], hand[1]);
                    break;
                case "getPlayerMana":
                        commandHandler.getPlayerMana(actionNode, output, action, player[0], player[1]);
                    break;
                case "getCardsOnTable":
                    commandHandler.getCardsOnTable(action, actionNode, output, table);
                    break;
                case "endPlayerTurn":
//                    System.out.printf("Player %d ended his turn in round %d\n", playerTurn + 1, roundCnt);
                    turnCycle++;
                    // if it was the second player's turn, now it's the first player's turn
                    if(playerTurn == 0) {
                        if(player[0].getDeck().size() > 0) {
                            hand[0].addCard(player[0].getDeck().get(0));
                            player[0].getDeck().remove(0);
//                            player[1].getDeck().remove(0);
                        }
                    } else {
                        if(player[1].getDeck().size() > 0) {
                            hand[1].addCard(player[1].getDeck().get(0));
//                            deckP2.remove(0);
//                            player[0].getDeck().remove(0);
                            player[1].getDeck().remove(0);
                        }
                    }
                    if(playerTurn == 0)
                        playerTurn = 1;
                    else
                        playerTurn = 0;

                    if(turnCycle == 2) {
                        roundCnt++;
                        System.out.printf("-----END OF ROUND %d-----\n", roundCnt - 1);
                        System.out.printf("P1 before updateMana %d ", player[0].getMana());
                        player[0].updateMana(roundCnt);
                        System.out.printf("and after %d\n", player[0].getMana());
                        System.out.printf("P2 before updateMana %d ", player[1].getMana());
                        player[1].updateMana(roundCnt);
                        System.out.printf("and after %d\n", player[1].getMana());
                        turnCycle = 0;

                        System.out.printf("-----------START OF ROUND %d------------\n", roundCnt);
                    }


                    break;
                case "placeCard":

//                    System.out.printf("for player %d, placed card %s with %d mana\n", playerTurn + 1, hand[playerTurn].getHand().get(action.getHandIdx()).getName(), hand[playerTurn].getHand().get(action.getHandIdx()).getMana());


//                    System.out.printf("card: %s: player: %d\n", hand[playerTurn].getHand().get(action.getHandIdx()).getName(), playerTurn);
                    boolean[] ok = {false};
                    commandHandler.placeCard(action, actionNode, output, player[0], player[1], hand, playerTurn, table, action.getHandIdx(), ok);
                    if(action.getHandIdx() < hand[playerTurn].getHand().size() && ok[0]) {
                        hand[playerTurn].removeCard(hand[playerTurn].getHand().get(action.getHandIdx()));
                    }
                    break;
                default:
                    break;
            }
        }
        return output;

    }

}



