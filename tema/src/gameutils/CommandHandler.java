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

import java.util.ArrayList;

public class CommandHandler {
    private final StartGame game;
    private final GameStats gameStats;
    private final ArrayNode output;
    private final Table table;
    private final Deck gameDeck;
    private final Cards gameCards;


    public CommandHandler(final StartGame game, final GameStats gameStats, final ArrayNode output, final Table table, final Deck gameDeck, final Cards gameCards) {
        this.game = game;
        this.gameStats = gameStats;
        this.output = output;
        this.table = table;
        this.gameDeck = gameDeck;
        this.gameCards = gameCards;
    }

    private int playerTurn;

    public void actionsHandler(final ActionsInput actionsInput, final Input input) {
        for (int i = 0; i < input.getGames().size(); i++) {
            playerTurn = input.getGames().get(i).getStartGame().getStartingPlayer();
        }
        switch (actionsInput.getCommand()) {
            case "getPlayerDeck" -> {
                ObjectNode newNode = output.addObject();
                int whichPlayer = actionsInput.getPlayerIdx();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("playerIdx", actionsInput.getPlayerIdx());
                ArrayNode outputArray = newNode.putArray("output");
                ArrayList<Cards> deck;
                if (whichPlayer == 1) {
                    deck = gameDeck.getDeckP1();
                } else {
                    deck = gameDeck.getDeckP2();
                }
                for (int i = 0; i < deck.size(); i++) {
                    CardInput tempCard = deck.get(i).getCard();
                    ObjectNode cardNode = outputArray.addObject();
                    cardNode.put("mana", tempCard.getMana());
                    cardNode.put("attackDamage", tempCard.getAttackDamage());
                    cardNode.put("health", tempCard.getHealth());
                    cardNode.put("description", tempCard.getDescription());
                    ArrayNode colorsArray = cardNode.putArray("colors");
                    for (int j = 0; j < tempCard.getColors().size(); j++) {
                        colorsArray.add(tempCard.getColors().get(j));
                    }
                    cardNode.put("name", tempCard.getName());
                }
                break;
            }
            case "getPlayerHero" -> {
                ObjectNode newNode = output.addObject();
                int whichPlayer = actionsInput.getPlayerIdx();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("playerIdx", actionsInput.getPlayerIdx());
                ObjectNode heroNode = newNode.putObject("output");
                CardInput hero;
                if (whichPlayer == 1) {
                    hero = table.getHeroP1().getCard();
                } else {
                    hero = table.getHeroP2().getCard();
                }
                heroNode.put("mana", hero.getMana());
                heroNode.put("description", hero.getDescription());
                ArrayNode colorsArray = heroNode.putArray("colors");
                for (int j = 0; j < hero.getColors().size(); j++) {
                    colorsArray.add(hero.getColors().get(j));
                }
                heroNode.put("name", hero.getName());
                Hero tempHero;
                if (whichPlayer == 1) {
                    tempHero = gameCards.getHeroP1();
                } else {
                    tempHero = gameCards.getHeroP2();
                }
                heroNode.put("health", tempHero.getHealth());
                break;
            }
            case "getPlayerTurn" -> {
                ObjectNode newNode = output.addObject();
                newNode.put("command", actionsInput.getCommand());
                newNode.put("output", playerTurn);
                break;
            }
        }
    }
}