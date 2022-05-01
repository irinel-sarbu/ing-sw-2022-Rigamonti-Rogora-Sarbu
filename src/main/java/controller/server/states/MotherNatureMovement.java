package controller.server.states;

import controller.server.GameLobby;
import exceptions.EmptyNoEntryListException;
import exceptions.IllegalMovementException;
import model.GameModel;
import model.expert.CharacterCard;
import util.CharacterType;
import util.GameState;


public abstract class MotherNatureMovement {
    /**
     * Abstract method: move mother nature by the specified number of steps
     *
     * @param tempLobby current lobby
     * @param steps     steps to move mother nature
     * @throws IllegalMovementException the specified movement exceed the maximum possible
     */
    public abstract void moveMotherNature(GameLobby tempLobby, int steps) throws IllegalMovementException;

    protected void nextState(GameLobby tempLobby) {
        try {
            GameModel tempGame = tempLobby.getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.GRANNY_HERBS);
            int motherNaturePos = tempGame.getMotherNature().getPosition();
            if (tempGame.getIslandGroupByID(motherNaturePos).getNoEntrySize() == 0) {
                tempLobby.getResolveIsland().solveIsland(tempLobby, motherNaturePos);
            } else {
                tempCharacter.addNoEntryTile(tempGame.getIslandGroupByID(motherNaturePos).removeNoEntry());
            }
            if (tempGame.checkForRooksEmpty() || tempGame.checkForTooFewIslands()) {
                tempLobby.getGameOver().selectWinner(tempLobby);
            } else {
                tempLobby.setGameState(GameState.TURN_EPILOGUE);
            }

        } catch (EmptyNoEntryListException e) {
            e.printStackTrace();
        }
    }

}
