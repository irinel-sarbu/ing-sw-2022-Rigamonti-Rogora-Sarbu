package controller.server.states;

import controller.server.GameController;
import controller.server.GameLobby;
import exceptions.EmptyNoEntryListException;
import exceptions.IllegalMovementException;
import exceptions.LobbyNotFoundException;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.expert.CharacterCard;
import util.CharacterType;
import util.GameState;


public class MotherNatureMovement {

    private final GameController controller;

    public MotherNatureMovement(GameController gameController) {
        this.controller = gameController;
    }

    public void moveMotherNature(String code, int steps) throws IllegalMovementException {
        try {
            GameLobby tempLobby = GameController.getLobby(code);
            if (tempLobby.getModel().getCharacterByType(CharacterType.POSTMAN) != null && tempLobby.getModel().getCharacterByType(CharacterType.POSTMAN).getEffect()) {
                if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements() + 2)
                    throw new IllegalMovementException();
            } else {
                if (steps > tempLobby.getCurrentPlayer().peekFoldDeck().getMovements())
                    throw new IllegalMovementException();
            }
            tempLobby.getModel().moveMotherNature(steps);
            nextState(code);
        } catch (LobbyNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void nextState(String code) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.GRANNY_HERBS);
            int motherNaturePos = tempGame.getMotherNature().getPosition();
            if (tempGame.getIslandGroupByID(motherNaturePos).getNoEntrySize() == 0) {
                controller.getResolveIsland().solveIsland(code, motherNaturePos);
            } else {
                tempCharacter.addNoEntryTile(tempGame.getIslandGroupByID(motherNaturePos).removeNoEntry());
            }
            if (tempGame.checkForRooksEmpty()||tempGame.checkForToFewIslands()){
                controller.getGameOver().selectWinner(code);
            }else{
                GameController.getLobby(code).setGameState(GameState.TURN_EPILOGUE);
            }

        } catch (LobbyNotFoundException | EmptyNoEntryListException e) {
            e.printStackTrace();
        }
    }

}
