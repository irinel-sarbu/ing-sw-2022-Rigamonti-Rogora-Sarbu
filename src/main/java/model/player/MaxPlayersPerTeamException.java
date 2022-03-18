package model.player;

public class MaxPlayersPerTeamException extends RuntimeException {

    public MaxPlayersPerTeamException(String message) {
        super(message);
    }

    public MaxPlayersPerTeamException() {
        super();
    }
}
