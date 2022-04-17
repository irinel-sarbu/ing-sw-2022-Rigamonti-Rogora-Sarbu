package events.types.clientToServer;

import util.CharacterType;

public class EUseGrannyEffect extends EUseCharacterEffect {
    private final int islandID;

    public EUseGrannyEffect(int islandID) {
        super(CharacterType.MONK);
        this.islandID = islandID;
    }

    public int getIslandID() {
        return islandID;
    }
}