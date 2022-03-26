package model.board;

import util.Color;

public class Student {
    private static int count;
    private final int ID;
    private final Color color;

    public Student(int ID, Color color) {
        this.ID = ID;
        this.color = color;
    }

    public Student(Color color) {
        this(count, color);
        count++;
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
