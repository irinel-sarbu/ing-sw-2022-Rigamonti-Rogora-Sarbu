package model.expert;

import util.*;

public class Character {
    private int cost;
    private CharacterType character;
    private boolean effectIsUsed;

    public Character(CharacterType character){
        this.cost= character.getBaseCost();
        this.character=character;
        resetEffect();
    }

    public Character(int cost, CharacterType character) {
        this.cost = cost;
        this.character = character;
        resetEffect();
    }

    public CharacterType getCharacter() {
        return character;
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
