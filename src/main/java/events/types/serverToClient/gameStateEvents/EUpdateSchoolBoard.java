package events.types.serverToClient.gameStateEvents;

import events.Event;
import events.EventType;
import model.board.SchoolBoard;

public class EUpdateSchoolBoard extends Event {
    private final SchoolBoard schoolBoard;
    private final String playerName;

    public EUpdateSchoolBoard(SchoolBoard schoolBoard, String playerName) {
        super(EventType.UPDATE_SCHOOLBOARD);
        this.schoolBoard = schoolBoard;
        this.playerName = playerName;
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "EUpdateSchoolBoard { SchoolBoardOf: " + playerName + " }";
    }
}
