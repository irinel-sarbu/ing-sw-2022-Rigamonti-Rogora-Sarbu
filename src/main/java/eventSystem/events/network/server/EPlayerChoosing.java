package eventSystem.events.network.server;

import eventSystem.events.Event;

public class EPlayerChoosing extends Event {
    private final String playerName;
    private final ChoiceType choiceType;

    public EPlayerChoosing(String playerName, ChoiceType chooseType) {
        this.playerName = playerName;
        this.choiceType = chooseType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ChoiceType getChoiceType() {
        return choiceType;
    }

    @Override
    public String toString() {
        return "EPlayerChoosing { playerName: " + playerName + ", choiceType: " + choiceType + " }";
    }
}