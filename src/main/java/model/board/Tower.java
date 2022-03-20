package model.board;

import util.TowerColor;

public class Tower {
    private final TowerColor color;

    public Tower(TowerColor color) {
        this.color = color;
    }

    public TowerColor getColor() {
        return color;
    }
}
