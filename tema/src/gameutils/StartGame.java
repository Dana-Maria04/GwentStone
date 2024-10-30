package gameutils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Input;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.heroes.Hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class StartGame {

    protected Player[] player;
    protected ArrayList<ActionsInput> actionsinputs = new ArrayList<>();
    protected ObjectMapper mapper = new ObjectMapper();


    public ArrayNode runGame(Input input) {

        ArrayNode output = mapper.createArrayNode();
        // todo initialise the players , decks , cards , and heroes


        // todo handle commands
        actionsinputs = input.getGames().get(0).getActions();

        for(ActionsInput action : actionsinputs){
            ObjectNode actionNode = mapper.createObjectNode();

            switch(action.getCommand()){
                case "getPlayerDeck":
                    // todo getPlayerDeck
                    break;
                case "getPlayeHero":
                    // todo getplayerhero
                    break;
                case "getPlayerTurn":
                    // todo gateplayerturn
                    break;
                default:
                    break;
            }
        }

    }





}



