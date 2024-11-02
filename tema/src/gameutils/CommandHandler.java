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

    public void placeCard(ActionsInput action, ObjectNode actionNode, ArrayNode output, Player p1, Player p2, Hand[] hand, int playerTurn, Table table) {

        String cardName1 = p1.getDeck().get(action.getHandIdx()).getName();
        String cardName2 = p2.getDeck().get(action.getHandIdx()).getName();


        String[] specialMinions ={ "The Ripper ", "Miraj" , "The Cursed One", "Disciple"};

        if(playerTurn - 1 == 0) {
            if(p1.getMana() < p1.getDeck().get(action.getHandIdx()).getMana()) {
                actionNode.put("command", action.getCommand());
                actionNode.put("handIdx", action.getHandIdx());
                actionNode.put("error", "Not enough mana to place card on table.");
                output.add(actionNode);
                return;
            }

            for ( int i = 0 ; i < specialMinions.length; i++) {
                if (cardName1.equals(specialMinions[i])) {
                    if (table.getTable().get(3).size() < NUM_CARDS) {
                        // add back row for player 1
                        table.getTable().get(3).add(p1.getDeck().get(action.getHandIdx()));
                        hand[playerTurn].removeCard(p1.getDeck().get(action.getHandIdx()));
                        p1.decMana(p1.getDeck().get(action.getHandIdx()).getMana());
                        return;
                    } else if (table.getTable().get(2).size() < NUM_CARDS) {
                        // add front row for player 1
                        table.getTable().get(2).add(p1.getDeck().get(action.getHandIdx()));
                        hand[playerTurn].removeCard(p1.getDeck().get(action.getHandIdx()));
                        p1.decMana(p1.getDeck().get(action.getHandIdx()).getMana());
                        return;
                    } else {
                        actionNode.put("command", action.getCommand());
                        actionNode.put("handIdx", action.getHandIdx());
                        actionNode.put("error", "Table is full.");
                        output.add(actionNode);
                        return;
                    }
                }
            }

            if(cardName1.equals("Berserker") || cardName1.equals("Sentinel")) {
                if( table.getTable().get(3).size() < NUM_CARDS) {
                    // add back row for player 1
                    table.getTable().get(3).add(p1.getDeck().get(action.getHandIdx()));
                    hand[playerTurn].removeCard(p1.getDeck().get(action.getHandIdx()));
                    p1.decMana(p1.getDeck().get(action.getHandIdx()).getMana());
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", action.getHandIdx());
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                }


            }

            if(cardName1.equals("Goliath") || cardName1.equals("Warden")) {
                if( table.getTable().get(2).size() < NUM_CARDS) {
                   //add front row for player 1
                    table.getTable().get(2).add(p1.getDeck().get(action.getHandIdx()));
                    hand[playerTurn].removeCard(p1.getDeck().get(action.getHandIdx()));
                    p1.decMana(p1.getDeck().get(action.getHandIdx()).getMana());
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", action.getHandIdx());
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                }
            }
        } else {
            if(p2.getMana() < p2.getDeck().get(action.getHandIdx()).getMana()) {
                actionNode.put("command", action.getCommand());
                actionNode.put("handIdx", action.getHandIdx());
                actionNode.put("error", "Not enough mana to place card on table.");
                output.add(actionNode);
                return;
            }

            for ( int i = 0 ; i < specialMinions.length; i++) {
                if (cardName1.equals(specialMinions[i])) {
                    //check if there is space on either rows
                    if (table.getTable().get(0).size() < NUM_CARDS) {
                        // add back row for player 1
                        table.getTable().get(0).add(p2.getDeck().get(action.getHandIdx()));
                        hand[playerTurn].removeCard(p2.getDeck().get(action.getHandIdx()));
                        p2.decMana(p2.getDeck().get(action.getHandIdx()).getMana());
                        return;
                    } else if (table.getTable().get(1).size() < NUM_CARDS) {
                        // add front row for player 1
                        table.getTable().get(1).add(p2.getDeck().get(action.getHandIdx()));
                        hand[playerTurn].removeCard(p2.getDeck().get(action.getHandIdx()));
                        p2.decMana(p2.getDeck().get(action.getHandIdx()).getMana());
                        return;
                    } else {
                        actionNode.put("command", action.getCommand());
                        actionNode.put("handIdx", action.getHandIdx());
                        actionNode.put("error", "Table is full.");
                        output.add(actionNode);
                        return;
                    }
                }
            }

            if(cardName2.equals("Berserker") || cardName2.equals("Sentinel")) {
                //add back row for player 2
                if( table.getTable().get(0).size() < NUM_CARDS) {
                    table.getTable().get(0).add(p2.getDeck().get(action.getHandIdx()));
                    hand[playerTurn].removeCard(p2.getDeck().get(action.getHandIdx()));
                    p2.decMana(p2.getDeck().get(action.getHandIdx()).getMana());
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", action.getHandIdx());
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                }
            }

            if(cardName2.equals("Goliath") || cardName2.equals("Warden")) {
                //add front row for player 2
                if( table.getTable().get(1).size() < NUM_CARDS) {
                    table.getTable().get(1).add(p2.getDeck().get(action.getHandIdx()));
                    hand[playerTurn].removeCard(p2.getDeck().get(action.getHandIdx()));
                    p2.decMana(p2.getDeck().get(action.getHandIdx()).getMana());
                    return;
                } else {
                    actionNode.put("command", action.getCommand());
                    actionNode.put("handIdx", action.getHandIdx());
                    actionNode.put("error", "Table is full.");
                    output.add(actionNode);
                    return;
                    }
                }
            }
        }
    }