package controller.server.states;

import controller.server.GameLobby;
import exceptions.*;
import model.Player;
import util.GameState;

public class TurnEpilogue {

    /**
     * Check if game over should be called, conditions are:
     * - empty bag at the end of the turn
     * - a player's assistant deck is empty
     *
     * @param thisGame {@link GameLobby} to inspect
     * @return true if game over should be called, false otherwise
     */
    private boolean checkGameOver(GameLobby thisGame) {
        return thisGame.getModel().getBag().isEmpty() ||
                thisGame.getOrder().stream().anyMatch(player -> player.getAssistants().size() == 0);
    }

    /**
     * At the end of the turn, player chooses a cloud tile and moves students to own entrance.
     *
     * @param thisGame     lobby the action is referring to
     * @param actingPlayer player giving the command
     * @param cloudTilePos index of cloud tile selected to pick student from
     * @throws WrongPhaseException   the game is not in the proper phase to performs this action
     * @throws WrongPlayerException  the acting player does not have the right to act at this moment
     * @throws NoCloudTileException  the selected cloud tile does not exist
     * @throws EntranceFullException player's entrance is full
     */
    // TODO: when EntranceFullException is thrown, asks the player to select which students to keep in its entrance
    public void refillFromCloudTile(GameLobby thisGame, Player actingPlayer, int cloudTilePos)
            throws WrongPhaseException, WrongPlayerException, NoCloudTileException, EntranceFullException {

        if (thisGame.wrongPlayer(actingPlayer)) throw new WrongPlayerException();
        if (cloudTilePos >= thisGame.getModel().getNumOfCloudTiles()) throw new NoCloudTileException();
        if (thisGame.getModel().getCloudTile(cloudTilePos).isEmpty()) throw new EmptyStudentListException();

        thisGame.getModel().moveFromCloudTileToEntrance(thisGame.getModel().getCloudTile(cloudTilePos), thisGame.getCurrentPlayer());

        /*
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
                thisGame.nextRound();
            }
        }

        // call currentplayer switch, if last player switch round
        if(!thisGame.setNextPlayer()) {
            thisGame.nextRound();
        }

    }
}
