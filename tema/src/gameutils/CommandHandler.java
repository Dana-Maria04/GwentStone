package gameutils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import fileio.ActionsInput;

import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;
import gameutils.cardsinfo.heroes.Hero;
import gameutils.cardsinfo.heroes.HeroFactory;

import java.util.ArrayList;
import java.util.LinkedList;

import gameutils.cardsinfo.minions.SpecialMinionsFactory;

import static gameutils.GameConstants.BACK_ROW1;
import static gameutils.GameConstants.BACK_ROW2;
import static gameutils.GameConstants.FRONT_ROW1;
import static gameutils.GameConstants.FRONT_ROW2;
import static gameutils.GameConstants.NUM_CARDS;
import static gameutils.GameConstants.NUM_ROWS;



/**
 * Class that handles the commands given by the player
 */
public class CommandHandler {


    /**
     * Gets the player's deck, also storing it in a Json node
     *
     * @param deckP1      the deck of player 1
     * @param deckP2      the deck of player 2
     * @param actionNode  the JSON node to store the command result
     * @param action      the action containing player information
     * @param output      the overall output JSON array
     */
    public void getPlayerDeck(final ArrayList<Cards> deckP1, final ArrayList<Cards> deckP2,
                              final ObjectNode actionNode,
                              final ActionsInput action, final ArrayNode output) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());
        ArrayNode deckArray = actionNode.putArray("output");

        ArrayList<Cards> playerDeck;
//        playerDeck = new ArrayList<>();

        if (action.getPlayerIdx() - 1 == 0) {
            playerDeck = deckP1;
        } else {
            playerDeck = deckP2;
        }

        for (Cards card : playerDeck) {
            ObjectNode cardNode = createCardNode(actionNode, card.getCard(), false);
            deckArray.add(cardNode);
        }
        output.add(actionNode);
    }

    /**
     * Gets the player's turn and stores it in a Json node
     *
     * @param actionNode  the JSON node to store the command result
     * @param output      the overall output JSON array
     * @param action      the action input containing the command details
     * @param playerTurn  the current player's turn (0 or 1)
     */
    public void getPlayerTurn(final ObjectNode actionNode, final ArrayNode output,
                              final ActionsInput action, final int playerTurn) {
        ObjectNode turnNode = actionNode.objectNode();
        turnNode.put("command", action.getCommand());
        turnNode.put("output", playerTurn + 1);
        output.add(turnNode);
    }

    /**
     * Gets the hero of the specified player
     *
     * @param actionNode  the JSON node for storing the command result
     * @param output      the overall output JSON array
     * @param action      the action input containing player information
     * @param p1          player 1
     * @param p2          player 2
     */
    public void getPlayerHero(final ObjectNode actionNode, final ArrayNode output,
                              final ActionsInput action,
                              final Player p1, final Player p2) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        Hero hero;
        if (action.getPlayerIdx() == 1) {
            hero = p1.getHero();
        } else {
            hero = p2.getHero();
        }

        ObjectNode heroNode = createCardNode(actionNode, hero.getCardInput(), true);
        actionNode.set("output", heroNode);
        output.add(actionNode);
    }


    /**
     * Gets the player's mana by checking the player index and
     * returning the mana of the corresponding one
     *
     * @param actionNode  the JSON node to store the command result
     * @param output      the overall output JSON array
     * @param action      the action input containing player information
     * @param p1          player 1
     * @param p2          player 2
     */
    public void getPlayerMana(final ObjectNode actionNode, final ArrayNode output,
                              final ActionsInput action,
                              final Player p1, final Player p2) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() - 1 == 0) {
            actionNode.put("output", p1.getMana());
        } else {
            actionNode.put("output", p2.getMana());
        }

        output.add(actionNode);
    }

    /**
     * Handles the placement of a card on the table for the current player.
     * It checks the player's turn and places the card on the corresponding row.
     * In the end it removes the card from the player's hand
     *
     * @param action       the action containing card placement details
     * @param actionNode   the JSON node to store the command result
     * @param output       the overall output JSON array
     * @param p1           player 1
     * @param p2           player 2
     * @param hand         the array of hands for each player
     * @param playerTurn   the current player's turn(0 or 1)
     * @param table        the game table
     * @param handIdx      the index of the card in the player's hand
     * @param ok           a flag indicating whether the card was successfully placed
     */
    public void placeCard(final ActionsInput action, final ObjectNode actionNode,
                          final ArrayNode output, final Player p1, final Player p2,
                          final Hand[] hand, final int playerTurn, final Table table,
                          final int handIdx, final boolean[] ok) {
        if (playerTurn == 0) {
            placeCardForPlayer(action, actionNode, output, p1, hand[0], BACK_ROW1, FRONT_ROW1,
                    table, handIdx, ok);
        } else {
            placeCardForPlayer(action, actionNode, output, p2, hand[1], BACK_ROW2, FRONT_ROW2,
                    table, handIdx, ok);
        }
        if (action.getHandIdx() < hand[playerTurn].getHand().size() && ok[0]) {
            hand[playerTurn].removeCard(hand[playerTurn].getHand().get(action.getHandIdx()));
        }
    }

    /**
     * Handles the placement of a card on the table for a specific player.
     * It checks if the player has enough mana to place the card and if the row is full.
     * If the card is successfully placed, it decrements the player's mana
     * and sets the flag to true (indicating the card was placed)
     *
     * @param action     the action containing details about the card placement
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param player     the player attempting to place the card
     * @param hand       the hand of the player
     * @param backRow    the index of the player's back row on the table
     * @param frontRow   the index of the player's front row on the table
     * @param table      the game table where cards are placed
     * @param handIdx    the index of the card in the player's hand
     * @param ok         a flag array indicating if the card was successfully placed
     */
    private void placeCardForPlayer(final ActionsInput action, final ObjectNode actionNode,
                                    final ArrayNode output, final Player player, final Hand hand,
                                    final int backRow, final int frontRow, final Table table,
                                    final int handIdx, final boolean[] ok) {
        String[] backMinions = {"Berserker", "Sentinel", "The Cursed One", "Disciple"};
        String[] frontMinions = {"Goliath", "Warden", "The Ripper", "Miraj"};

        if (hand.getHand().isEmpty() || handIdx >= hand.getHand().size()) {
            return;
        }

        if (player.getMana() < hand.getHand().get(handIdx).getCard().getMana()) {
            actionNode.put("command", action.getCommand());
            actionNode.put("error", "Not enough mana to place card on table.");
            actionNode.put("handIdx", handIdx);
            output.add(actionNode);
            return;
        }

        placeFrontMinionBackMinion(backMinions, actionNode, table, backRow, hand,
                handIdx, player, ok, action, output);
        placeFrontMinionBackMinion(frontMinions, actionNode, table, frontRow, hand,
                handIdx, player, ok, action, output);

    }

    private void placeFrontMinionBackMinion(final String[] minionsCards,
                                            final ObjectNode actionNode, final Table table,
                                            final int row, final Hand hand, final int handIdx,
                                            final Player player, final boolean[] ok,
                                            final ActionsInput action, final ArrayNode output) {
        for (String card : minionsCards) {
            if (hand.getHand().get(handIdx).getCard().getName().equals(card)) {
                if (table.getTable().get(row).size() < NUM_CARDS) {
                    Minions minion = new Minions(hand.getHand().get(handIdx));
                    table.getTable().get(row).add(minion);
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


    /**
     * Retrieves the cards in the specified player's hand
     *
     * @param action     the action containing the player's index
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param h1         the hand of player 1
     * @param h2         the hand of player 2
     */
    public void getCardsInHand(final ActionsInput action, final ObjectNode actionNode,
                               final ArrayNode output, final Hand h1, final Hand h2) {
        actionNode.put("command", action.getCommand());
        actionNode.put("playerIdx", action.getPlayerIdx());

        ArrayNode cardsArray = actionNode.arrayNode();

        Hand selectedHand;
        if (action.getPlayerIdx() - 1 == 0) {
            selectedHand = h1;
        } else {
            selectedHand = h2;
        }

        for (int i = 0; i < selectedHand.getHand().size(); i++) {
            ObjectNode handNode = createCardNode(actionNode, selectedHand.getHand()
                    .get(i).getCard(), false);
            cardsArray.add(handNode);
        }

        actionNode.set("output", cardsArray);
        output.add(actionNode);
    }


    /**
     * Gets and shows the cards on the table
     *
     * @param action     the action containing the command details
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param table      the game table containing the cards
     */
    public void getCardsOnTable(final ActionsInput action, final ObjectNode actionNode,
                                final ArrayNode output, final Table table) {
        actionNode.put("command", action.getCommand());
        ArrayNode tableArray = actionNode.putArray("output");
        for (LinkedList<Minions> row : table.getTable()) {
            ArrayNode rowArray = actionNode.arrayNode();

            for (Minions card : row) {
                ObjectNode cardNode = createCardNode(actionNode, card.getCard(), false);
                rowArray.add(cardNode);
            }

            tableArray.add(rowArray);
        }
        output.add(actionNode);
    }


    /**
     * Ends the current player's turn, updating the game state and preparing
     * for the next player's turn
     *
     * @param table     the current game table containing minions on rows
     * @param player    the array of players in the game
     * @param hand      the array of hands for each player
     * @param gameStats the statistics and state of the current game
     */
    public void endPlayerTurn(final Table table, final Player[] player, final Hand[] hand,
                              final GameStats gameStats) {

        gameStats.setTurnCycle(gameStats.getTurnCycle() + 1);

        if (gameStats.getPlayerTurn() == 0) {
            for (int j = 2; j < NUM_ROWS; j++) {
                for (Minions minion : table.getTable().get(j)) {
                    minion.setIsFrozen(0);
                }
            }
        } else {
            for (int j = 0; j < FRONT_ROW1; j++) {
                for (Minions minion : table.getTable().get(j)) {
                    minion.setIsFrozen(0);
                }
            }
        }

        if (gameStats.getPlayerTurn() == 0) {
            gameStats.setPlayerTurn(1);
        } else {
            gameStats.setPlayerTurn(0);
        }

        if (gameStats.getTurnCycle() == 2) {
            gameStats.setRoundCnt(gameStats.getRoundCnt() + 1);

            player[0].updateMana(gameStats.getRoundCnt());
            player[1].updateMana(gameStats.getRoundCnt());

            if (!player[0].getDeck().isEmpty()) {
                hand[0].addCard(player[0].getDeck().remove(0));
            }
            if (!player[1].getDeck().isEmpty()) {
                hand[1].addCard(player[1].getDeck().remove(0));
            }

            gameStats.setTurnCycle(0);

            for (int j = 0; j < NUM_ROWS; j++) {
                for (Minions minion : table.getTable().get(j)) {
                    minion.setHasAttacked(0);
                }
            }
            player[0].getHero().setHasAttacked(0);
            player[1].getHero().setHasAttacked(0);
        }
    }

    /**
     * Executes an attack from one card to another on the table, checks rules,
     * removes target if health is 0, and marks attacker as having attacked
     *
     * @param action     the action containing the attacker and target details
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param playerTurn the index of the player whose turn it is (0 or 1)
     * @param table      the game table containing the cards
     */
    public void cardUsesAttack(final ActionsInput action, final ObjectNode actionNode,
                               final ArrayNode output, final int playerTurn, final Table table) {
        actionNode.put("command", action.getCommand());

        if (playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW1 || action
                .getCardAttacked().getX() == BACK_ROW1)) {
            addCardActionError(actionNode, action, output,
                    "Attacked card does not belong to the enemy.");
            return;
        }

        if (playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW2
                || action.getCardAttacked().getX() == BACK_ROW2)) {
            addCardActionError(actionNode, action, output,
                    "Attacked card does not belong to the enemy.");
            return;
        }

        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();
        int attackedX = action.getCardAttacked().getX();
        int attackedY = action.getCardAttacked().getY();

        if (table.getTable().get(attackerX).size() <= attackerY) {
            addCardActionError(actionNode, action, output,
                    "Attacker card is not on the table.");
            return;
        }

        if (table.getTable().get(attackedX).size() <= attackedY) {
            addCardActionError(actionNode, action, output,
                    "Attacked card is not on the table.");
            return;
        }

        Minions attackedMinion = table.getTable().get(attackedX).get(attackedY);
        Minions attackerMinion = table.getTable().get(attackerX).get(attackerY);
        if (table.verifyTankOnRow(playerTurn) == 1
                && attackedMinion.verifyTank(attackedMinion) == 0) {
            addCardActionError(actionNode, action, output,
                    "Attacked card is not of type 'Tank'.");
            return;
        }

        if (attackerMinion.getHasAttacked() == 1) {
            addCardActionError(actionNode, action, output,
                    "Attacker card has already attacked this turn.");
            return;
        }
        attackerMinion.setHasAttacked(1);
        int damage = table.getTable().get(attackerX).get(attackerY).getCard().getAttackDamage();
        attackedMinion.decHealth(damage);
        if (attackedMinion.getCard().getHealth() <= 0) {
            table.getTable().get(attackedX).remove(attackedY);
        }
    }

    /**
     * Retrieves the card at a specified position on the table
     *
     * @param action     the action containing the position details (X, Y)
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param table      the game table containing the cards
     */
    public void getCardAtPosition(final ActionsInput action, final ObjectNode actionNode,
                                  final ArrayNode output, final Table table) {
        actionNode.put("command", action.getCommand());
        actionNode.put("x", action.getX());
        actionNode.put("y", action.getY());

        if (table.getTable().get(action.getX()).size() <= action.getY()) {
            actionNode.put("output", "No card available at that position.");
        } else {
            Minions card = table.getTable().get(action.getX()).get(action.getY());
            ObjectNode cardNode = createCardNode(actionNode, card.getCard(), false);
            actionNode.set("output", cardNode);
        }

        output.add(actionNode);
    }

    /**
     * Executes a card's ability on a target card on the table,
     * checks rules, applies ability, and marks attacker as having attacked
     *
     * @param action     the action containing the ability details
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param playerTurn the index of the player whose turn it is (0 or 1)
     * @param table      the game table containing the cards
     */
    public void cardUsesAbility(final ActionsInput action, final ObjectNode actionNode,
                                final ArrayNode output, final int playerTurn, final Table table) {
        actionNode.put("command", action.getCommand());

        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();
        int attackedX = action.getCardAttacked().getX();
        int attackedY = action.getCardAttacked().getY();

        Minions attackerMinion = table.getTable().get(attackerX).get(attackerY);
        Minions attackedMinion = table.getTable().get(attackedX).get(attackedY);


        if (attackerMinion.getIsFrozen() == 1) {
            actionNode.put("error", "Attacker card is frozen.");
            output.add(actionNode);
            return;
        }

        if (attackerMinion.getHasAttacked() == 1) {
            addCardActionError(actionNode, action, output,
                    "Attacker card has already attacked this turn.");
            return;
        }

        if (attackerMinion.getCard().getName().equals("Disciple")) {
            if (playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW2
                    || action.getCardAttacked().getX() == BACK_ROW2)) {
                addCardActionError(actionNode, action, output,
                        "Attacked card does not belong to the current player.");
                return;
            } else if (playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW1
                    || action.getCardAttacked().getX() == BACK_ROW1)) {
                addCardActionError(actionNode, action, output,
                        "Attacked card does not belong to the current player.");
                return;
            }
        }


        if (playerTurn == 0 && (action.getCardAttacked().getX() == FRONT_ROW1
                || action.getCardAttacked().getX() == BACK_ROW1)
                && !attackerMinion.getCard().getName().equals("Disciple")) {
            addCardActionError(actionNode, action, output,
                    "Attacked card does not belong to enemy.");
            return;
        }

        if (playerTurn == 1 && (action.getCardAttacked().getX() == FRONT_ROW2
                || action.getCardAttacked().getX() == BACK_ROW2)
                && !attackerMinion.getCard().getName().equals("Disciple")) {
            addCardActionError(actionNode, action, output,
                    "Attacked card does not belong to the enemy.");
            return;
        }


        if (!attackerMinion.getCard().getName().equals("Disciple")) {
            if (table.verifyTankOnRow(playerTurn) == 1 && attackedMinion
                    .verifyTank(attackedMinion) == 0) {
                addCardActionError(actionNode, action, output,
                        "Attacked card is not of type 'Tank'.");
                return;
            }
        }

        Minions attackerWithAbility = SpecialMinionsFactory.createMinion(attackerMinion);
        attackerWithAbility.ability(attackedMinion);

        attackerMinion.setHasAttacked(1);

        if (attackedMinion.getCard().getHealth() <= 0) {
            table.getTable().get(attackedX).remove(attackedY);
        }

    }

    /**
     * Attacks the opponent's hero with a minion card, checks rules,
     * applies damage, and marks attacker as having attacked
     * If the hero's health reaches 0, the game ends
     *
     * @param action     the action containing the attacker details
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param p1         player 1
     * @param p2         player 2
     * @param playerTurn the index of the player whose turn it is (0 or 1)
     * @param table      the game table containing the cards
     */
    public void useAttackHero(final ActionsInput action, final ObjectNode actionNode,
                              final ArrayNode output, final Player p1, final Player p2,
                              final int playerTurn, final Table table) {
        actionNode.put("command", action.getCommand());
        ObjectNode cardAttackerNode = actionNode.objectNode();
        ObjectNode gameEndNode = actionNode.objectNode();
        int attackerX = action.getCardAttacker().getX();
        int attackerY = action.getCardAttacker().getY();

        cardAttackerNode.put("x", attackerX);
        cardAttackerNode.put("y", attackerY);
        actionNode.set("cardAttacker", cardAttackerNode);

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

    /**
     * Executes the hero's ability on a specific row of the table,
     * after checking player's turn and other rules
     *
     * @param action     the action containing the ability details
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param p1         player 1
     * @param p2         player 2
     * @param playerTurn the index of the player whose turn it is (0 or 1)
     * @param table      the game table containing the rows of cards
     */
    public void useHeroAbility(final ActionsInput action, final ObjectNode actionNode,
                               final ArrayNode output, final Player p1, final Player p2,
                               final int playerTurn, final Table table) {
        actionNode.put("command", action.getCommand());
        actionNode.put("affectedRow", action.getAffectedRow());

        int affectedRow  = action.getAffectedRow();

        if (playerTurn == 0) {
            processHeroAbility(p1.getHero(), p1, actionNode, output,
                    table, playerTurn, affectedRow);
        } else {
            processHeroAbility(p2.getHero(), p2, actionNode, output,
                    table, playerTurn, affectedRow);
        }
    }

    /**
     * Processes the hero's ability based on their current state,
     * the game table, and the affected row.
     *
     * @param hero         the hero whose ability is being used
     * @param player       the player who controls the hero
     * @param actionNode   the JSON node where action results and errors are stored
     * @param output       the JSON array where the actionNode is added if there's an error
     * @param table        the game table containing all rows and their statuses
     * @param playerTurn   indicator of the current player's turn
     *                     (0 for player one, 1 for player two)
     * @param affectedRow  the row that is targeted by the hero's ability
     */
    public void processHeroAbility(final Hero hero, final Player player,
                                   final ObjectNode actionNode, final ArrayNode output,
                                   final Table table, final int playerTurn,
                                   final int affectedRow) {

        // Check if the player has enough mana
        if (player.getMana() < hero.getCard().getMana()) {
            actionNode.put("error", "Not enough mana to use hero's ability.");
            output.add(actionNode);
            return;
        }

        // Check if the hero has already attacked
        if (hero.getHasAttacked() == 1) {
            actionNode.put("error", "Hero has already attacked this turn.");
            output.add(actionNode);
            return;
        }

        // Determine rows based on playerTurn for offensive and defensive abilities
        int enemyFrontRow, enemyBackRow, ownFrontRow, ownBackRow;

        if (playerTurn == 0) {
            enemyFrontRow = FRONT_ROW2;
            enemyBackRow = BACK_ROW2;
            ownFrontRow = FRONT_ROW1;
            ownBackRow = BACK_ROW1;
        } else {
            enemyFrontRow = FRONT_ROW1;
            enemyBackRow = BACK_ROW1;
            ownFrontRow = FRONT_ROW2;
            ownBackRow = BACK_ROW2;
        }


        // if offensive, the affected row must be opponent's row
        if (hero.verifyOffensive() == 1 && (affectedRow != enemyFrontRow
                && affectedRow != enemyBackRow)) {
            actionNode.put("error", "Selected row does not belong to the enemy.");
            output.add(actionNode);
            return;
        }

        // if defensive, the affected row must be player's row
        if (hero.verifyDefensive() == 1 && (affectedRow != ownFrontRow
                && affectedRow != ownBackRow)) {
            actionNode.put("error", "Selected row does not belong to the current player.");
            output.add(actionNode);
            return;
        }

        // Execute hero ability
        Hero heroAbility = HeroFactory.createHero(hero);
        heroAbility.ability(table.getTable().get(affectedRow));
        hero.setHasAttacked(1);
        player.decMana(hero.getCard().getMana());
    }



    /**
     * Gets the cards on the table that are frozen
     *
     * @param action     the action containing the command details
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param table      the game table containing the cards
     */
    public void getFrozenCardsOnTable(final ActionsInput action, final ObjectNode actionNode,
                                      final ArrayNode output, final Table table) {
        actionNode.put("command", action.getCommand());
        ArrayNode frozenArray = actionNode.putArray("output");
        for (LinkedList<Minions> row : table.getTable()) {
            for (Minions minion : row) {
                if (minion.getIsFrozen() == 1) {
                    ObjectNode frozenNode = createCardNode(actionNode,
                            minion.getCard(), false);
                    frozenArray.add(frozenNode);
                }
            }
        }
        output.add(actionNode);
    }

    /**
     * Gets the total number of games played or player wins based on the command
     *
     * @param action     the action containing the command details
     * @param actionNode the JSON node to store the result of the command
     * @param output     the overall JSON array to store all command results
     * @param i          the total number of games played so far
     */
    public static void getTotalGamesPlayedOrPlayerWins(final ActionsInput action,
                                                       final ObjectNode actionNode,
                                                       final ArrayNode output, final int i) {
        actionNode.put("command", action.getCommand());
        actionNode.put("output", i);
        output.add(actionNode);
    }


    /**
     * Creates a JSON representation of a card
     *
     * @param actionNode the JSON node to store the card details
     * @param card       the card to be represented as JSON
     * @param isHeroCard indicates whether the card is a hero card
     * @return the JSON node representing the card
     */
    private ObjectNode createCardNode(final ObjectNode actionNode, final CardInput card,
                                      final boolean isHeroCard) {
        ObjectNode cardNode = actionNode.objectNode();
        cardNode.put("mana", card.getMana());
        cardNode.put("health", card.getHealth());
        cardNode.put("description", card.getDescription());
        if (!isHeroCard) {
            cardNode.put("attackDamage", card.getAttackDamage());
        }
        ArrayNode colorsArray = cardNode.putArray("colors");
        for (String color : card.getColors()) {
            colorsArray.add(color);
        }
        cardNode.put("name", card.getName());
        return cardNode;
    }

    /**
     * Adds an error message to the output for a failed card action
     *
     * @param actionNode   the JSON node to store the error details
     * @param action       the action causing the error
     * @param output       the overall JSON array to store all command results
     * @param errorMessage the error message to be added
     */
    private void addCardActionError(final ObjectNode actionNode, final ActionsInput action,
                                    final ArrayNode output, final String errorMessage) {
        ObjectNode cardAttackerNode = actionNode.objectNode();
        cardAttackerNode.put("x", action.getCardAttacker().getX());
        cardAttackerNode.put("y", action.getCardAttacker().getY());
        actionNode.set("cardAttacker", cardAttackerNode);

        ObjectNode cardAttackedNode = actionNode.objectNode();
        cardAttackedNode.put("x", action.getCardAttacked().getX());
        cardAttackedNode.put("y", action.getCardAttacked().getY());
        actionNode.set("cardAttacked", cardAttackedNode);

        actionNode.put("command", action.getCommand());
        actionNode.put("error", errorMessage);
        output.add(actionNode);
    }
}
