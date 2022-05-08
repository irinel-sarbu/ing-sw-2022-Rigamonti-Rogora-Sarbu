package util;

public enum TowerColor {
    GRAY("Gray"),
    BLACK("Black"),
    WHITE("White");

    private final String string;

    TowerColor(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
