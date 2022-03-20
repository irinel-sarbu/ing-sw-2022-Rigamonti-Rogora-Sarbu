package model.expert;

import util.*;

public abstract class Character {
    private int cost;
    private CharacterName character;
    private boolean effectIsUsed;

    public Character(int cost, CharacterName character) {
        this.cost = cost;
        this.character = character;
        resetEffect();
    }

    public abstract void useEffect();

    public void effectUsed(){
        this.effectIsUsed = true;
    }

    public void resetEffect() {
        this.effectIsUsed = false;
    }
}
