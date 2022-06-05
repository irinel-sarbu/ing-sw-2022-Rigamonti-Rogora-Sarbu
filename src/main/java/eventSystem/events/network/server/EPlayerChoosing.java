package eventSystem.events.network.server;

import eventSystem.events.Event;

/**
 * Class that represents a server to client message.
 */
public class EPlayerChoosing extends Event {
    private final String playerName;
    private final ChoiceType choiceType;

    /**
     * Default constructor
     *
     * @param playerName of player that is choosing
     * @param chooseType
     */
    public EPlayerChoosing(String playerName, ChoiceType chooseType) {
        this.playerName = playerName;
        this.choiceType = chooseType;
    }

    /**
     * Getter
     *
     * @return name of player that is choosing
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter
     *
     * @return choice type
     */
    public ChoiceType getChoiceType() {
        return choiceType;
    }

    @Override
    public String toString() {
        return "EPlayerChoosing { playerName: " + playerName + ", choiceType: " + choiceType + " }";
    }
}