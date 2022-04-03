package controller.server.states;

import controller.server.GameController;
import controller.server.GameLobby;
import exceptions.*;
import model.Player;
import util.GameState;

public class TurnEpilogue {

    private final GameController controller;

    public TurnEpilogue(GameController controller) {
        this.controller = controller;
    }

    private boolean checkGameOver(GameLobby thisGame) {
        return thisGame.getModel().getBag().isEmpty() ||
                thisGame.getOrder().stream().anyMatch(player -> player.getAssistants().size() == 0);
    }

    public void refillFromCloudTile(GameLobby thisGame, Player actingPlayer, int cloudTilePos)
            throws WrongPhaseException, WrongPlayerException, NoCloudTileException, EntranceFullException {
        if (thisGame.wrongState(GameState.TURN_EPILOGUE)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(actingPlayer)) throw new WrongPlayerException();
        if (cloudTilePos >= thisGame.getModel().getNumOfCloudTiles()) throw new NoCloudTileException();
        if (thisGame.getModel().getCloudTile(cloudTilePos).isEmpty()) throw new EmptyStudentListException();

        actingPlayer.getSchoolBoard().addToEntrance(thisGame.getModel().getCloudTile(cloudTilePos).getAndRemoveStudents());

        if (thisGame.setNextPlayer()) {
            thisGame.setGameState(GameState.STUDENT_MOVEMENT);
        } else {
            if (checkGameOver(thisGame))
                controller.getGameOver().selectWinner(thisGame.getCode());
            else
                thisGame.nextTurn();
        }
    }
}
