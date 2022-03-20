package util;

public enum CharacterName {
    HERALD(0, "Herald"),
    KNIGHT(1, "Knight"),
    CENTAUR(2, "Centaur"),
    MUSHROOM_FANATIC(3, "Mushroom fanatic"),
    JESTER(4, "Jester"),
    THIEF(5, "Thief"),
    MINSTREL(6, "Minstrel"),
    MONK(7, "Monk"),
    GRANNY_HERBS(8, "Granny herbs"),
    POSTMAN(9, "Postman"),
    PRINCESS(10, "Princess"),
    FARMER(11, "Farmer");

    private int value;
    private String name;

    CharacterName(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.name;
    }
}