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

        String[] specialMinions ={ "The Ripper ", "Miraj" , "The Cursed One", "Disciple"};

        if(playerTurn - 1 == 0  && p1.getDeck().size() != 0) {
            System.out.printf("intru in if ul de playerTurn(1)\n");
//            if(p1.getDeck().size() == 0) {
//            if(p1.getDeck()==null) {
//                actionNode.put("command", action.getCommand());
//                actionNode.put("handIdx", handIdx);
//                actionNode.put("error", "No cards in deck.");
//                output.add(actionNode);
//                System.out.printf("No cards in deck.\n");
//                return;
//            }

//            if(p1.getDeck().size() == 0) {
//                System.out.printf("No cards in deck(size == 0).\n");
//            }

//            if(p1.getMana() < p1.getDeck().get(handIdx).getMana()) {
//                actionNode.put("command", action.getCommand());
//                actionNode.put("handIdx", handIdx);
//                actionNode.put("error", "Not enough mana to place card on table.");
//                 output.add(actionNode);
//                return;
//            }

            if(handIdx >= p1.getDeck().size()) {
                return ;
            }

            System.out.printf("trec de conditia de oprire in if ul de playerTurn(1)\n");

            for (int i = 0 ; i < specialMinions.length; i++) {
                if(p1.getDeck().get(handIdx).equals(specialMinions[i])) {
                    System.out.printf("am un special minion in playerTurn(1)\n");
                    if (table.getTable().get(3).size() < NUM_CARDS) {
                        // add back row for player 1
                        table.getTable().get(3).add(p1.getDeck().get(handIdx));
//                        hand[playerTurn].removeCard(p1.getDeck().get(handIdx));
                        p1.decMana(p1.getDeck().get(action.getHandIdx()).getMana());
                        System.out.printf("am pus special minion in playerTurn(1)\n");
                        return;
                    } else if (table.getTable().get(2).size() < NUM_CARDS) {
                        // add front row for player 1
                        table.getTable().get(2).add(p1.getDeck().get(handIdx));
//                        hand[playerTurn].removeCard(p1.getDeck().get(handIdx));
                        p1.decMana(p1.getDeck().get(action.getHandIdx()).getMana());
                        System.out.printf("am pus special minion in playerTurn(1)\n");
                        return;
                    } else {
                        actionNode.put("command", action.getCommand());
                        actionNode.put("handIdx", handIdx);
                        actionNode.put("error", "Table is full.");
                        output.add(actionNode);
                        return;
                    }
                }
            }

            if(p1.getDeck().get(handIdx).equals("Berserker") || p1.getDeck().get(handIdx).equals("Sentinel")) {
                if(table.getTable().get(3).size() < NUM_CARDS) {
                    // add back row for player 1
                    table.getTable().get(3).add(p1.getDeck().get(handIdx));
//                    hand[playerTurn].removeCard(p1.getDeck().get(handIdx));
                    p1.decMana(p1.getDeck().get(handIdx).getMana());
                    System.out.printf("am pus un berserker sau sentinel in playerTurn(1)\n");
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", handIdx);
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                }


            }

            if(p1.getDeck().get(handIdx).equals("Goliath") || p1.getDeck().get(handIdx).equals("Warden")) {
                if(table.getTable().get(2).size() < NUM_CARDS) {
                   //add front row for player 1
                    table.getTable().get(2).add(p1.getDeck().get(handIdx));
//                    hand[playerTurn].removeCard(p1.getDeck().get(handIdx));
                    p1.decMana(p1.getDeck().get(action.getHandIdx()).getMana());
                    System.out.printf("am pus un Goliath sau Warden in playerTurn(1)\n");
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", handIdx);
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                }
            }
        } else {

            System.out.printf("intru in if ul de playerTurn(2)\n");
            if(p2.getDeck().size() == 0 || handIdx >= p2.getDeck().size()) {
                return ;
            }
            System.out.printf("trec de conditia de oprire in if ul de playerTurn(2)\n");


//            if(p2.getMana() < p2.getDeck().get(action.getHandIdx()).getMana()) {
//                actionNode.put("command", action.getCommand());
//                actionNode.put("handIdx", handIdx);
//                actionNode.put("error", "Not enough mana to place card on table.");
//                output.add(actionNode);
//                return;
//            }

            for ( int i = 0 ; i < specialMinions.length; i++) {
                if(p2.getDeck().get(handIdx).equals(specialMinions[i])) {
                    System.out.printf("am un special minion in playerTurn(2)\n");
                    //check if there is space on either rows
                    if (table.getTable().get(0).size() < NUM_CARDS) {
                        // add back row for player 2
                        table.getTable().get(0).add(p2.getDeck().get(handIdx));
//                        hand[playerTurn].removeCard(p2.getDeck().get(handIdx));
                        p2.decMana(p2.getDeck().get(handIdx).getMana());
                        System.out.printf("am pus special minion in playerTurn(2)\n");
                        return;
                    } else if (table.getTable().get(1).size() < NUM_CARDS) {
                        // add front row for player 2
                        table.getTable().get(1).add(p2.getDeck().get(handIdx));
//                        hand[playerTurn].removeCard(p2.getDeck().get(handIdx));
                        p2.decMana(p2.getDeck().get(handIdx).getMana());
                        System.out.printf("am pus special minion in playerTurn(2)\n");
                        return;
                    } else {
                        actionNode.put("command", action.getCommand());
                        actionNode.put("handIdx", handIdx);
                        actionNode.put("error", "Table is full.");
                        output.add(actionNode);
                        return;
                    }
                }
            }



            if(p2.getDeck().get(handIdx).equals("Berserker") || p2.getDeck().get(handIdx).equals("Sentinel")) {
                //add back row for player 2
                if( table.getTable().get(0).size() < NUM_CARDS) {
                    table.getTable().get(0).add(p2.getDeck().get(handIdx));
//                    hand[playerTurn].removeCard(p2.getDeck().get(handIdx));
                    p2.decMana(p2.getDeck().get(action.getHandIdx()).getMana());
                    System.out.printf("am pus un berserker sau sentinel in playerTurn(2)\n");
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", handIdx);
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                }
            }

            if(p2.getDeck().get(handIdx).equals("Goliath") || p2.getDeck().get(handIdx).equals("Warden")) {
                //add front row for player 2
                if(table.getTable().get(1).size() < NUM_CARDS) {
                    table.getTable().get(1).add(p2.getDeck().get(handIdx));
//                    hand[playerTurn].removeCard(p2.getDeck().get(handIdx));
                    p2.decMana(p2.getDeck().get(handIdx).getMana());
                    System.out.printf("am pus un Goliath sau Warden in playerTurn(2)\n");
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", handIdx);
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                    }
                }
            }


        for (int i = 0; i < NUM_ROWS; i++) {
            System.out.printf("Row %d: ", i);
            for (CardInput card : table.getTable().get(i)) {
                System.out.printf("%s, ", card.getName());
            }
            System.out.printf("\n");
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
            System.out.printf("CE PUN IN OUTPUT(1):\n");
            for (int i = 0; i < h1.getHand().size(); i++) {
                System.out.printf(" %s\n", h1.getHand().get(i).getName());

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
            System.out.printf("CE PUN IN OUTPUT(2):\n");
            for (int i = 0; i < h2.getHand().size(); i++) {
                System.out.printf(" %s\n", h2.getHand().get(i).getName());

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

        // Add the cards array to the action node under the key "output"
        actionNode.set("output", cardsArray);

        // Add the action node to the output array
        output.add(actionNode);
    }

}