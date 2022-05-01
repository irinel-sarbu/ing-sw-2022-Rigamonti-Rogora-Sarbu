package view.cli;

import eventSystem.EventManager;
import eventSystem.events.local.EUpdateServerInfo;
import eventSystem.events.network.client.*;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToDining;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToIsland;
import model.board.*;
import model.expert.CharacterCard;
import network.LightModel;
import util.*;
import view.View;

import javax.swing.plaf.ColorUIResource;
import java.util.*;

public class CliView extends View {
    Scanner scanner;
    Menu currentMenu;

    @Override
    public void run() {
        this.scanner = new Scanner(System.in);

        System.out.println("Eriantys #LOGO TODO");
        askServerInfo();
    }

    public void closeScanner() {
        scanner.close();
        scanner = new Scanner(System.in);
    }

    private String readString(String defaultValue) {
        String string;
        string = scanner.nextLine();
        return string.isBlank() ? defaultValue : string;
    }

    private int readInt(int defaultValue) {
        int num;

        try {
            String numString = readString("");
            if (numString.isBlank())
                return defaultValue;
            num = Integer.parseInt(numString);
        } catch (NumberFormatException e) {
            System.err.print("Please insert a number and not a String >>> ");
            num = readInt(defaultValue);
        }

        return num;
    }

    @Override
    public void askServerInfo() {
        final int defaultPort = 5000;
        final String defaultAddress = "localhost";

        String address;
        int port;

        System.out.println("> Please specify the following settings. The default value is shown between brackets.");

        System.out.print("\rInsert server ADDRESS [localhost] >>> ");
        address = readString(defaultAddress);

        System.out.print("\rInsert server PORT [" + defaultPort + "] >>> ");
        port = readInt(defaultPort);

        EventManager.notify(new EUpdateServerInfo(address, port));
    }

    private String askPlayerName() {
        String insertedName;

        do {
            System.out.print("\rInsert your name >>> ");
            insertedName = readString("");

            if (insertedName.equals("wwssadadba")) {
                displayMessage("I know you know...");
            }
        } while (insertedName.isBlank());

        return insertedName;
    }

    @Override
    public void chooseCreateOrJoin() {
        int selection;

        System.out.println("Choose between:");
        System.out.println(" [1] Create lobby");
        System.out.println(" [2] Join lobby");

        do {
            System.out.print("\rInsert your choice [1 or 2] >>> ");
            selection = readInt(0);
        } while ((selection != 1) && (selection != 2));

        switch (selection) {
            case 1 -> createLobby();
            case 2 -> joinLobby();
        }
    }

    @Override
    public void createLobby() {
        System.out.println("Choose between:");
        System.out.println(" [1] NORMAL");
        System.out.println(" [2] EXPERT");

        int gameMode;
        do {
            System.out.print("\rInsert your choice [1 or 2] >>> ");
            gameMode = readInt(0);
        } while ((gameMode != 1) && (gameMode != 2));

        int numOfPlayers;
        do {
            System.out.print("\rInsert how many players are going to play [2 or 3] >>> ");
            numOfPlayers = readInt(0);
        } while ((numOfPlayers != 2) && (numOfPlayers != 3));

        String nickname = askPlayerName();
        EventManager.notify(new ECreateLobbyRequest(gameMode == 1 ? GameMode.NORMAL : GameMode.EXPERT, numOfPlayers, nickname));
    }

    @Override
    public void joinLobby() {
        System.out.print("\rInsert lobby code >>> ");
        String lobbyCode = readString("");

        String nickname = askPlayerName();
        EventManager.notify(new EJoinLobbyRequest(lobbyCode, nickname));
    }

    @Override
    public void chooseWizard(List<Wizard> availableWizards) {
        System.out.println("Choose your wizard:");
        for (int i = 0; i < availableWizards.size(); i++) {
            System.out.println(" id " + i + " - " + availableWizards.get(i));
        }

        int choice;
        do {
            System.out.print("\rInsert wizard id >>> ");
            choice = readInt(-1);
        } while (choice < 0 || choice >= availableWizards.size());

        EventManager.notify(new EWizardChosen(availableWizards.get(choice)));
    }

    @Override
    public void chooseAssistant(List<Assistant> deck) {
        System.out.println("Choose an assistant from your deck:");
        for (int i = 0; i < deck.size(); i++) {
            System.out.println(" id " + i + " - " + deck.get(i));
        }

        int choice;
        do {
            System.out.print("\rInsert assistant id >>> ");
            choice = readInt(-1);
        } while (choice < 0 || choice >= deck.size());

        EventManager.notify(new EAssistantChosen(deck.get(choice)));
    }

    @Override
    public void showFirstMenu(LightModel model, String client) {
        Menu main = new Menu("Action phase menu");
        Menu activateCharacter = new Menu("Activate character card menu");
        Menu moveStudent = new Menu("Student movement menu");
        Menu moveStudentToDiningRoom = new Menu("Move students to dining room menu");
        Menu moveStudentToIsland = new Menu("Move students to island menu");

        //TODO: put if to modify the menu relative tho the GAME_STATE

        main.putAction("Move student", () -> {
            switchMenu(moveStudent);
            return true;
        });

        moveStudent.putAction("Return to main menu", () -> {
            switchMenu(main);
            return true;
        });

        moveStudent.putAction("Move student from entrance to island", () -> {
            switchMenu(moveStudentToIsland);
            return true;
        });
        moveStudent.putAction("Move student from entrance to dining room", () -> {
            switchMenu(moveStudentToDiningRoom);
            return true;
        });

        moveStudentToDiningRoom.putAction("Return to movement menu", () -> {
            switchMenu(moveStudent);
            return true;
        });

        moveStudentToDiningRoom.putAction("Select Student to move to the Dining Room", () -> {
            selectStudentToDining(model, client);
            return true;
        });

        moveStudentToIsland.putAction("Return to movement menu", () -> {
            switchMenu(moveStudent);
            return true;
        });

        moveStudentToIsland.putAction("Select Student to move to the island and Select island", () -> {
            selectStudentToIsland(model, client);
            return true;
        });

        if (model.getGameMode() == GameMode.EXPERT) {
            main.putAction("Activate character card", () -> {
                switchMenu(activateCharacter);
                return true;
            });

            activateCharacter.putAction("Return to main menu", () -> {
                switchMenu(main);
                return true;
            });

            activateCharacter.putAction("Activate Character Effect of " + model.getCharacters().get(0), () -> {
                activateEffect(model.getCharacters().get(0), model, client);
                return true;
            });

            activateCharacter.putAction("Activate Character Effect of " + model.getCharacters().get(1), () -> {
                activateEffect(model.getCharacters().get(1), model, client);
                return true;
            });

            activateCharacter.putAction("Activate Character Effect of " + model.getCharacters().get(2), () -> {
                activateEffect(model.getCharacters().get(2), model, client);
                return true;
            });
        }
        switchMenu(main);
    }

    private void switchMenu(Menu newMenu) {
        currentMenu = newMenu;
        currentMenu.show();
        boolean sleep = false;

        while (!sleep) {
            System.out.print("\rChoose action >>> ");
            int actionNumber = readInt(-1);
            sleep = currentMenu.executeAction(actionNumber);
        }
    }

    private void selectStudentToDining(LightModel model, String client) {
        int choice;
        List<Student> studentList = model.getSchoolBoardMap().get(client).getEntranceStudents();
        printEntrance(studentList);
        do {
            System.out.print("\rInsert student >>> ");
            choice = readInt(-1);
        } while (choice < 0 || choice >= studentList.size());
        EventManager.notify(new EStudentMovementToDining(studentList.get(choice).getID()));
    }

    private void selectStudentToIsland(LightModel model, String client) {
        int choice1, choice2;
        List<Student> studentList = model.getSchoolBoardMap().get(client).getEntranceStudents();
        printEntrance(studentList);
        do {
            System.out.print("\rInsert student >>> ");
            choice1 = readInt(-1);
        } while (choice1 < 0 || choice1 >= studentList.size());

        List<IslandGroup> islands = model.getIslandGroups();
        printIslands(islands);
        do {
            System.out.print("\rInsert which island TILE >>> ");
            choice2 = readInt(-1);
        } while (choice2 < 0 || choice2 >= 11); //Hardcoded 12 = max number of islandtiles

        EventManager.notify(new EStudentMovementToIsland(studentList.get(choice1).getID(), choice2));
    }

    private void printEntrance(List<Student> entrance) {
        System.out.println("Students in entrance:");
        for (int i = 0; i < entrance.size(); i++) {
            System.out.printf("\t%2d - %s%n\n", i, entrance.get(i));
        }
    }

    //TODO: TEST INTENSELY

    private void activateEffect(CharacterCard character, LightModel model, String client) {
        int coins = model.getSchoolBoardMap().get(client).getCoinSupply().getNumOfCoins();
        System.out.println("\rYour Coins: " + coins);
        switch (character.getCharacter()) {
            case MONK -> {
                int choice1, choice2;
                List<IslandGroup> islands = model.getIslandGroups();
                printIslands(islands);
                do {
                    System.out.print("\rInsert which island TILE >>> ");
                    choice1 = readInt(-1);
                } while (choice1 < 0 || choice1 >= 11); //Hardcoded 12 = max number of islandtiles
                List<Student> students = character.getStudents();
                printStudents(students);
                do {
                    System.out.print("\rInsert which Student >>> ");
                    choice2 = readInt(-1);
                } while (choice2 < 0 || choice2 >= 3); //Hardcoded 3 = max number of students on MONK

                EventManager.notify(new EUseMonkEffect(students.get(choice2).getID(), choice1));
            }
            case CENTAUR, POSTMAN, FARMER, KNIGHT -> {
                EventManager.notify(new EUseCharacterEffect(character.getCharacter()));
            }
            case HERALD -> {
                int choice;
                List<IslandGroup> islands = model.getIslandGroups();
                printIslandGroups(islands);
                do {
                    System.out.print("\rInsert which island to resolve>>> ");
                    choice = readInt(-1);
                } while (choice < 0 || choice >= islands.size() - 1);
                EventManager.notify(new EUseHeraldEffect(choice));
            }
            case GRANNY_HERBS -> {
                int choice;
                List<IslandGroup> islands = model.getIslandGroups();
                printIslands(islands);
                do {
                    System.out.print("\rInsert which island Tile to Add the No Entry Tiles to>> ");
                    choice = readInt(-1);
                } while (choice < 0 || choice >= 11); //Hardcoded 12 = max number of islandtiles
                EventManager.notify(new EUseGrannyEffect(choice));
            }
            case JESTER -> {
                int choice;
                List<Integer> jesterIDs = new ArrayList<>(), entranceIDs = new ArrayList<>();
                List<Student> jesterStudents = character.getStudents(), entranceStudents = model.getSchoolBoardMap().get(client).getEntranceStudents();

                System.out.println("Select 3 students from Jester:");
                for (int i = 0; i < 3; i++) {
                    printStudents(jesterStudents);
                    do {
                        System.out.print("\rInsert Student number " + i + " >>> ");
                        choice = readInt(-1);
                    } while (choice < 0 || choice >= jesterStudents.size() - 1);
                    jesterIDs.add(jesterStudents.remove(choice).getID());
                }

                System.out.println("Select 3 students from Entrance to switch them with:");
                for (int i = 0; i < 3; i++) {
                    printEntrance(entranceStudents);
                    do {
                        System.out.print("\rInsert Student number " + i + " >>> ");
                        choice = readInt(-1);
                    } while (choice < 0 || choice >= entranceStudents.size() - 1);
                    entranceIDs.add(entranceStudents.remove(choice).getID());
                }

                EventManager.notify(new EUseJesterEffect(entranceIDs, jesterIDs));
            }
            case MINSTREL -> {
                int choice;
                List<Integer> entranceIDs = new ArrayList<>();
                List<Color> diningColors = new ArrayList<>();
                List<Student> entranceStudents = model.getSchoolBoardMap().get(client).getEntranceStudents();

                System.out.println("Select 3 students from Entrance to switch:");
                for (int i = 0; i < 2; i++) {
                    printEntrance(entranceStudents);
                    do {
                        System.out.print("\rInsert Student number " + i + " >>> ");
                        choice = readInt(-1);
                    } while (choice < 0 || choice >= entranceStudents.size() - 1);
                    entranceIDs.add(entranceStudents.remove(choice).getID());
                }

                System.out.println("Select 3 students color from dining room to switch:");
                printDiningRoom(model.getSchoolBoardMap().get(client));
                for (int i = 0; i < 2; i++) {
                    do {
                        System.out.print("\rInsert color ID number " + i + " >>> ");
                        choice = readInt(-1);
                    } while (choice < 0 || choice >= 4); //Hardcoded 5 = max number of colors
                    diningColors.add(Color.values()[choice]);
                }

                EventManager.notify(new EUseMinstrelEffect(entranceIDs, diningColors));
            }
            case PRINCESS -> {
                int choice;
                List<Student> students = character.getStudents();
                printStudents(students);
                do {
                    System.out.print("\rInsert which Student >>> ");
                    choice = readInt(-1);
                } while (choice < 0 || choice >= 3); //Hardcoded 3 = max number of students on Princess

                EventManager.notify(new EUsePrincessEffect(choice));
            }
            case MUSHROOM_FANATIC -> {
                int choice;
                System.out.println("Choose a color from:");
                for (Color color : Color.values()) {
                    System.out.printf("\t%2d - %s\n", color.getValue(), color);
                }
                do {
                    System.out.print("\rInsert color ID >>> ");
                    choice = readInt(-1);
                } while (choice < 0 || choice >= 4); //Hardcoded 5 = max number of colors

                EventManager.notify(new EUseFanaticEffect(Color.values()[choice]));
            }
            case THIEF -> {
                int choice;
                System.out.println("Choose a color from:");
                for (Color color : Color.values()) {
                    System.out.printf("\t%2d - %s\n", color.getValue(), color);
                }
                do {
                    System.out.print("\rInsert color ID >>> ");
                    choice = readInt(-1);
                } while (choice < 0 || choice >= 4); //Hardcoded 5 = max number of colors

                EventManager.notify(new EUseThiefEffect(Color.values()[choice]));
            }
            default -> {
                Logger.warning("Stuck.. Missing Selected Character type");
            }
        }

    }

    private void printStudents(List<Student> students) {
        System.out.println("Select one student from:");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("\t%2d - %s%n\n", i, students.get(i));
        }
    }

    private void printIslandGroups(List<IslandGroup> islandGroups) {
        System.out.println("Select one Island Group From:");
        for (int i = 0; i < islandGroups.size(); i++) {
            System.out.printf("\t%2d - %s%n\n", i, islandGroups.get(i));
        }
    }

    private void printDiningRoom(SchoolBoard schoolBoard) {
        System.out.println("Select 3 colors from:");
        for (Color color : Color.values()) {
            System.out.printf("\t%2d - %s students are %2d\n", color.getValue(), color, schoolBoard.getStudentsOfColor(color));
        }
    }


    @Override
    public void update(LightModel model) {
        // Clear screen - doesn't work in intellij terminal
        System.out.print("\033[H\033[2J");

        System.out.println("BOARD");

        System.out.println("- islands:");
        printIslands(model.getIslandGroups());

        System.out.println("- mother nature position: " + model.getMotherNaturePosition());

        System.out.println("- cloud tiles: ");
        printCloudTiles(model.getCloudTiles());

        for (Map.Entry<String, SchoolBoard> entry : model.getSchoolBoardMap().entrySet()) {
            System.out.println("- schoolBoard of " + entry.getKey() + ":\n" + entry.getValue());
        }

        if (model.getCharacters() != null) {
            System.out.println("- extracted characters: " + model.getCharacters());
            if (model.getActiveCharacterEffect() != null) {
                System.out.println("- active character effect: " + model.getActiveCharacterEffect());
            } else {
                System.out.println("- active character effect: none");
            }
        }

        System.out.println("- assistants: " + model.getDeck());
    }

    private void printIslands(List<IslandGroup> islandGroups) {
        for (IslandGroup group : islandGroups) {
            String islandGroupName = "IslandGroup_" + String.format("%2s", group.getIslandGroupID()).replace(' ', '0');
            System.out.println("\t" + islandGroupName + ":");
            for (IslandTile island : group.getIslands()) {
                System.out.println("\t\t" + island);
            }
        }
        System.out.println();
    }

    private void printCloudTiles(List<CloudTile> cloudTiles) {
        for (CloudTile cloudTile : cloudTiles) {
            System.out.println("\t" + cloudTile);
        }
        System.out.println();
    }
}
