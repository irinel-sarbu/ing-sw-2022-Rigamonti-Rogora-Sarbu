package util;

public enum Color {
    YELLOW(0, "Y"),
    BLUE(1, "B"),
    GREEN(2, "G"),
    RED(3, "R"),
    PINK(4, "P");

    private final int value;
    private final String string;

    /**
     * constructor of Color
     */
    Color(int value, String string) {
        this.value = value;
        this.string = string;
    }

    /**
     * getter for the value attribute
     */
    public int getValue() {
        return value;
    }

    /**
     * overrides toSting
     */
    @Override
    public String toString() {
        return string;
    }
}
