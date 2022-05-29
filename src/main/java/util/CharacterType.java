package util;

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


    CharacterType(int baseCost, String name, int number) {
        this.baseCost = baseCost;
        this.name = name;
        this.number = number;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name;
    }
}