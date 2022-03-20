package util;

public enum CharacterName {
    HERALD(0),
    KNIGHT(1),
    CENTAUR(2),
    MUSHROOM_FANATIC(3),
    JESTER(4),
    THIEF(5),
    MINSTREL(6),
    MONK(7),
    GRANNY_HERBS(8),
    POSTMAN(9),
    PRINCESS(10),
    FARMER(11);

    private int value;

    CharacterName(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}