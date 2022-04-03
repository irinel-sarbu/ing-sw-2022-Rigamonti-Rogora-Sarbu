package controller.server.states;

import controller.server.GameLobby;
import exceptions.*;
import model.Player;
import model.board.SchoolBoard;
import model.board.Student;
import model.expert.CharacterCard;
import util.CharacterType;
import util.Color;
import util.GameState;

import java.util.Optional;
import java.util.stream.Collectors;

public class StudentMovement {
    public StudentMovement() {

    }

    private void movementEpilogue(GameLobby thisGame) {
        thisGame.addStudentsMoved();
        if (thisGame.getStudentsMoved() == thisGame.getMaxStudentsMoved()) {
            thisGame.resetStudentsMoved();
            thisGame.setGameState(GameState.MOTHERNATURE_MOVEMENT);
        }
    }

    private void stealProfessor(GameLobby thisGame, Color color, Boolean farmerUsed)
            throws ProfessorFullException, ProfessorNotFoundException {
        if (thisGame.getCurrentPlayer().getSchoolBoard().hasProfessor(color)) return; // prevent from self stealing
        // TODO: magari esiste un altro modo per farlo
        try {
            thisGame.getCurrentPlayer().getSchoolBoard().addProfessor(thisGame.getModel().removeProfessor(color));
        } catch (ProfessorNotFoundException e) {    // someone else already has this professor
            SchoolBoard withProfessor = thisGame.getOrder().stream()
                    .map(Player::getSchoolBoard)
                    .filter(sb -> sb.hasProfessor(color)).findFirst().orElse(null);
            assert withProfessor != null;
            int withProfessorCount = withProfessor.getStudentsOfColor(color);
            SchoolBoard current = thisGame.getCurrentPlayer().getSchoolBoard();
            int currentCount = current.getStudentsOfColor(color);

            if (currentCount > withProfessorCount - (farmerUsed ? 1 : 0)) {
                current.addProfessor(withProfessor.removeProfessorByColor(color));
            }
        }
    }


    public void moveStudentToDining(GameLobby thisGame, Player player, int studentPosition)
            throws WrongPhaseException, WrongPlayerException, StudentNotFoundException, DiningRoomFullException,
            ProfessorFullException, ProfessorNotFoundException {
        if (thisGame.wrongState(GameState.STUDENT_MOVEMENT)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(player)) throw new WrongPlayerException();

        Student movingStudent = thisGame.getCurrentPlayer().getSchoolBoard().getEntranceStudent(studentPosition);
        thisGame.getCurrentPlayer().getSchoolBoard().addToDiningRoom(movingStudent);
        thisGame.getCurrentPlayer().getSchoolBoard().removeFromEntrance(studentPosition);

        CharacterCard farmer = thisGame.getModel().getCharacterByType(CharacterType.FARMER);
        boolean farmerUsed = (farmer != null && farmer.getEffect());

        stealProfessor(thisGame, movingStudent.getColor(), farmerUsed);

        movementEpilogue(thisGame);

    }

    public void moveStudentToIsland(GameLobby thisGame, Player player, int studentPosition, int islandID)
            throws WrongPhaseException, WrongPlayerException, StudentNotFoundException {
        if (thisGame.wrongState(GameState.STUDENT_MOVEMENT)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(player)) throw new WrongPlayerException();

        Student movingStudent = thisGame.getCurrentPlayer().getSchoolBoard().getEntranceStudent(studentPosition);
        thisGame.getModel().getIslandTileByID(islandID).addStudent(movingStudent);
        thisGame.getCurrentPlayer().getSchoolBoard().removeFromEntrance(studentPosition);

        movementEpilogue(thisGame);

    }
}
