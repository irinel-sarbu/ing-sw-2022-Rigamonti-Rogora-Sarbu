package eventSystem.events.network.client;

import util.CharacterType;

/**
 * Class that represents a client to server message.
 */
public final class EUseHeraldEffect extends EUseCharacterEffect {
    private final int islandGroupID;

    /**
     * Default constructor
     *
     * @param islandGroupID selected island
     */
    public EUseHeraldEffect(int islandGroupID) {
        super(CharacterType.MONK);
        this.islandGroupID = islandGroupID;
    }

    /**
     * Getter
     *
     * @return selected island
     */
    public int getIslandGroupID() {
        return islandGroupID;
    }
}