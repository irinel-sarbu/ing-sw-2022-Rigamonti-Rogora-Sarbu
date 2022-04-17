package events.types.clientToServer;

import util.CharacterType;

public class EUseHeraldEffect extends EUseCharacterEffect {
    private final int islandGroupID;

    public EUseHeraldEffect(int islandGroupID) {
        super(CharacterType.MONK);
        this.islandGroupID = islandGroupID;
    }

    public int getIslandGroupID() {
        return islandGroupID;
    }
}