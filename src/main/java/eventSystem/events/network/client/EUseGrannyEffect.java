package eventSystem.events.network.client;

import util.CharacterType;

/**
 * Class that represents a client to server message.
 */
public final class EUseGrannyEffect extends EUseCharacterEffect {
    private final int islandID;

    /**
     * Default constructor
     *
     * @param islandID island where to put no entry tile
     */
    public EUseGrannyEffect(int islandID) {
        super(CharacterType.MONK);
        this.islandID = islandID;
    }

    /**
     * Getter
     *
     * @return island id
     */
    public int getIslandID() {
        return islandID;
    }
}