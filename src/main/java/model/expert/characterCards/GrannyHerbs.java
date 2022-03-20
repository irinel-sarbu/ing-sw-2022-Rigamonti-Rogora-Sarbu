package model.expert.characterCards;

import model.expert.Character;
import model.expert.NoEntryTile;
import util.CharacterName;

import java.util.ArrayList;
import java.util.List;

public class GrannyHerbs extends Character {

    List<NoEntryTile> noEntryTileList;

    public GrannyHerbs() {
        super(2, CharacterName.GRANNY_HERBS);
        noEntryTileList = new ArrayList<>();
    }
}
