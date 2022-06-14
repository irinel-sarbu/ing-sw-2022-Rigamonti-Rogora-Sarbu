package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.SchoolBoard;

/**
 * Class that represents a server to client message.
 */
public class EUpdateSchoolBoard extends Event {
    private final SchoolBoard schoolBoard;
    private final String playerName;

    /**
     * Default constructor
     *
     * @param schoolBoard updated schoolboard
     * @param playerName  owner of the schoolboard
     */
    public EUpdateSchoolBoard(SchoolBoard schoolBoard, String playerName) {
        this.schoolBoard = schoolBoard;
        this.playerName = playerName;
    }

    /**
     * Getter
     *
     * @return schoolboard
     */
    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    /**
     * Getter
     *
     * @return player name
     */
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "EUpdateSchoolBoard { SchoolBoardOf: " + playerName + " }";
    }
}
