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
    protected ArrayList<ActionsInput> actionsinputs;
    protected ObjectMapper mapper = new ObjectMapper();
    protected ArrayNode output = mapper.createArrayNode();
    protected ArrayNode deckNode = mapper.createArrayNode();
    private int WinP1;
    private int WinP2;
    private int checkIfGameEnded;

    private ArrayList<CardInput> deckP1 = new ArrayList<>();
    private ArrayList<CardInput> deckP2 = new ArrayList<>();

    private ArrayList<CardInput> handP1;
    private ArrayList<CardInput> handP2;


    public ArrayNode runGame(Input input) {

        ArrayNode output = mapper.createArrayNode();
        // todo initialise the players , decks , cards , and heroes
        WinP1 = 0;
        WinP2 = 0; // initialize wins

        int playerTurn = 0;
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

            }

            for(int p2 = 0 ; p2 < input.getPlayerTwoDecks().getNrCardsInDeck(); p2++){
                CardInput cardInput = input.getPlayerTwoDecks().getDecks().get(deckIdx2).get(p2);
                deckP2.add(new Cards(cardInput).getCardInput());
            }

            // shuffle the decks
            Collections.shuffle(deckP1, new Random(randSeed));
            Collections.shuffle(deckP2, new Random(randSeed));
            playerTurn = startGame.getStartingPlayer() - 1;
        }
        // todo handle commands
        actionsinputs = input.getGames().get(0).getActions();

        for(ActionsInput action : actionsinputs){
            ObjectNode actionNode = mapper.createObjectNode();

            CommandHandler commandHandler = new CommandHandler();
            switch(action.getCommand()){
                case "getPlayerDeck":
                    // todo getPlayerDeck
                    //int playerIdx = action.getPlayerIdx();
//                    commandHandler.getPlayerDeck(actionNode, player, action, output);

                    commandHandler.getPlayerDeck(deckP1, deckP2, actionNode, action, output);

                    break;
                case "getPlayeHero":
                    // todo getplayerhero
                    break;
                case "getPlayerTurn":
                    // todo gateplayerturn
                    commandHandler.getPlayerTurn(actionNode, output, action, playerTurn);
                    break;
                default:
                    break;
            }
        }
        return output;

    }





}



