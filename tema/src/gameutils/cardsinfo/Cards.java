package gameutils.cardsinfo;

import fileio.CardInput;

import java.util.ArrayList;

public class Cards {

    private String name;
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;

    public Cards(final CardInput cardInput) {
        //constructor for card attribute input
        this.name = cardInput.getName();
        this.mana = cardInput.getMana();
        this.attackDamage = cardInput.getAttackDamage();
        this.health = cardInput.getHealth();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
    }

    public Cards(final Cards cards) {
        // copy constructor
        this.name = cards.getName();
        this.mana = cards.getMana();
        this.attackDamage= cards.getAttackDamage();
        this.health = cards.getHealth();
        this.description = cards.getDescription();
        this.colors = cards.getColors();
    }

    public Cards(final String name, final int mana, final int attackDamage, final int health,final String description, final ArrayList<String> colors) {
        // constructor for card attributes
        this.name = name;
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
    }

    public Cards() {
        // default constructor
        this.name = "";
        this.mana = 0;
        this.attackDamage=0;
        this.health = 0;
        this.description = "";
        this.colors= new ArrayList<>();
    }

    // getters for cards
    public String getName() {
        return name;
    }

    public int getMana() {
        return mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    // setter for cards


    public void setName(String name) {
        this.name = name;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }
}
