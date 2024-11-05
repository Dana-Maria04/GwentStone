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

    public void getPlayerDeck(ArrayList<Cards> deckP1, ArrayList<Cards> deckP2, ObjectNode actionNode, ActionsInput action, ArrayNode output) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());
        ArrayNode deckArray = actionNode.putArray("output");

        ArrayList<Cards> playerDeck;
        playerDeck = new ArrayList<>();

        if (action.getPlayerIdx() - 1 == 0)
            playerDeck = deckP1;
        else
            playerDeck = deckP2;


        for (Cards card : playerDeck) {
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
            deckArray.add(cardNode);
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
        if (action.getPlayerIdx() - 1 == 0) {
            actionNode.put("output", p1.getMana());
        } else {
            actionNode.put("output", p2.getMana());
        }

        output.add(actionNode);
    }
    public void placeCard(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player p1, Player p2, Hand[] hand, int playerTurn, Table table, int handIdx, boolean[] ok) {
        if (playerTurn == 0) {
            placeCardForPlayer(action, actionNode, output, p1, hand[0], BACK_ROW1, FRONT_ROW1, table, handIdx, ok);
        } else {
            placeCardForPlayer(action, actionNode, output, p2, hand[1], BACK_ROW2, FRONT_ROW2, table, handIdx, ok);
        }
    }

    private void placeCardForPlayer(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player player, Hand hand, int backRow, int frontRow, Table table, int handIdx, boolean[] ok) {
        String[] backMinions = {"Berserker", "Sentinel", "The Cursed One", "Disciple"};
        String[] frontMinions = {"Goliath", "Warden", "The Ripper", "Miraj"};

        if (hand.getHand().size() <= 0 || handIdx >= hand.getHand().size()) {
            return;
        }

        if (player.getMana() < hand.getHand().get(handIdx).getCard().getMana()) {
            actionNode.put("command", action.getCommand());
            actionNode.put("error", "Not enough mana to place card on table.");
            actionNode.put("handIdx", handIdx);
            output.add(actionNode);
            return;
        }

        for (String backMinion : backMinions) {
            if (hand.getHand().get(handIdx).getCard().getName().equals(backMinion)) {
                if (table.getTable().get(backRow).size() < NUM_CARDS) {
                    Minions minion = new Minions(hand.getHand().get(handIdx));
                    table.getTable().get(backRow).add(minion);
                    player.decMana(hand.getHand().get(handIdx).getCard().getMana());
                    ok[0] = true;
                    return;
                }
            }
        }

        for (String frontMinion : frontMinions) {
            if (hand.getHand().get(handIdx).getCard().getName().equals(frontMinion)) {
                if (table.getTable().get(frontRow).size() < NUM_CARDS) {
                    Minions minion = new Minions(hand.getHand().get(handIdx));
                    table.getTable().get(frontRow).add(minion);
                    player.decMana(hand.getHand().get(handIdx).getCard().getMana());
                    ok[0] = true;
                    return;
                }
            }
        }
    }


    public void getCardsInHand(ActionsInput action, ObjectNode actionNode, ArrayNode output, Hand h1, Hand h2) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        ArrayNode cardsArray = actionNode.arrayNode();

        Hand selectedHand;
        if (action.getPlayerIdx() - 1 == 0) {
            selectedHand = h1;
        } else {
            selectedHand = h2;
        }
        addCardsToArrayNode(selectedHand, cardsArray, actionNode);

        actionNode.set("output", cardsArray);
        output.add(actionNode);
    }

    private void addCardsToArrayNode(Hand hand, ArrayNode cardsArray, ObjectNode actionNode) {
        for (int i = 0; i < hand.getHand().size(); i++) {
            ObjectNode handNode = actionNode.objectNode();
            handNode.put("mana", hand.getHand().get(i).getCard().getMana());
            handNode.put("attackDamage", hand.getHand().get(i).getCard().getAttackDamage());
            handNode.put("health", hand.getHand().get(i).getCard().getHealth());
            handNode.put("description", hand.getHand().get(i).getCard().getDescription());

            ArrayNode colorsArray = handNode.putArray("colors");
            for (String color : hand.getHand().get(i).getCard().getColors()) {
                colorsArray.add(color);
            }

            handNode.put("name", hand.getHand().get(i).getCard().getName());
            cardsArray.add(handNode);
        }
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

    public void cardUsesAttack(ActionsInput action, ObjectNode actionNode, ArrayNode output, int playerTurn, Table table) {
        actionNode.put("command", action.getCommand());

        ObjectNode cardAttackerNode = actionNode.objectNode();
        cardAttackerNode.put("x", action.getCardAttacker().getX());
        cardAttackerNode.put("y", action.getCardAttacker().getY());
        actionNode.set("cardAttacker", cardAttackerNode);

        ObjectNode cardAttackedNode = actionNode.objectNode();
        cardAttackedNode.put("x", action.getCardAttacked().getX());
        cardAttackedNode.put("y", action.getCardAttacked().getX());
        actionNode.set("cardAttacked", cardAttackedNode);


        System.out.printf("playerTurn: %d cu X%d si Y%d\n", playerTurn,action.getCardAttacked().getX(), action.getCardAttacked().getY() );
        if(playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW1 || action.getCardAttacked().getX() == BACK_ROW1)) {

            actionNode.put("error", "Attacked card does not belong to the enemy.");
            // TODO put cardattacker and cardattacked coordinates
            output.add(actionNode);
            return;
        }

        if(playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW2 || action.getCardAttacked().getX() == BACK_ROW2)) {
            actionNode.put("command", "cardUsesAttack");


            // TODO put cardattacker and cardattacked coordinates

            actionNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(actionNode);
            return;
        }

        String[] tankMinions = {"Goliath", "Warden"};

        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();
        int attackedX = action.getCardAttacked().getX();
        int attackedY = action.getCardAttacked().getY();


        if (attackerY >= table.getTable().get(attackerX).size() || table.getTable().get(attackerX).isEmpty()) {
            System.out.printf("invalid\n");
            return;
        }

        if (attackedY >= table.getTable().get(attackedX).size() || table.getTable().get(attackedX).isEmpty()) {
            System.out.printf("invalid\n");
            return;
        }

        int damage = table.getTable().get(attackerX).get(attackerY).getCard().getAttackDamage();
        CardInput tableCard= table.getTable().get(attackedX).get(attackedY).getCard();

        if (attackedX == 0 || attackedX == 1 && attackerX == 2 || attackerX == 3) {
            for (Minions minion : table.getTable().get(1)) {
                if (minion.verifyTank(minion) == 1) {
                    // the minion is tank
                    if (damage >= minion.getCard().getHealth()) {
                        table.getTable().get(1).remove(minion);
                        return ;
                    } else {
                        minion.decHealth(damage);
                        return ;
                    }
                }
            }

            if (damage >= tableCard.getHealth()) {
                table.getTable().get(attackedX).remove(attackedY);
                return;
            }
        } else if (attackedX == 2 || attackedX == 3 && attackerX == 0 || attackerX == 1) {
            for (Minions minion : table.getTable().get(2)) {
                if (minion.verifyTank(minion) == 1) {
                    // the minion is tank
                    if (damage >= minion.getCard().getHealth()) {
                        table.getTable().get(2).remove(minion);
                    } else {
                        minion.decHealth(damage);
                    }
                }
            }

            if (damage >= tableCard.getHealth() && table.getTable().get(attackedX).size() > attackedY) {
                table.getTable().get(attackedX).remove(attackedY);
                return;
            }
        }
    }

    public void getCardAtPosition(ActionsInput action, ObjectNode actionNode, ArrayNode output, Table table) {
        actionNode.put("command", action.getCommand());
        actionNode.put("x", action.getX());
        actionNode.put("y", action.getY());

        if (table.getTable().get(action.getX()).size() <= action.getY()) {
            actionNode.put("output", "No card available at that position.");
        } else {
            Minions card = new Minions(table.getTable().get(action.getX()).get(action.getY()));
            ObjectNode cardNode = actionNode.objectNode();
            cardNode.put("name", card.getCard().getName());
            cardNode.put("mana", card.getCard().getMana());
            cardNode.put("attackDamage", card.getCard().getAttackDamage());
            cardNode.put("health", card.getCard().getHealth());
            cardNode.put("description", card.getCard().getDescription());

            ArrayNode colorsArray = cardNode.putArray("colors");
            for (String color : card.getCard().getColors()) {
                colorsArray.add(color);
            }
            cardNode.set("colors", colorsArray);
            actionNode.set("output", cardNode);
        }

        output.add(actionNode);
    }

}