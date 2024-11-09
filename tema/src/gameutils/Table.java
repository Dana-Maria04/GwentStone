package gameutils;

import fileio.CardInput;
import gameutils.cardsinfo.Cards;
import gameutils.cardsinfo.Minions;
import gameutils.cardsinfo.heroes.Hero;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import static gameutils.GameConstants.*;

public class Table {

    private ArrayList<LinkedList<Minions>> table;

    public Table(){
        //intiliaze table
        table = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            table.add(new LinkedList<>());
        }
    }


    public int verifyTankOnRow(int playerTurn) {
        LinkedList<Minions> frontRowMinions;
        if(playerTurn == 0) {
            frontRowMinions = table.get(1);
        } else{
            frontRowMinions = table.get(2);
        }
        for(Minions minion : frontRowMinions) {
            if(minion.verifyTank(minion) == 1) {
                return 1;
            }
        }
        return 0;
    }


    public ArrayList<LinkedList<Minions>> getTable() {
        return table;
    }

    public void setTable(ArrayList<LinkedList<Minions>> table) {
        this.table = table;
    }

}
