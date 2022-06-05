package controller.server.states;

import controller.server.GameLobby;
import exceptions.IllegalMovementException;

/**
 * Is the DefaultMotherNatureMovement class, extends MotherNatureMovement
 */
public class DefaultMotherNatureMovement extends MotherNatureMovement {
    /**
     * Move mother nature of specified steps in default conditions
     *
     * @param tempLobby current lobby
     * @param steps     steps to move mother nature
     * @throws IllegalMovementException number of steps exceed maximum allowed movements
     */
    @Override
    public void moveMotherNature(GameLobby tempLobby, int steps) throws IllegalMovementException {
        if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements())
            throw new IllegalMovementException();
        tempLobby.getModel().moveMotherNature(steps);
        nextState(tempLobby);
    }
}
