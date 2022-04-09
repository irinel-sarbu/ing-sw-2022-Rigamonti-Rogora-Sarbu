package controller.server.states;

import controller.server.GameLobby;
import exceptions.IllegalMovementException;

public class DefaultMotherNatureMovement extends MotherNatureMovement {
    @Override
    public void moveMotherNature(GameLobby tempLobby, int steps) throws IllegalMovementException {
        if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements())
            throw new IllegalMovementException();
        tempLobby.getModel().moveMotherNature(steps);
        nextState(tempLobby);
    }
}
