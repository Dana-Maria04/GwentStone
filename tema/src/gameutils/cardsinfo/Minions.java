package gameutils.cardsinfo;

import fileio.CardInput;

public class Minions extends Cards {
        private int frozenCnt;
        private int attackedCnt;
        private int health;
        private int isTank = 0;
        private int hasAttacked = 0;

        public Minions(Cards card) {
                super(card.getCardInput());
                this.health = card.getCardInput().getHealth();
                this.frozenCnt = 0;
                this.attackedCnt = 0;
        }

        public int getHasAttacked() {
                return hasAttacked;
        }

        public void setHasAttacked(int hasAttacked) {
                this.hasAttacked = hasAttacked;
        }

        public int getIsTank() {
                return isTank;
        }

        public void setIsTank(int isTank) {
                this.isTank = isTank;
        }

        public int getFrozenCnt() {
                return frozenCnt;
        }

        public void setFrozenCnt(final int frozenCnt) {
                this.frozenCnt = frozenCnt;
        }

        public int getAttackedCnt() {
                return attackedCnt;
        }

        public void setAttackedCnt(final int attackedCnt) {
                this.attackedCnt = attackedCnt;
        }
}