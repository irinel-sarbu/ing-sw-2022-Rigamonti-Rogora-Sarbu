package controller.server.states;

import controller.server.GameLobby;
import exceptions.*;
import model.Player;
import util.GameState;

public class TurnEpilogue {

    private boolean checkGameOver(GameLobby thisGame) {
        return thisGame.getModel().getBag().isEmpty() ||
                thisGame.getOrder().stream().anyMatch(player -> player.getAssistants().size() == 0);
    }

    /**
     * At the end of the turn, player chooses a cloud tile and moves students to own entrance.
     *
     * @param thisGame
     * @param actingPlayer
     * @param cloudTilePos
     * @throws WrongPhaseException
     * @throws WrongPlayerException
     * @throws NoCloudTileException
     * @throws EntranceFullException
     */
    public void refillFromCloudTile(GameLobby thisGame, Player actingPlayer, int cloudTilePos)
            throws WrongPhaseException, WrongPlayerException, NoCloudTileException, EntranceFullException {

        if (thisGame.wrongPlayer(actingPlayer)) throw new WrongPlayerException();
        if (cloudTilePos >= thisGame.getModel().getNumOfCloudTiles()) throw new NoCloudTileException();
        if (thisGame.getModel().getCloudTile(cloudTilePos).isEmpty()) throw new EmptyStudentListException();

        thisGame.getModel().moveFromCloudTileToEntrance(thisGame.getModel().getCloudTile(cloudTilePos), thisGame.getCurrentPlayer());

        /**
         * Sets next player.
         * If there is a next player restart the action phase with STUDENT_MOVEMENT state
         * Else checks if game is over and goes in GAME_OVER state
         * If not game over go to next round AKA PLANNING_PHASE
         */
        if (thisGame.setNextPlayer()) {
            thisGame.setGameState(GameState.STUDENT_MOVEMENT);
        } else {
            if (checkGameOver(thisGame)) {
                thisGame.getGameOver().selectWinner(thisGame);
            } else {
                thisGame.nextTurn();
            }
        }
    }
}
