package gameutils.cardsinfo;

import fileio.CardInput;

public class Minions extends Cards {
        private int frozenCnt;
        private int attackedCnt;
        private int health;


//        public Minions(final CardInput cardInput) {
//                super(cardInput);
//                frozenCnt = 0;
//                attackedCnt = 0;
//        }

        public Minions(CardInput minions) {
                super(minions.getName(), minions.getMana(),minions.getDescription(), minions.getColors());
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
