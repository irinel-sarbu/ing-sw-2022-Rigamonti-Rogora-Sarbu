package model.expert.characterCards;

import model.expert.Character;

import static util.CharacterName.*;

public class Centaur extends Character {
    public Centaur(){
        super(3, CENTAUR);
    }

    @Override
    public void useEffect() {
        effectUsed();
    }
}
