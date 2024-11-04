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

    public void getPlayerDeck(ArrayList<Cards> deckP1, ArrayList<Cards> deckP2,ObjectNode actionNode, ActionsInput action, ArrayNode output) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());
        ArrayNode deckArray = actionNode.putArray("output");

        ArrayList<Cards> playerDeck;
        playerDeck = new ArrayList<>();

        if(action.getPlayerIdx() - 1 == 0)
            playerDeck = deckP1;
        else
            playerDeck = deckP2;


        for (Cards card : playerDeck) {
            ObjectNode cardNode = actionNode.objectNode();

            if(!card.getCard().getName().equals("Berserker") && !card.getCard().getName().equals("Sentinel")) {
                cardNode.put("mana", card.getCard().getMana());
                cardNode.put("attackDamage", card.getCard().getAttackDamage());
                cardNode.put("health", card.getCard().getHealth());
                cardNode.put("description", card.getCard().getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for (String color : card.getCard().getColors()) {
                    colorsArray.add(color);
                }

                cardNode.put("name", card.getCard().getName());

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

    public void placeCard(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player p1, Player p2, Hand[] hand, int playerTurn, Table table, int handIdx, boolean[] ok) {

        String[] backMinions = {"Berserker", "Sentinel", "The Cursed One", "Disciple"};
        String[] frontMinions = {"Goliath", "Warden", "The Ripper", "Miraj"};

        if(playerTurn == 0  && hand[0].getHand().size() > 0) {
            if(handIdx >= hand[0].getHand().size()) {
                return;
            }


            if(p1.getMana() < hand[0].getHand().get(handIdx).getCard().getMana()) {
                System.out.printf("messi\n");
                actionNode.put("command", action.getCommand());
                actionNode.put("error","Not enough mana to place card on table.");
                actionNode.put("handIdx", handIdx);
                output.add(actionNode);
                return;
            }

            for (int i = 0; i < backMinions.length; i++) {
                if (hand[0].getHand().get(handIdx).getCard().getName().equals(backMinions[i])) {
                    if (table.getTable().get(3).size() < NUM_CARDS) {
                        Minions minion = new Minions(hand[0].getHand().get(handIdx));
                        table.getTable().get(3).add(minion);
                        p1.decMana(hand[0].getHand().get(handIdx).getCard().getMana());
                        ok[0] = true;
                        return;
                    }
                }
            }

            for(int i = 0 ; i < frontMinions.length; i++) {
                if (hand[0].getHand().get(handIdx).getCard().getName().equals(frontMinions[i])) {
                    if (table.getTable().get(2).size() < NUM_CARDS ) {
                        Minions minion = new Minions(hand[0].getHand().get(handIdx));
                        table.getTable().get(2).add(minion);

                        p1.decMana(hand[0].getHand().get(handIdx).getCard().getMana());
                        ok[0] = true;
                        return;
                    }
                }
            }

        } else {
            if(hand[1].getHand().size() <= 0 || handIdx >= hand[1].getHand().size()) {
                return ;
            }

            if(p2.getMana() < hand[1].getHand().get(handIdx).getCard().getMana()) {
                System.out.printf("neymar\n");
                actionNode.put("command", action.getCommand());
                actionNode.put("error","Not enough mana to place card on table.");
                actionNode.put("handIdx", handIdx);
                output.add(actionNode);
                return;
            }

            for(int i = 0 ; i < backMinions.length; i++) {
                if (hand[1].getHand().get(handIdx).getCard().getName().equals(backMinions[i])) {
                    if (table.getTable().get(0).size() < NUM_CARDS) {
                        Minions minion = new Minions(hand[1].getHand().get(handIdx));
                        table.getTable().get(0).add(minion);
                        p2.decMana(hand[1].getHand().get(handIdx).getCard().getMana());
                        ok[0] = true;
                        return;
                    }
                }
            }
            for(int i = 0 ; i < frontMinions.length; i++) {
                if (hand[1].getHand().get(handIdx).getCard().getName().equals(frontMinions[i])) {
                    if (table.getTable().get(1).size() < NUM_CARDS) {
                        Minions minion = new Minions(hand[1].getHand().get(handIdx));
                        table.getTable().get(1).add(minion);

                        p2.decMana(hand[1].getHand().get(handIdx).getCard().getMana());
                        ok[0] = true;
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
                handNode.put("mana", h1.getHand().get(i).getCard().getMana());
                handNode.put("attackDamage", h1.getHand().get(i).getCard().getAttackDamage());
                handNode.put("health", h1.getHand().get(i).getCard().getHealth());
                handNode.put("description", h1.getHand().get(i).getCard().getDescription());

                ArrayNode colorsArray = handNode.putArray("colors");
                for (String color : h1.getHand().get(i).getCard().getColors()) {
                    colorsArray.add(color);
                }

                handNode.put("name", h1.getHand().get(i).getCard().getName());
                cardsArray.add(handNode);
            }
        } else {
//            System.out.printf("CE PUN IN OUTPUT(2):\n");
            for (int i = 0; i < h2.getHand().size(); i++) {
//                System.out.printf(" %s\n", h2.getHand().get(i).getName());

                // Create a node for each card
                ObjectNode handNode = actionNode.objectNode();
                handNode.put("mana", h2.getHand().get(i).getCard().getMana());
                handNode.put("attackDamage", h2.getHand().get(i).getCard().getAttackDamage());
                handNode.put("health", h2.getHand().get(i).getCard().getHealth());
                handNode.put("description", h2.getHand().get(i).getCard().getDescription());

                ArrayNode colorsArray = handNode.putArray("colors");
                for (String color : h2.getHand().get(i).getCard().getColors()) {
                    colorsArray.add(color);
                }

                handNode.put("name", h2.getHand().get(i).getCard().getName());
                cardsArray.add(handNode);
            }
        }

        actionNode.set("output", cardsArray);
        output.add(actionNode);
    }

    public void getCardsOnTable(ActionsInput action, ObjectNode actionNode, ArrayNode output, Table table) {
        actionNode.put("command", action.getCommand());
        ArrayNode tableArray = actionNode.putArray("output");
        for (LinkedList<Minions> row : table.getTable()) {
            ArrayNode rowArray = actionNode.arrayNode();
            for (Minions card : row) {
                ObjectNode cardNode = actionNode.objectNode();
                cardNode.put("mana", card.getCard().getMana());
                cardNode.put("attackDamage", card.getCard().getAttackDamage());
                cardNode.put("health", card.getCard().getHealth());
                cardNode.put("description", card.getCard().getDescription());
                ArrayNode colorsArray = cardNode.putArray("colors");
                for (String color : card.getCard().getColors()) {
                    colorsArray.add(color);
                }
                cardNode.put("name", card.getCard().getName());
                rowArray.add(cardNode);
            }
            tableArray.add(rowArray);
        }
        output.add(actionNode);
    }

//    public void cardUsesAttack(ActionsInput action, ObjectNode actionNode, ArrayNode output, int playerTurn, Table table){
////        actionNode.put("command", action.getCommand());
////
////        Coordinates attackerCoord = new Coordinates(action.getCardAttacker().getX(), action.getCardAttacker().getY());
//////        actionNode.put("cardAttacker", attackerCoord.toString());
////
////        Coordinates attackedCoord = new Coordinates(action.getCardAttacked().getX(), action.getCardAttacked().getY());
//////        actionNode.put("cardAttacked", attackedCoord.toString());
//
//        if(playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW1 || action.getCardAttacked().getX() == BACK_ROW1)) {
//            actionNode.put("error", "Attacked card does not belong to the enemy.");
//            output.add(actionNode);
//            return;
//        }
//
//        if(playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW2 || action.getCardAttacked().getX() == BACK_ROW2)) {
//            actionNode.put("error", "Attacked card does not belong to the enemy.");
//            output.add(actionNode);
//            return;
//        }
//
//        String[] tankMinions = {"Goliath", "Warden"};
//
//        int attackerX = action.getCardAttacker().getX();
//        int attackerY = action.getCardAttacker().getY();
//        int attackedX = action.getCardAttacked().getX();
//        int attackedY = action.getCardAttacked().getY();
//
//        if(table.getTable().get(attackerX).get(attackerY).getAttackDamage() >=  table.getTable().get(attackedX).get(attackedY).getHealth()){
//            table.getTable().get(attackedX).remove(attackedY);
//            return;
//        }else{
//            int healthAfterDamage = table.getTable().get(attackedX).get(attackedY).getHealth() - table.getTable().get(attackerX).get(attackerY).getAttackDamage();
//            table.getTable().get(attackedX).get(attackedY).setHealth(healthAfterDamage);
//            return;
//        }


//        CardInput attacker = table.getTable().get(attackerX).get(attackerY);
//        CardInput attacked = table.getTable().get(attackedX).get(attackedY);

        //2nd player gets attacked and check if he has tanks

//        if(attackedX == 0 || attackedX == 1 && attackerX == 2 || attackerX == 3) {
//            int verifyTank = 0;
//            for(int i = 0; i < 2; i++) {
//                for(CardInput card : table.getTable().get(i)) {
//                    if(card.getName().equals("Goliath") || card.getName().equals("Warden")) {
//                        verifyTank = 1;
//                    }
//                }
//            }
//            if(verifyTank == 1) {
//
//            }
//        }


//        output.add(actionNode);

//    }

}