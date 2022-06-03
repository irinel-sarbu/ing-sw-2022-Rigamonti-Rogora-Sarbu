package view.gui;

import controller.client.ClientController;
import exceptions.DiningRoomFullException;
import exceptions.PlayerNotFoundException;
import exceptions.ProfessorFullException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.GameModel;
import model.Player;
import model.board.Professor;
import model.board.SchoolBoard;
import model.board.Student;
import network.LightModel;
import util.*;
import view.View;
import view.gui.controllers.characterControllers.*;


public class GuiApplication extends Application {

    private ClientController clientController;
    private View guiView;


    @Override
    public void start(Stage stage) throws Exception {
        this.guiView = new GuiView();
        this.clientController = new ClientController(guiView);
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image("/ui/Icon.png"));
        SceneController.setStage(stage);
        SceneController.switchScene("loginScene.fxml");
        SceneController.startMediaPlayer();
        stage.show();
        //charTester();
    }

    @Override
    public void stop() {
        SceneController.stopMediaPlayer();
        Platform.exit();
        System.exit(0);
    }

    private void charTester() {
        //RANDOM SEEDS:
        // -- 0 = JESTER, MUSHROOM FANATIC, CENTAUR
        // -- 3 = GRANNY HERBS, MINSTREL, KNIGHT
        // -- 14 = HERALD, FARMER, THIEF
        // -- 1750 = PRINCESS, POSTMAN, MONK
        LightModel model = new LightModel("player0");

        Random.setSeed(14);
        GameModel game = new GameModel(2, GameMode.EXPERT);
        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        try {
            model.setPlayerSchoolBoard("player0", game.getPlayerByName("player0").getSchoolBoard());
            game.getPlayerByName("player0").getSchoolBoard().addToDiningRoom(new Student(0, Color.RED));
            game.getPlayerByName("player0").getSchoolBoard().addToDiningRoom(new Student(1, Color.RED));
            game.getPlayerByName("player0").getSchoolBoard().addToDiningRoom(new Student(2, Color.RED));
            game.getPlayerByName("player0").getSchoolBoard().addToDiningRoom(new Student(3, Color.BLUE));
            game.getPlayerByName("player0").getSchoolBoard().addProfessor(new Professor(Color.RED));
        } catch (PlayerNotFoundException | DiningRoomFullException | ProfessorFullException e) {
            return;
        }


        model.setCharacters(game.getCharacters());
        model.setIslandGroups(game.getIslandGroups());
        SceneController.switchScene("thiefSelection.fxml");
        ThiefSelectionSceneController controller = (ThiefSelectionSceneController) SceneController.getCurrentSceneController();
        controller.setUpCharacterChoice(model);
    }
}
