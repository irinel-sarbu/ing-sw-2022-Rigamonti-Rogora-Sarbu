package util;

public enum TowerColor {
    WHITE(0, "White"),
    BLACK(1, "Black"),
    GRAY(2, "Gray");

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
