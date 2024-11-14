package gameutils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;
import gameutils.cardsinfo.heroes.Hero;
import gameutils.cardsinfo.heroes.HeroFactory;

import java.util.ArrayList;
import java.util.LinkedList;

import gameutils.cardsinfo.minions.*;

import static gameutils.GameConstants.*;


public class CommandHandler {


    public void endPlayerTurn(Table table, Player[] player, Hand[] hand, int[] playerTurn, int[] turnCycle, int[] roundCnt) {
        turnCycle[0]++;

        if (playerTurn[0] == 0) {
            for (int j = 2; j < 4; j++) {
                for (Minions minion : table.getTable().get(j)) {
                    minion.setIsFrozen(0);
                }
            }
        } else {
            for (int j = 0; j < 2; j++) {
                for (Minions minion : table.getTable().get(j)) {
                    minion.setIsFrozen(0);
                }
            }
        }

        playerTurn[0] = playerTurn[0] == 0 ? 1 : 0;

        if (turnCycle[0] == 2) {
            roundCnt[0]++;
            player[0].updateMana(roundCnt[0]);
            player[1].updateMana(roundCnt[0]);

            if (!player[0].getDeck().isEmpty()) {
                hand[0].addCard(player[0].getDeck().remove(0));
            }
            if (!player[1].getDeck().isEmpty()) {
                hand[1].addCard(player[1].getDeck().remove(0));
            }

            turnCycle[0] = 0;
            for (int j = 0; j < 4; j++) {
                for (Minions minion : table.getTable().get(j)) {
                    minion.setHasAttacked(0);
                }
            }
            player[0].getHero().setHasAttacked(0);
            player[1].getHero().setHasAttacked(0);
        }
    }











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

    public void getPlayerTurn(ObjectNode actionNode, ArrayNode output, ActionsInput action, int playerTurn) {
        ObjectNode turnNode = actionNode.objectNode();
        turnNode.put("command", action.getCommand());
        turnNode.put("output", playerTurn + 1);
        output.add(turnNode);
    }

    public void getPlayerHero(ObjectNode actionNode, ArrayNode output, ActionsInput action, Input input, Player p1, Player p2) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        ObjectNode heroNode = actionNode.objectNode();

        Hero hero;
        if (action.getPlayerIdx() == 1) {
            hero = p1.getHero();
        } else if (action.getPlayerIdx() == 2) {
            hero = p2.getHero();
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
        heroNode.put("health", hero.getCard().getHealth());

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

    public void placeCard(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player p1, Player p2, Hand[] hand, int[] playerTurn, Table table, int handIdx, boolean[] ok) {
        if (playerTurn[0] == 0) {
            placeCardForPlayer(action, actionNode, output, p1, hand[0], BACK_ROW1, FRONT_ROW1, table, handIdx, ok);
        } else {
            placeCardForPlayer(action, actionNode, output, p2, hand[1], BACK_ROW2, FRONT_ROW2, table, handIdx, ok);
        }
        if (action.getHandIdx() < hand[playerTurn[0]].getHand().size() && ok[0]) {
            hand[playerTurn[0]].removeCard(hand[playerTurn[0]].getHand().get(action.getHandIdx()));
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
                    minion.setHasAttacked(0);
                    ok[0] = true;
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("error", "Cannot place card since row is full.");
                    output.add(actionNode);
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
                    minion.setHasAttacked(0);
                    ok[0] = true;
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("error", "Cannot place card since row is full.");
                    output.add(actionNode);
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
        ObjectNode cardAttackedNode = actionNode.objectNode();

        if (playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW1 || action.getCardAttacked().getX() == BACK_ROW1)) {
            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);

            actionNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(actionNode);
            return;
        }

        if (playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW2 || action.getCardAttacked().getX() == BACK_ROW2)) {

            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);


            actionNode.put("command", "cardUsesAttack");
            actionNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(actionNode);
            return;
        }

        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();
        int attackedX = action.getCardAttacked().getX();
        int attackedY = action.getCardAttacked().getY();


        if (table.getTable().get(attackerX).size() <= attackerY) {
            actionNode.put("command", action.getCommand());
            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);
            actionNode.put("error", "Attacker card is not on the table.");
            output.add(actionNode);
            return;
        }

        if (table.getTable().get(attackedX).size() <= attackedY) {
            actionNode.put("command", action.getCommand());
            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);
            actionNode.put("error", "Attacked card is not on the table.");
            output.add(actionNode);
            return;
        }


        Minions attackedMinion = table.getTable().get(attackedX).get(attackedY);
        Minions attackerMinion = table.getTable().get(attackerX).get(attackerY);
        if (table.verifyTankOnRow(playerTurn) == 1 && attackedMinion.verifyTank(attackedMinion) == 0) {

            actionNode.put("command", action.getCommand());
            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);
            actionNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(actionNode);
            return;
        }

        if (attackerMinion.getHasAttacked() == 1) {
            actionNode.put("command", action.getCommand());
            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);
            actionNode.put("error", "Attacker card has already attacked this turn.");
            output.add(actionNode);
            return;
        }

        attackerMinion.setHasAttacked(1);


        int damage = table.getTable().get(attackerX).get(attackerY).getCard().getAttackDamage();

        attackedMinion.decHealth(damage);

        if(attackedMinion.getCard().getHealth() <= 0) {
            table.getTable().get(attackedX).remove(attackedY);
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

    public void cardUsesAbility(ActionsInput action, ObjectNode actionNode, ArrayNode output, int playerTurn, Table table) {
        actionNode.put("command", action.getCommand());

        ObjectNode cardAttackerNode = actionNode.objectNode();
        ObjectNode cardAttackedNode = actionNode.objectNode();

        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();
        int attackedX = action.getCardAttacked().getX();
        int attackedY = action.getCardAttacked().getY();

        if (attackerY >= table.getTable().get(attackerX).size())
            return;
        if (attackedY >= table.getTable().get(attackedX).size())
            return;

        Minions attackerMinion = table.getTable().get(attackerX).get(attackerY);
        Minions attackedMinion = table.getTable().get(attackedX).get(attackedY);


        if(attackerMinion.getIsFrozen() == 1) {
            actionNode.put("error", "Attacker card is frozen.");
            output.add(actionNode);
            return;
        }

        if (attackerMinion.getHasAttacked() == 1) {
            actionNode.put("command", action.getCommand());
            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);
            actionNode.put("error", "Attacker card has already attacked this turn.");
            output.add(actionNode);
            return;
        }

        if (attackerMinion.getCard().getName().equals("Disciple")) {
            if (playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW2 || action.getCardAttacked().getX() == BACK_ROW2)) {
                cardAttackerNode.put("x", action.getCardAttacker().getX());
                cardAttackerNode.put("y", action.getCardAttacker().getY());
                actionNode.set("cardAttacker", cardAttackerNode);

                cardAttackedNode.put("x", action.getCardAttacked().getX());
                cardAttackedNode.put("y", action.getCardAttacked().getY());
                actionNode.set("cardAttacked", cardAttackedNode);

                actionNode.put("error", "Attacked card does not belong to the current player.");
                output.add(actionNode);
                return;
            } else if (playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW1 || action.getCardAttacked().getX() == BACK_ROW1)) {
                cardAttackerNode.put("x", action.getCardAttacker().getX());
                cardAttackerNode.put("y", action.getCardAttacker().getY());
                actionNode.set("cardAttacker", cardAttackerNode);

                cardAttackedNode.put("x", action.getCardAttacked().getX());
                cardAttackedNode.put("y", action.getCardAttacked().getY());
                actionNode.set("cardAttacked", cardAttackedNode);

                actionNode.put("error", "Attacked card does not belong to the current player.");
                output.add(actionNode);
                return;
            }
        }


        if (playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW1 || action.getCardAttacked().getX() == BACK_ROW1)
                && !attackerMinion.getCard().getName().equals("Disciple")) {

            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);

            actionNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(actionNode);
            return;
        }

        if (playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW2 || action.getCardAttacked().getX() == BACK_ROW2)
                && !attackerMinion.getCard().getName().equals("Disciple")) {

            cardAttackerNode.put("x", action.getCardAttacker().getX());
            cardAttackerNode.put("y", action.getCardAttacker().getY());
            actionNode.set("cardAttacker", cardAttackerNode);

            cardAttackedNode.put("x", action.getCardAttacked().getX());
            cardAttackedNode.put("y", action.getCardAttacked().getY());
            actionNode.set("cardAttacked", cardAttackedNode);


            actionNode.put("command", "cardUsesAbility");
            actionNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(actionNode);
            return;
        }


        if (!attackerMinion.getCard().getName().equals("Disciple")) {
            if (table.verifyTankOnRow(playerTurn) == 1 && attackedMinion.verifyTank(attackedMinion) == 0) {

                actionNode.put("command", action.getCommand());
                cardAttackerNode.put("x", action.getCardAttacker().getX());
                cardAttackerNode.put("y", action.getCardAttacker().getY());
                actionNode.set("cardAttacker", cardAttackerNode);

                cardAttackedNode.put("x", action.getCardAttacked().getX());
                cardAttackedNode.put("y", action.getCardAttacked().getY());
                actionNode.set("cardAttacked", cardAttackedNode);
                actionNode.put("error", "Attacked card is not of type 'Tank'.");
                output.add(actionNode);
                return;
            }

        }

        Minions attackerWithAbility = MinionsFactory.createMinion(attackerMinion);
        attackerWithAbility.ability(attackedMinion);

        attackerMinion.setHasAttacked(1);

        if (attackedMinion.getCard().getHealth() <= 0) {
            table.getTable().get(attackedX).remove(attackedY);
        }

    }

    public void useAttackHero(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player p1, Player p2, int playerTurn, Table table) {
        actionNode.put("command", action.getCommand());
        ObjectNode cardAttackerNode = actionNode.objectNode();
        ObjectNode gameEndNode = actionNode.objectNode();
        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();

        cardAttackerNode.put("x", attackerX);
        cardAttackerNode.put("y", attackerY);

        actionNode.set("cardAttacker", cardAttackerNode);

        if (attackerY >= table.getTable().get(attackerX).size())
            return;
        Minions attackerMinion = table.getTable().get(attackerX).get(attackerY);

        if (attackerMinion.getIsFrozen() == 1) {
            actionNode.put("error", "Attacker card is frozen.");
            output.add(actionNode);
            return;
        }

        if (attackerMinion.getHasAttacked() == 1) {
            actionNode.put("error", "Attacker card has already attacked this turn.");
            output.add(actionNode);
            return;
        }

        // && attackerMinion.verifyTank(attackerMinion) == 0 ??
        if (table.verifyTankOnRow(playerTurn) == 1) {
            actionNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(actionNode);
            return;
        }

        if (playerTurn == 0) {
            Hero heroPlayer2 = p2.getHero();
            heroPlayer2.decHealth(attackerMinion.getCard().getAttackDamage());

            if (heroPlayer2.getCard().getHealth() <= 0) {
                gameEndNode.put("gameEnded", "Player one killed the enemy hero.");
                output.add(gameEndNode);
                p1.incWinCnt();
            }
        } else {
            Hero heroPlayer1 = p1.getHero();
            heroPlayer1.decHealth(attackerMinion.getCard().getAttackDamage());

            if (heroPlayer1.getCard().getHealth() <= 0) {
                gameEndNode.put("gameEnded", "Player two killed the enemy hero.");
                output.add(gameEndNode);
                p2.incWinCnt();

            }
        }

        attackerMinion.setHasAttacked(1);



    }

    public void useHeroAbility(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player p1, Player p2, int playerTurn, Table table) {
        actionNode.put("command", action.getCommand());
        actionNode.put("affectedRow", action.getAffectedRow());


        int affectedRow  = action.getAffectedRow();

        if (playerTurn == 0) {
            Hero heroPlayer1 = p1.getHero();

            if (p1.getMana() < heroPlayer1.getCard().getMana()) {
                actionNode.put("error", "Not enough mana to use hero's ability.");
                output.add(actionNode);
                return;
            }

            if (heroPlayer1.getHasAttacked() == 1) {
                actionNode.put("error", "Hero has already attacked this turn.");
                output.add(actionNode);
                return;
            }

            // if offensive , affected row must be opponent's row
            if (heroPlayer1.verifyOffensive() == 1 && (affectedRow == FRONT_ROW1 || affectedRow == BACK_ROW1)) {
                actionNode.put("error", "Selected row does not belong to the enemy.");
                output.add(actionNode);
                return;
            }

            // if defensive , affected row must be player's row
            if (heroPlayer1.verifyDefensive() == 1 &&(affectedRow == FRONT_ROW2 || affectedRow == BACK_ROW2)) {
                actionNode.put("error", "Selected row does not belong to the current player.");
                output.add(actionNode);
                return;
            }

            Hero heroAbility = HeroFactory.createHero(heroPlayer1);
            heroAbility.ability(table.getTable().get(affectedRow));

            heroPlayer1.setHasAttacked(1);

            p1.decMana(heroPlayer1.getCard().getMana());

        } else {
            Hero heroPlayer2 = p2.getHero();

            if (p2.getMana() < heroPlayer2.getCard().getMana()) {
                actionNode.put("error", "Not enough mana to use hero's ability.");
                output.add(actionNode);
                return;
            }

            if (heroPlayer2.getHasAttacked() == 1) {
                actionNode.put("error", "Hero has already attacked this turn.");
                output.add(actionNode);
                return;
            }

            // if offensive , affected row must be opponent's row
            if (heroPlayer2.verifyOffensive() == 1 && (affectedRow == FRONT_ROW2 || affectedRow == BACK_ROW2)) {
                actionNode.put("error", "Selected row does not belong to the enemy.");
                output.add(actionNode);
                return;
            }

            // if defensive , affected row must be player's row
            if (heroPlayer2.verifyDefensive() == 1 && (affectedRow == FRONT_ROW1 || affectedRow == BACK_ROW1)) {
                actionNode.put("error", "Selected row does not belong to the current player.");
                output.add(actionNode);
                return;
            }

            Hero heroAbility = HeroFactory.createHero(heroPlayer2);
            heroAbility.ability(table.getTable().get(affectedRow));


            heroPlayer2.setHasAttacked(1);

            p2.decMana(heroPlayer2.getCard().getMana());

        }
    }

    public void getFrozenCardsOnTable(ActionsInput action, ObjectNode actionNode, ArrayNode output,Table table) {
        actionNode.put("command", action.getCommand());
        ArrayNode frozenArray = actionNode.putArray("output");

        for (LinkedList<Minions> row : table.getTable()) {
            for (Minions minion : row) {
                if (minion.getIsFrozen() == 1) {
                    ObjectNode frozenNode = actionNode.objectNode();
                    frozenNode.put("mana", minion.getCard().getMana());
                    frozenNode.put("attackDamage", minion.getCard().getAttackDamage());
                    frozenNode.put("health", minion.getCard().getHealth());
                    frozenNode.put("description", minion.getCard().getDescription());

                    ArrayNode colorsArray = frozenNode.putArray("colors");
                    for (String color : minion.getCard().getColors()) {
                        colorsArray.add(color);
                    }

                    frozenNode.put("name", minion.getCard().getName());
                    frozenArray.add(frozenNode);
                }
            }
        }

        output.add(actionNode);
    }

    public static void getPlayerWins(ActionsInput action, ObjectNode actionNode, ArrayNode output, int wins){
        actionNode.put("command", action.getCommand());
        actionNode.put("output", wins);
        output.add(actionNode);
    }

    public static void getTotalGamesPlayed(ActionsInput action, ObjectNode actionNode, ArrayNode output, int i){
        actionNode.put("command", action.getCommand());
        actionNode.put("output", i);
        output.add(actionNode);
    }

}
