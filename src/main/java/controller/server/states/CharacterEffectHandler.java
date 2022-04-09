package controller.server.states;


import controller.server.GameLobby;
import exceptions.*;
import model.GameModel;
import model.board.SchoolBoard;
import model.board.Student;
import model.expert.CharacterCard;
import util.CharacterType;
import util.Color;

import java.util.ArrayList;

/**
 * When the client activates a character effect, the event handler will call one of the methods Below.
 * All the information needed will be sent using the parameters.
 * After the effect is activated, effectIsUsed will become TRUE.
 * TODO: in EventDispatcher check if effect is already active
 * TODO: set & reset effect with the handler in lobby
 */
public class CharacterEffectHandler {

    public void monkEffect(GameLobby tempLobby, int studentID, int islandPos) {
        try {
            GameModel tempGame = tempLobby.getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.MONK);
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            tempGame.getIslandTileByID(islandPos).addStudent(tempCharacter.removeStudent(studentID));
            if (tempGame.getBag().getRemainingStudents() != 0) tempCharacter.addStudent(tempGame.getBag().pull());
        } catch (StudentNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void farmerEffect(GameLobby tempLobby) {
        GameModel tempGame = tempLobby.getModel();
        CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.FARMER);
        tempCharacter.setCost(tempCharacter.getCost() + 1);
    }

    public void heraldEffect(GameLobby tempLobby, int islandGroupID) {
        GameModel tempGame = tempLobby.getModel();
        CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.HERALD);
        tempCharacter.setCost(tempCharacter.getCost() + 1);
        // calls solveIsland method in TmpResolveIsland Class of GameController, passing the island to solve
        tempLobby.getResolveIsland().solveIsland(tempLobby, islandGroupID);
        if (tempGame.checkForRooksEmpty() || tempGame.checkForToFewIslands()) {
            tempLobby.getGameOver().selectWinner(tempLobby);
        }
    }

    public void postmanEffect(GameLobby tempLobby) {
        GameModel tempGame = tempLobby.getModel();
        CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.POSTMAN);
        tempCharacter.setCost(tempCharacter.getCost() + 1);
    }

    public void grannyHerbsEffect(GameLobby tempLobby, int islandID) {
        GameModel tempGame = tempLobby.getModel();
        CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.GRANNY_HERBS);
        tempCharacter.setCost(tempCharacter.getCost() + 1);
        try {
            tempGame.getIslandGroupByID(islandID).addNoEntry(tempCharacter.removeNoEntryTile());
        } catch (EmptyNoEntryListException e) {
            // Do nothing
        }
    }

    public void centaurEffect(GameLobby tempLobby) {
        GameModel tempGame = tempLobby.getModel();
        CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.CENTAUR);
        tempCharacter.setCost(tempCharacter.getCost() + 1);
    }

    // CLIENT checks that size is at most 3
    // the arrayLists are as follows (example 2 students exchanged): eS [13][45]  jS[87][24] containing
    // student IDS, 13 is exchanged with 87 and 45 is exchanged with 24
    public void jesterEffect(GameLobby tempLobby, ArrayList<Integer> entranceStudents, ArrayList<Integer> jesterStudents) throws LengthMismatchException {
        try {
            GameModel tempGame = tempLobby.getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.JESTER);
            SchoolBoard tempSchoolBoard = tempLobby.getCurrentPlayer().getSchoolBoard();
            tempCharacter.setCost(tempCharacter.getCost() + 1);

            Student tempStudent;
            if (entranceStudents.size() != jesterStudents.size()) throw new LengthMismatchException();
            for (int i = 0; i < entranceStudents.size(); i++) {
                tempStudent = tempSchoolBoard.removeFromEntrance(entranceStudents.get(i));
                tempSchoolBoard.addToEntrance(tempCharacter.removeStudent(jesterStudents.get(i)));
                tempCharacter.addStudent(tempStudent);
            }
        } catch (StudentNotFoundException | EntranceFullException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    // CLIENT checks that size is at most 2
    public void minstrelEffect(GameLobby tempLobby, ArrayList<Integer> entranceStudents, ArrayList<Color> diningStudents) {
        try {
            GameModel tempGame = tempLobby.getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.MINSTREL);
            SchoolBoard tempSchoolBoard = tempLobby.getCurrentPlayer().getSchoolBoard();
            tempCharacter.setCost(tempCharacter.getCost() + 1);

            Student tempStudent;
            for (int i = 0; i < entranceStudents.size(); i++) {
                tempStudent = tempSchoolBoard.removeFromDiningRoom(diningStudents.get(i));
                tempSchoolBoard.addToDiningRoom(tempSchoolBoard.removeFromEntrance(entranceStudents.get(i)));
                tempSchoolBoard.addToEntrance(tempStudent);
            }

        } catch (DiningRoomEmptyException | StudentNotFoundException | DiningRoomFullException | EntranceFullException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void knightEffect(GameLobby tempLobby) {
        GameModel tempGame = tempLobby.getModel();
        CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.KNIGHT);
        tempCharacter.setCost(tempCharacter.getCost() + 1);
    }

    public void princessEffect(GameLobby tempLobby, int studentID) {
        try {
            GameModel tempGame = tempLobby.getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.PRINCESS);
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            tempLobby.getCurrentPlayer().getSchoolBoard().addToDiningRoom(tempCharacter.removeStudent(studentID));
            if (tempGame.getBag().getRemainingStudents() != 0) tempCharacter.addStudent(tempGame.getBag().pull());
        } catch (StudentNotFoundException | DiningRoomFullException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void mushroomFanaticEffect(GameLobby tempLobby, Color color) {
        GameModel tempGame = tempLobby.getModel();
        CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.MUSHROOM_FANATIC);
        tempCharacter.setCost(tempCharacter.getCost() + 1);
        tempCharacter.setColor(color);
    }

    public void thiefEffect(GameLobby tempLobby, Color color) {
        try {
            GameModel tempGame = tempLobby.getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.THIEF);
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            for (int i = 0; i < tempGame.getPlayers().size(); i++) {
                for (int k = 0; k < 3; k++) {
                    try {
                        tempGame.getBag().put(tempGame.getPlayerByID(i).getSchoolBoard().removeFromDiningRoom(color));
                    } catch (DiningRoomEmptyException e) {
                        //doNothing
                    }
                }
            }
        } catch (PlayerNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }
}
