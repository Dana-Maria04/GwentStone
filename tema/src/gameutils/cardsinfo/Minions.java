package gameutils.cardsinfo;

import fileio.CardInput;
import fileio.StartGameInput;
import gameutils.CommandHandler;
import gameutils.StartGame;
import gameutils.Table;

import java.util.ArrayList;
import java.util.LinkedList;

public class Minions extends Cards {
        private int health;
//        private int isTank = 0;
        private int hasAttacked = 0;

        public Minions(Cards card) {
                super(card.getCardInput());
                this.health = card.getCardInput().getHealth();
                this.hasAttacked = 0;
        }

        public int verifyTank(Minions minion) {
                if (minion.getCard().getName().equals("Warden") || minion.getCard().getName().equals("Goliath")) {
                  return 1;
                }
                return 0;
        }



        public int hasAttacked() {
                return hasAttacked;
        }

        public void decHealth(int health) {
                this.health -= health;
        }
        public int getHasAttacked() {
                return hasAttacked;
        }

        public void setHasAttacked(int hasAttacked) {
                this.hasAttacked = hasAttacked;
        }

}