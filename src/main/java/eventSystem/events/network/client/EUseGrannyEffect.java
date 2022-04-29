package eventSystem.events.network.client;

import util.CharacterType;

public final class EUseGrannyEffect extends EUseCharacterEffect {
    private final int islandID;

    public EUseGrannyEffect(int islandID) {
        super(CharacterType.MONK);
        this.islandID = islandID;
    }

    public int getIslandID() {
        return islandID;
    }
}