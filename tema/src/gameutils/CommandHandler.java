package gameutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.StartGameInput;
import fileio.Input;
import gameutils.Player;
import gameutils.GameStats;
import gameutils.StartGame;
import gameutils.Table;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;
import gameutils.cardsinfo.heroes.Hero;
import gameutils.Deck;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class CommandHandler {

    public void getPlayerDeck(ArrayList<CardInput> deckP1, ArrayList<CardInput> deckP2,ObjectNode actionNode, ActionsInput action, ArrayNode output) {
//        actionNode.put("command", action.getCommand());
//        actionNode.put("playerIdx", action.getPlayerIdx());
//        Deck deck = player[action.getPlayerIdx() - 1].getDeck();
//        ArrayList<CardInput> playerDeck = deck.getDeck().getDecks().get(action.getPlayerIdx() - 1);
        ArrayNode deckArray = actionNode.putArray("output");

        ArrayList<CardInput> playerDeck;
        playerDeck = new ArrayList<>();

        if(action.getPlayerIdx() - 1 == 0)
            playerDeck = deckP1;
        else
            playerDeck = deckP2;


        for (CardInput card : playerDeck) {
            ObjectNode cardNode = actionNode.objectNode();

            if(!card.getName().equals("Berserker") && !card.getName().equals("Sentinel")) {
                cardNode.put("mana", card.getMana());
                cardNode.put("attackDamage", card.getAttackDamage());
                cardNode.put("health", card.getHealth());
                cardNode.put("description", card.getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for (String color : card.getColors()) {
                    colorsArray.add(color);
                }

                cardNode.put("name", card.getName());

                deckArray.add(cardNode);
            }
        }

        output.add(actionNode);


    }

    public void getPlayerTurn(ObjectNode actionNode, ArrayNode output, ActionsInput action, int startingPlayer) {
        ObjectNode turnNode = actionNode.objectNode();

        turnNode.put("command", action.getCommand());
        turnNode.put("output", startingPlayer + 1);

        output.add(turnNode);


    }

    public void getPlayerHero(ObjectNode actionNode, ArrayNode output, ActionsInput action, Input input) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        ObjectNode heroNode = actionNode.objectNode();

        StartGameInput startGame = new StartGameInput();
        startGame = input.getGames().get(0).getStartGame();



        Hero hero;
        if (action.getPlayerIdx() == 1) {
            hero = new Hero(startGame.getPlayerOneHero());
        } else if (action.getPlayerIdx() == 2) {
            hero = new Hero(startGame.getPlayerTwoHero());
        } else {
            // Handle invalid player index
            heroNode.put("error", "Invalid player index");
            actionNode.set("output", heroNode);
            output.add(actionNode);
            return;
        }

        heroNode.put("mana", hero.getCardInput().getMana());
        heroNode.put("description", hero.getCardInput().getDescription());

        ArrayNode colorsArray = heroNode.putArray("colors");
        for (String color : hero.getCardInput().getColors()) {
            colorsArray.add(color);
        }

        heroNode.put("name", hero.getCardInput().getName());
        heroNode.put("health", hero.getHealth());

        actionNode.set("output", heroNode);
        output.add(actionNode);
    }

}