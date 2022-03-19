package util;

public enum Color {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4);

    private int value;

    private Color(int num) {
        this.value = num;
    }

    public int getValue() {
        return value;
    }
}
