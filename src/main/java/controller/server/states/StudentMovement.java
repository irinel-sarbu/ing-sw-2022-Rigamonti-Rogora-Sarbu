package controller.server.states;

import controller.server.GameLobby;
import exceptions.DiningRoomFullException;
import exceptions.StudentNotFoundException;
import exceptions.WrongPhaseException;
import exceptions.WrongPlayerException;
import model.Player;
import model.board.Student;
import util.GameState;

public class StudentMovement {
    public StudentMovement() {

    }

    public void moveStudentToDining(GameLobby thisGame, Player player, int studentPosition)
            throws WrongPhaseException, WrongPlayerException, StudentNotFoundException, DiningRoomFullException {
        if (thisGame.wrongState(GameState.STUDENT_MOVEMENT)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(player)) throw new WrongPlayerException();

        Student movingStudent = thisGame.getCurrentPlayer().getSchoolBoard().getEntranceStudent(studentPosition);
        thisGame.getCurrentPlayer().getSchoolBoard().addToDiningRoom(movingStudent);
        thisGame.getCurrentPlayer().getSchoolBoard().removeFromEntrance(studentPosition);
    }

    public void moveStudentToIsland(GameLobby thisGame, Player player, int studentPosition, int islandID)
            throws WrongPhaseException, WrongPlayerException, StudentNotFoundException {
        if (thisGame.wrongState(GameState.STUDENT_MOVEMENT)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(player)) throw new WrongPlayerException();

        Student movingStudent = thisGame.getCurrentPlayer().getSchoolBoard().getEntranceStudent(studentPosition);
        thisGame.getModel().getIslandTileByID(islandID).addStudent(movingStudent);
        thisGame.getCurrentPlayer().getSchoolBoard().removeFromEntrance(studentPosition);
    }
}
