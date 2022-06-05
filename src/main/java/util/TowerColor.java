package util;

public enum TowerColor {
    GRAY("Gray"),
    BLACK("Black"),
    WHITE("White");

    private final String string;

    /**
     * Constructor of TowerColor
     */
    TowerColor(String string) {
        this.string = string;
    }

    /**
     * Overrides toString
     */
    @Override
    public String toString() {
        return string;
    }
}
