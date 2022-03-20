package model.expert;

import util.*;

public abstract class Character {               // TODO: observer per characterCards effects
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
}
