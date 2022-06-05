package util;

/**
 * enum of CharacterType
 */
public enum CharacterType {
    HERALD(3, "Herald", 0),
    KNIGHT(2, "Knight", 1),
    CENTAUR(3, "Centaur", 2),
    MUSHROOM_FANATIC(3, "Mushroom fanatic", 3),
    JESTER(1, "Jester", 4),
    THIEF(3, "Thief", 5),
    MINSTREL(1, "Minstrel", 6),
    MONK(1, "Monk", 7),
    GRANNY_HERBS(2, "Granny herbs", 8),
    POSTMAN(1, "Postman", 9),
    PRINCESS(2, "Princess", 10),
    FARMER(2, "Farmer", 11);

    private final int baseCost;
    private final String name;
    private final int number;

    /**
     * constructor of CharacterType
     */
    CharacterType(int baseCost, String name, int number) {
        this.baseCost = baseCost;
        this.name = name;
        this.number = number;
    }

    /**
     * Getter for the BASE cost attribute
     */
    public int getBaseCost() {
        return baseCost;
    }

    /**
     * Getter for the name attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the number attribute
     */
    public int getNumber() {
        return number;
    }

    /**
     * Overrides toString
     */
    @Override
    public String toString() {
        return name;
    }
}