package view.gui;

import controller.client.ClientController;
import exceptions.PlayerNotFoundException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.GameModel;
import model.Player;
import model.board.SchoolBoard;
import network.LightModel;
import util.*;
import view.View;
import view.gui.controllers.characterControllers.GrannyIslandSelectorSceneController;
import view.gui.controllers.characterControllers.HeraldIslandSelectorSceneController;
import view.gui.controllers.characterControllers.JesterCardSelectionSceneController;
import view.gui.controllers.characterControllers.MonkSelectionSceneController;


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

        Random.setSeed(0);
        GameModel game = new GameModel(2, GameMode.EXPERT);
        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        try {
            model.setPlayerSchoolBoard("player0", game.getPlayerByName("player0").getSchoolBoard());
        } catch (PlayerNotFoundException e) {
            return;
        }

        model.setCharacters(game.getCharacters());
        model.setIslandGroups(game.getIslandGroups());
        SceneController.switchScene("jesterCardSelection.fxml");
        JesterCardSelectionSceneController controller = (JesterCardSelectionSceneController) SceneController.getCurrentSceneController();
        controller.setUpCharacterChoice(model);
    }
}
