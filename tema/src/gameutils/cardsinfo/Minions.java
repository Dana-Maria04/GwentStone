package gameutils.cardsinfo;

/**
 * Represents a minion card in the game, which extends the functionality of a basic card
 */
public class Minions extends Cards {

        private final  int health;
        private int hasAttacked = 0;
        private int isFrozen = 0;

        /**
         * Constructs a new Minion based on an existing card
         *
         * @param card the base card from which to create the minion
         */
        public Minions(final Cards card) {
                super(card.getCardInput());
                this.health = card.getCardInput().getHealth();
                this.hasAttacked = 0;
                this.isFrozen = 0;
        }

        /**
         * Copy Constructs a new Minion
         *
         * @param minion the minion to copy.
         */
        public Minions(final Minions minion) {
                super(minion.getCard());
                this.health = minion.getCard().getHealth();
                this.hasAttacked = minion.getHasAttacked();
        }

        /**
         * Verifies if the minion is of type 'Tank'
         *
         * @param minion the minion to verify
         * @return 1 if the minion is a tank, 0 otherwise
         */
        public int verifyTank(final Minions minion) {
                if (minion.getCard().getName().equals("Warden")
                        || minion.getCard().getName().equals("Goliath")) {
                        return 1;
                }
                return 0;
        }

        /**
         * Increases the attack damage of the specified minion
         * by the specified amount
         *
         * @param minion the minion whose attack damage will be increased
         * @param damage the amount of damage to increase the minion's attack
         */
        public void incAttackDamage(final Minions minion, final int damage) {
                minion.getCard().setAttackDamage(minion.getCard().getAttackDamage() + damage);
        }

        /**
         * Applies the minion's special ability to the specified target. This method is intended
         * to be overridden by specific minion subclasses with unique abilities
         *
         * @param target the target minion on which the ability is applied
         */
        public void ability(final Minions target) {
        }

        /**
         * Gets if the minion has attacked or not this turn
         *
         * @return 1 if the minion has attacked this turn, 0 otherwise
         */
        public int getHasAttacked() {
                return hasAttacked;
        }

        /**
         * Sets the attack status of the minion
         *
         * @param hasAttacked 1 if the minion has attacked this turn, 0 otherwise
         */
        public void setHasAttacked(final int hasAttacked) {
                this.hasAttacked = hasAttacked;
        }

        /**
         * Gets if the minion is frozen or not
         *
         * @return 1 if the minion is frozen, 0 otherwise
         */
        public int getIsFrozen() {
                return isFrozen;
        }

        /**
         * Sets the frozen status of the minion
         *
         * @param isFrozen 1 if the minion is frozen, 0 otherwise
         */
        public void setIsFrozen(final int isFrozen) {
                this.isFrozen = isFrozen;
        }
}
