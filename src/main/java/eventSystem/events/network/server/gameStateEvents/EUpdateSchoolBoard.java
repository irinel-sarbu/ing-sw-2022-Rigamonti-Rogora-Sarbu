package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.SchoolBoard;

public class EUpdateSchoolBoard extends Event {
    private final SchoolBoard schoolBoard;
    private final String playerName;

    public EUpdateSchoolBoard(SchoolBoard schoolBoard, String playerName) {
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
