package controller.server.states;

import controller.server.GameLobby;
import exceptions.ProfessorFullException;
import exceptions.ProfessorNotFoundException;
import model.Player;
import model.board.SchoolBoard;
import util.Color;

public class DefaultStudentMovement extends StudentMovement {

    @Override
    protected boolean canSteal(int currentCount, int withProfessorCount) {
        return currentCount > withProfessorCount;
    }
}