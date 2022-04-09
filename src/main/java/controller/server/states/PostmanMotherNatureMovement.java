package controller.server.states;

import controller.server.GameLobby;
import exceptions.IllegalMovementException;

public class PostmanMotherNatureMovement extends MotherNatureMovement{
    @Override
    public void moveMotherNature(GameLobby tempLobby, int steps) throws IllegalMovementException {
        if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements() + 2)
            throw new IllegalMovementException();
        tempLobby.getModel().moveMotherNature(steps);
        nextState(tempLobby);
    }
}
