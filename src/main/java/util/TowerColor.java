package util;

public enum TowerColor {
    GRAY(0, "Gray"),
    BLACK(1, "Black"),
    WHITE(2, "White");

    private final int ID;
    private final String string;

    TowerColor(int ID, String string) {
        this.ID = ID;
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
