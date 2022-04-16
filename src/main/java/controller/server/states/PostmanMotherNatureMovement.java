package controller.server.states;

import controller.server.GameLobby;
import exceptions.IllegalMovementException;

public class PostmanMotherNatureMovement extends MotherNatureMovement {
    /**
     * Move mother nature when postman has been used (2 more steps)
     *
     * @param tempLobby current game lobby
     * @param steps     maximum steps of mother nature specified by the assistant card (maximum movement is greater by 2)
     * @throws IllegalMovementException the number of steps exceed the maximum permitted
     */
    @Override
    public void moveMotherNature(GameLobby tempLobby, int steps) throws IllegalMovementException {
        if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements() + 2)
            throw new IllegalMovementException();
        tempLobby.getModel().moveMotherNature(steps);
        nextState(tempLobby);
    }
}
