package model.expert;

import util.*;

public class Character {
    private int cost;
    private CharacterName character;
    private boolean effectIsUsed;

    public Character(int cost, CharacterName character) {
        this.cost = cost;
        this.character = character;
        resetEffect();
    }

    public void useEffect() {
        this.effectIsUsed = true;
    }

    public void resetEffect() {
        this.effectIsUsed = false;
    }

    public boolean getEffect() {
        return effectIsUsed;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
