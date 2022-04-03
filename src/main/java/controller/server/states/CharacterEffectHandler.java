package controller.server.states;


import controller.server.GameController;
import exceptions.*;
import model.GameModel;
import model.Player;
import model.board.IslandGroup;
import model.board.Professor;
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
 *  TODO : switch effectIsUsed to FALSE at the end of the turn (in TurnEpilogue).
 */
public class CharacterEffectHandler {
    private final GameController controller;

    public CharacterEffectHandler(GameController gameController) {
        this.controller = gameController;
    }

    public void monkEffect(String code, int studentID, int islandPos) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.MONK);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            tempGame.getIslandTileByID(islandPos).addStudent(tempCharacter.removeStudent(studentID));
            if (tempGame.getBag().getRemainingStudents() != 0) tempCharacter.addStudent(tempGame.getBag().pull());
        } catch (LobbyNotFoundException | StudentNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void farmerEffect(String code) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.FARMER);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            // TODO : when calculating to whom the professor goes, add an if that checks if this effect is active
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void heraldEffect(String code, int islandGroupID) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.HERALD);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            // calls solveIsland method in ResolveIsland Class of GameController, passing the island to solve
            controller.getResolveIsland().solveIsland(code, islandGroupID);
            if (tempGame.checkForRooksEmpty()||tempGame.checkForToFewIslands()){
                controller.getGameOver().selectWinner(code);
            }
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void postmanEffect(String code) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.POSTMAN);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void grannyHerbsEffect(String code, int islandID) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.GRANNY_HERBS);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            try {
                tempGame.getIslandGroupByID(islandID).addNoEntry(tempCharacter.removeNoEntryTile());
            } catch (EmptyNoEntryListException e) {
                // Do nothing
            }
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void centaurEffect(String code) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.CENTAUR);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    // CLIENT checks that size is at most 3
    // the arrayLists are as follows (example 2 students exchanged): eS [13][45]  jS[87][24] containing
    // student IDS, 13 is exchanged with 87 and 45 is exchanged with 24
    public void jesterEffect(String code, ArrayList<Integer> entranceStudents, ArrayList<Integer> jesterStudents) throws LengthMismatchException {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.JESTER);
            SchoolBoard tempSchoolBoard = GameController.getLobby(code).getCurrentPlayer().getSchoolBoard();
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);

            Student tempStudent;
            if (entranceStudents.size() != jesterStudents.size()) throw new LengthMismatchException();
            for (int i = 0; i < entranceStudents.size(); i++) {
                tempStudent = tempSchoolBoard.removeFromEntrance(entranceStudents.get(i));
                tempSchoolBoard.addToEntrance(tempCharacter.removeStudent(jesterStudents.get(i)));
                tempCharacter.addStudent(tempStudent);
            }
        } catch (LobbyNotFoundException | StudentNotFoundException | EntranceFullException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    // CLIENT checks that size is at most 2
    public void minstrelEffect(String code, ArrayList<Integer> entranceStudents, ArrayList<Color> diningStudents) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.MINSTREL);
            SchoolBoard tempSchoolBoard = GameController.getLobby(code).getCurrentPlayer().getSchoolBoard();
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);

            Student tempStudent;
            for (int i = 0; i < entranceStudents.size(); i++) {
                tempStudent = tempSchoolBoard.removeFromDiningRoom(diningStudents.get(i));
                tempSchoolBoard.addToDiningRoom(tempSchoolBoard.removeFromEntrance(entranceStudents.get(i)));
                tempSchoolBoard.addToEntrance(tempStudent);
            }

        } catch (LobbyNotFoundException | DiningRoomEmptyException | StudentNotFoundException | DiningRoomFullException | EntranceFullException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void knightEffect(String code) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.KNIGHT);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void princessEffect(String code, int studentID) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.PRINCESS);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            GameController.getLobby(code).getCurrentPlayer().getSchoolBoard().addToDiningRoom(tempCharacter.removeStudent(studentID));
            if (tempGame.getBag().getRemainingStudents() != 0) tempCharacter.addStudent(tempGame.getBag().pull());
        } catch (LobbyNotFoundException | StudentNotFoundException | DiningRoomFullException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void mushroomFanaticEffect(String code, Color color) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.MUSHROOM_FANATIC);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            tempCharacter.setColor(color);
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void thiefEffect(String code, Color color) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.THIEF);
            tempCharacter.useEffect();
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
        } catch (LobbyNotFoundException | PlayerNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }
}
