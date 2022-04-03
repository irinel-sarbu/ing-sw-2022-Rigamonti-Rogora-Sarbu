package model.board;

import util.Color;

public class Student {
    private final int ID;
    private final Color color;

    public Student(int ID, Color color) {
        this.ID = ID;
        this.color = color;
    }

    public int getID() {
        return ID;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return color.toString();
    }
}
