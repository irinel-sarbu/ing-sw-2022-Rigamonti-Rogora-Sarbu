package model.expert.characterCards;

import exceptions.EmptyNoEntryListException;
import model.expert.Character;
import model.expert.NoEntryTile;
import util.CharacterName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrannyHerbs extends Character {

    List<NoEntryTile> noEntryTileList;

    public GrannyHerbs() {
        super(2, CharacterName.GRANNY_HERBS);
        this.noEntryTileList = new ArrayList<>();
        this.noEntryTileList = new ArrayList<>(Collections.nCopies(4, new NoEntryTile()));
    }

    public NoEntryTile removeNoEntry() throws EmptyNoEntryListException {
        if (this.noEntryTileList.size() == 0) throw new EmptyNoEntryListException();
        return this.noEntryTileList.remove(0);
    }

    public int getRemainingNoEntry() {
        return noEntryTileList.size();
    }

    public void addNoEntry() {
        this.noEntryTileList.add(new NoEntryTile());
    }
}
