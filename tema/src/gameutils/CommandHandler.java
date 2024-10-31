package gameutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
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
}