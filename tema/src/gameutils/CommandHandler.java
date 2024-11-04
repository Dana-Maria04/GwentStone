package gameutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import gameutils.Player;
import gameutils.GameStats;
import gameutils.StartGame;
import gameutils.Table;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;
import gameutils.cardsinfo.heroes.Hero;
import gameutils.Deck;

import javax.smartcardio.Card;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.DoubleFunction;

import static gameutils.GameConstants.*;


public class CommandHandler {

    public void getPlayerDeck(ArrayList<CardInput> deckP1, ArrayList<CardInput> deckP2,ObjectNode actionNode, ActionsInput action, ArrayNode output) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());
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

    public void getPlayerMana(ObjectNode actionNode, ArrayNode output, ActionsInput action, Player p1, Player p2) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        ObjectNode manaNode = actionNode.objectNode();
        if(action.getPlayerIdx() - 1 == 0) {
            actionNode.put("output", p1.getMana());
        } else {
            actionNode.put("output", p2.getMana());
        }

        output.add(actionNode);
    }

    public void placeCard(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player p1, Player p2, Hand[] hand, int playerTurn, Table table, int handIdx) {

        String[] backMinions = {"Berserker", "Sentinel", "The Cursed One", "Disciple"};
        String[] frontMinions = {"Goliath", "Warden", "The Ripper", "Miraj"};

        if(playerTurn == 0  && hand[0].getHand().size() > 0) {
            if(handIdx >= hand[0].getHand().size()) {
                return;
            }
            for(int i = 0 ; i < backMinions.length; i++) {
                if (hand[0].getHand().get(handIdx).getName().equals(backMinions[i])) {
                    if (table.getTable().get(3).size() < NUM_CARDS && p1.getMana() >= hand[0].getHand().get(handIdx).getMana()) {
                        table.getTable().get(3).add(hand[0].getHand().get(handIdx));
                        p1.decMana(hand[0].getHand().get(handIdx).getMana());
                        return;
                    }
                }
            }
            for(int i = 0 ; i < frontMinions.length; i++) {
                if (hand[0].getHand().get(handIdx).getName().equals(frontMinions[i])) {
                    if (table.getTable().get(2).size() < NUM_CARDS && p1.getMana() >= hand[0].getHand().get(handIdx).getMana()) {
                        table.getTable().get(2).add(hand[0].getHand().get(handIdx));
                        p1.decMana(hand[0].getHand().get(handIdx).getMana());
                        return;
                    }
                }
            }

        } else {
            if(hand[1].getHand().size() <= 0 || handIdx >= hand[1].getHand().size()) {
                return ;
            }
            for(int i = 0 ; i < backMinions.length; i++) {
                if (hand[1].getHand().get(handIdx).getName().equals(backMinions[i])) {
                    if (table.getTable().get(0).size() < NUM_CARDS && p2.getMana() >= hand[1].getHand().get(handIdx).getMana()) {
                        table.getTable().get(0).add(hand[1].getHand().get(handIdx));
                        p2.decMana(hand[1].getHand().get(handIdx).getMana());
                        return;
                    }
                }
            }
            for(int i = 0 ; i < frontMinions.length; i++) {
                if (hand[1].getHand().get(handIdx).getName().equals(frontMinions[i])) {
                    if (table.getTable().get(1).size() < NUM_CARDS && p2.getMana() >= hand[1].getHand().get(handIdx).getMana()) {
                        table.getTable().get(1).add(hand[1].getHand().get(handIdx));
                        p2.decMana(hand[1].getHand().get(handIdx).getMana());
                        return;
                    }
                }
            }
        }
    }


    public void getCardsInHand(ActionsInput action, ObjectNode actionNode, ArrayNode output, Hand h1, Hand h2) {
        // Set the command and player index in the action node
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        // Create an ArrayNode to hold the cards
        ArrayNode cardsArray = actionNode.arrayNode();

        // Choose the correct hand based on the player index
        if (action.getPlayerIdx() - 1 == 0) {
//            System.out.printf("CE PUN IN OUTPUT(1):\n");
            for (int i = 0; i < h1.getHand().size(); i++) {
//                System.out.printf(" %s\n", h1.getHand().get(i).getName());

                // Create a node for each card
                ObjectNode handNode = actionNode.objectNode();
                handNode.put("mana", h1.getHand().get(i).getMana());
                handNode.put("attackDamage", h1.getHand().get(i).getAttackDamage());
                handNode.put("health", h1.getHand().get(i).getHealth());
                handNode.put("description", h1.getHand().get(i).getDescription());

                ArrayNode colorsArray = handNode.putArray("colors");
                for (String color : h1.getHand().get(i).getColors()) {
                    colorsArray.add(color);
                }

                handNode.put("name", h1.getHand().get(i).getName());
                cardsArray.add(handNode);
            }
        } else {
//            System.out.printf("CE PUN IN OUTPUT(2):\n");
            for (int i = 0; i < h2.getHand().size(); i++) {
//                System.out.printf(" %s\n", h2.getHand().get(i).getName());

                // Create a node for each card
                ObjectNode handNode = actionNode.objectNode();
                handNode.put("mana", h2.getHand().get(i).getMana());
                handNode.put("attackDamage", h2.getHand().get(i).getAttackDamage());
                handNode.put("health", h2.getHand().get(i).getHealth());
                handNode.put("description", h2.getHand().get(i).getDescription());

                ArrayNode colorsArray = handNode.putArray("colors");
                for (String color : h2.getHand().get(i).getColors()) {
                    colorsArray.add(color);
                }

                handNode.put("name", h2.getHand().get(i).getName());
                cardsArray.add(handNode);
            }
        }

        actionNode.set("output", cardsArray);
        output.add(actionNode);
    }

    public void getCardsOnTable(ActionsInput action, ObjectNode actionNode, ArrayNode output, Table table) {
        actionNode.put("command", action.getCommand());
        ArrayNode tableArray = actionNode.putArray("output");
        for (LinkedList<CardInput> row : table.getTable()) {
            ArrayNode rowArray = actionNode.arrayNode();
            for (CardInput card : row) {
                ObjectNode cardNode = actionNode.objectNode();
                cardNode.put("mana", card.getMana());
                cardNode.put("attackDamage", card.getAttackDamage());
                cardNode.put("health", card.getHealth());
                cardNode.put("description", card.getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for (String color : card.getColors()) {
                    colorsArray.add(color);
                }
                cardNode.put("name", card.getName());
                rowArray.add(cardNode);
            }
            tableArray.add(rowArray);
        }
        output.add(actionNode);
    }

    public void cardUsesAttack(ActionsInput action, ObjectNode actionNode, ArrayNode output, int playerTurn, Table table){
        actionNode.put("command", action.getCommand());

        Coordinates attackerCoord = new Coordinates(action.getCardAttacker().getX(), action.getCardAttacker().getY());
        actionNode.put("cardAttacker", attackerCoord.toString());

        Coordinates attackedCoord = new Coordinates(action.getCardAttacked().getX(), action.getCardAttacked().getY());
        actionNode.put("cardAttacked", attackedCoord.toString());

        if(playerTurn - 1 == 0 && (action.getCardAttacked().getX() == FRONT_ROW1 || action.getCardAttacked().getX() == BACK_ROW1)) {
            // err msg
            output.add(actionNode);
            return;
        }

        if(playerTurn - 1 == 1 && (action.getCardAttacked().getX() == FRONT_ROW2 || action.getCardAttacked().getX() == BACK_ROW2)) {
            // err msg
            output.add(actionNode);
            return;
        }

        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();
        int attackedX = action.getCardAttacked().getX();
        int attackedY = action.getCardAttacked().getY();

        CardInput attacker = table.getTable().get(attackerX).get(attackerY);
        CardInput attacked = table.getTable().get(attackedX).get(attackedY);

        if(attacker.getAttackDamage() == 0) {
            // err msg
            output.add(actionNode);
            return;
        }


        output.add(actionNode);

    }

}