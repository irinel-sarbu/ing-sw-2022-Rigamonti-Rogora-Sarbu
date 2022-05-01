package controller.server.states;

import controller.server.GameLobby;
import eventSystem.events.network.server.gameStateEvents.EUpdateIslands;
import eventSystem.events.network.server.gameStateEvents.EUpdateSchoolBoard;
import exceptions.*;
import model.Player;
import model.board.SchoolBoard;
import model.board.Student;
import util.Color;
import util.GameState;

public abstract class StudentMovement {

    /**
     * If necessary moves to the next game phase
     *
     * @param thisGame game lobby the command is referring to
     */
    protected void movementEpilogue(GameLobby thisGame) {
        thisGame.addStudentsMoved();
        Player player = thisGame.getCurrentPlayer();
        thisGame.broadcast(new EUpdateSchoolBoard(player.getSchoolBoard(), player.getName()));
        if (thisGame.getStudentsMoved() == thisGame.getMaxStudentsMoved()) {
            thisGame.resetStudentsMoved();
            thisGame.setGameState(GameState.MOTHERNATURE_MOVEMENT);
        }
    }

    /**
     * Abstract method: check if it's possible to steal a professor given numbers of student
     *
     * @param currentCount       students of the current player referring to the professor of a specific color
     * @param withProfessorCount students of the player who already has the professor of the same color
     * @return true if the player can steal the professor, false otherwise
     */
    protected abstract boolean canSteal(int currentCount, int withProfessorCount);

    /**
     * Checks if the current player can steal a professor from another player and do so
     *
     * @param thisGame game lobby the command is referring to
     * @param color    color of the professor to steal
     * @throws ProfessorFullException the professor table is full (should never happen, even if the player already has the professor
     */
    protected void stealProfessor(GameLobby thisGame, Color color)
            throws ProfessorFullException {
        if (thisGame.getCurrentPlayer().getSchoolBoard().hasProfessor(color))
            return; // prevent from self stealing
        // TODO: may exists another way to check this
        try {
            thisGame.getCurrentPlayer().getSchoolBoard().addProfessor(thisGame.getModel().removeProfessor(color));
        } catch (ProfessorNotFoundException e) {    // someone else already has this professor
            SchoolBoard withProfessor = thisGame.getOrder().stream()
                    .map(Player::getSchoolBoard)
                    .filter(sb -> sb.hasProfessor(color)).findFirst().orElse(null);
            int withProfessorCount = withProfessor.getStudentsOfColor(color);
            SchoolBoard current = thisGame.getCurrentPlayer().getSchoolBoard();
            int currentCount = current.getStudentsOfColor(color);

            if (canSteal(currentCount, withProfessorCount)) {
                current.addProfessor(withProfessor.removeProfessorByColor(color));
            }
        }
    }

    /**
     * Move a student from a player entrance to own dining room
     *
     * @param thisGame  game lobby the command is referring to
     * @param player    acting player
     * @param studentID ID of the student to move
     * @throws WrongPhaseException      command happens in the wrong phase
     * @throws WrongPlayerException     acting player does not have the right to act
     * @throws StudentNotFoundException selected student does not exist in the player entrance
     * @throws DiningRoomFullException  current player dining room can't accommodate any other student of the specified color
     * @throws ProfessorFullException   check {@link StudentMovement#stealProfessor(GameLobby, Color)}
     */
    public void moveStudentToDining(GameLobby thisGame, Player player, int studentID)
            throws WrongPhaseException, WrongPlayerException, StudentNotFoundException, DiningRoomFullException,
            ProfessorFullException {
        if (thisGame.wrongState(GameState.STUDENT_MOVEMENT))
            throw new WrongPhaseException();
        if (thisGame.wrongPlayer(player))
            throw new WrongPlayerException();

        Student movingStudent = thisGame.getCurrentPlayer().getSchoolBoard().getEntranceStudent(studentID);
        if (thisGame.getCurrentPlayer().getSchoolBoard().addToDiningRoom(movingStudent)) {
            try {
                thisGame.getModel().getCoinSupply().removeCoins(1);
                thisGame.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoin();
            } catch (supplyEmptyException e) {
                // Do nothing
            }

        }
        thisGame.getCurrentPlayer().getSchoolBoard().removeFromEntrance(studentID);

        stealProfessor(thisGame, movingStudent.getColor());

        movementEpilogue(thisGame);
    }

    /**
     * Move a student from the entrance to the specified island
     *
     * @param thisGame  game lobby the command is referring to
     * @param player    acting player
     * @param studentID ID of the student to move to the island
     * @param islandID  ID of the island to move the student to
     * @throws WrongPhaseException      command happens in the wrong phase
     * @throws WrongPlayerException     acting player does not have the right to act
     * @throws StudentNotFoundException selected student does not exists in the player entrance
     */
    public void moveStudentToIsland(GameLobby thisGame, Player player, int studentID, int islandID)
            throws WrongPhaseException, WrongPlayerException, StudentNotFoundException {
        if (thisGame.wrongState(GameState.STUDENT_MOVEMENT)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(player)) throw new WrongPlayerException();

        Student movingStudent = thisGame.getCurrentPlayer().getSchoolBoard().getEntranceStudent(studentID);
        thisGame.getModel().getIslandTileByID(islandID).addStudent(movingStudent);
        thisGame.getCurrentPlayer().getSchoolBoard().removeFromEntrance(studentID);

        thisGame.broadcast(new EUpdateIslands(thisGame.getModel().getIslandGroups(), thisGame.getModel().getMotherNature().getPosition()));
        movementEpilogue(thisGame);
    }
}
