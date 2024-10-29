package gameutils.cardsinfo;

import fileio.CardInput;

public class Minions extends Cards {
        private int frozenCnt;
        private int attackedCnt;

        public Minions(final CardInput cardInput) {
                super(cardInput);
                frozenCnt = 0;
                attackedCnt = 0;
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
