package model.board;

import util.Color;

public class Student {
    private final Color color;

    public Student(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return color.toString();
    }
}
