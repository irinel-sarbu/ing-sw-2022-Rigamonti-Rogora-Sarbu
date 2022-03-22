package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum CharacterType {
    HERALD(3, "Herald"),
    KNIGHT(2, "Knight"),
    CENTAUR(3, "Centaur"),
    MUSHROOM_FANATIC(3, "Mushroom fanatic"),
    JESTER(1, "Jester"),
    THIEF(3, "Thief"),
    MINSTREL(1, "Minstrel"),
    MONK(1, "Monk"),
    GRANNY_HERBS(2, "Granny herbs"),
    POSTMAN(1, "Postman"),
    PRINCESS(2, "Princess"),
    FARMER(2, "Farmer");

    private final int baseCost;
    private final String name;
    private final List<CharacterType> extractedCharacters;

    CharacterType(int baseCost, String name) {
        this.baseCost = baseCost;
        this.name = name;
        this.extractedCharacters = new ArrayList<>();
    }

    public int getBaseCost() {
        return baseCost;
    }

    public String getName() {
        return name;
    }

    public CharacterType getRandomCharacter() {
        CharacterType extracted = null;
        Random random = new Random();

        while(!extractedCharacters.contains(extracted))
            extracted = values()[random.nextInt(values().length)];

        extractedCharacters.add(extracted);

        return extracted;
    }

    @Override
    public String toString() {
        return name;
    }
}