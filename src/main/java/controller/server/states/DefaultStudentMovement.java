package controller.server.states;

import controller.server.GameLobby;
import exceptions.ProfessorFullException;
import exceptions.ProfessorNotFoundException;
import model.Player;
import model.board.SchoolBoard;
import util.Color;

public class DefaultStudentMovement extends StudentMovement {

    /**
     * Check if it's possible to steal the professor in normal conditions
     *
     * @param currentCount       students of the current player referring to the professor of a specific color
     * @param withProfessorCount students of the player who already has the professor of the same color
     * @return true if the number of students of the current player exceeds the number of students of the player with the specified color professor
     */
    @Override
    protected boolean canSteal(int currentCount, int withProfessorCount) {
        return currentCount > withProfessorCount;
    }
}