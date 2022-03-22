package util;

public enum Color {
    YELLOW(0, "Y"),
    BLUE(1, "B"),
    GREEN(2, "G"),
    RED(3, "R"),
    PINK(4, "P");

    private final int value;
    private final String string;

    Color(int value, String string) {
        this.value = value;
        this.string = string;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return string;
    }
}
