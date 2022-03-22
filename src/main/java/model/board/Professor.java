package model.board;

import util.Color;

public class Professor {
    private final Color color;

    public Professor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color.toString() + " Prof.";
    }
}
