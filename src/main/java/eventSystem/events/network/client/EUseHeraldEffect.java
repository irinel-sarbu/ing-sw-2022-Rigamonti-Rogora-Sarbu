package eventSystem.events.network.client;

import util.CharacterType;

public final class EUseHeraldEffect extends EUseCharacterEffect {
    private final int islandGroupID;

    public EUseHeraldEffect(int islandGroupID) {
        super(CharacterType.MONK);
        this.islandGroupID = islandGroupID;
    }

    public int getIslandGroupID() {
        return islandGroupID;
    }
}