package controller.server.states;

import controller.server.GameLobby;
import exceptions.EmptyNoEntryListException;
import exceptions.IllegalMovementException;
import model.GameModel;
import model.expert.CharacterCard;
import util.CharacterType;
import util.GameState;


public class MotherNatureMovement {
    public void moveMotherNature(GameLobby tempLobby, int steps) throws IllegalMovementException {
        if (tempLobby.getModel().getCharacterByType(CharacterType.POSTMAN) != null && tempLobby.getModel().getCharacterByType(CharacterType.POSTMAN).getEffect()) {
            if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements() + 2)
                throw new IllegalMovementException();
        } else {
            if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements())
                throw new IllegalMovementException();
        }
        tempLobby.getModel().moveMotherNature(steps);
        nextState(tempLobby);
    }

    private void nextState(GameLobby tempLobby) {
        try {
            GameModel tempGame = tempLobby.getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.GRANNY_HERBS);
            int motherNaturePos = tempGame.getMotherNature().getPosition();
            if (tempGame.getIslandGroupByID(motherNaturePos).getNoEntrySize() == 0) {
                tempLobby.getResolveIsland().solveIsland(tempLobby, motherNaturePos);
            } else {
                tempCharacter.addNoEntryTile(tempGame.getIslandGroupByID(motherNaturePos).removeNoEntry());
            }
            if (tempGame.checkForRooksEmpty() || tempGame.checkForToFewIslands()) {
                tempLobby.getGameOver().selectWinner(tempLobby);
            } else {
                tempLobby.setGameState(GameState.TURN_EPILOGUE);
            }

        } catch (EmptyNoEntryListException e) {
            e.printStackTrace();
        }
    }

}
