package model.player;

import util.Wizard;

public class Assistant {
    private int value;
    private int movements;
    private String name;
    private Wizard wizard;

    public Assistant(int value, int movements, String name, Wizard wizard) {
        this.value = value;
        this.movements = movements;
        this.name = name;
        this.wizard = wizard;
    }

    public int getValue() {
        return value;
    }

    public int getMovements() {
        return movements;
    }

    public String getName() {
        return name;
    }
}