package util;

public enum Wizard {
    WIZARD_1(1, "WIZARD 1"),
    WIZARD_2(2, "WIZARD 2"),
    WIZARD_3(3, "WIZARD 3"),
    WIZARD_4(4, "WIZARD 4");

    private final int value;
    private final String string;

    Wizard(int value, String string) {
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
