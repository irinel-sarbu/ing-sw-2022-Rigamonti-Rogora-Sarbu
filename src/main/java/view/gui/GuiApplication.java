package view.gui;

import controller.client.ClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.View;

import java.util.Objects;


public class GuiApplication extends Application {
    private ClientController clientController;
    private View guiView;

    /**
     * used to start the GUI
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.guiView = new GuiView();
        this.clientController = new ClientController(guiView);
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/ui/Icon.png"))));
        SceneController.setStage(stage);
        SceneController.switchScene("loginScene.fxml");
        SceneController.startMediaPlayer();
        stage.show();
    }

    /**
     * used to stop the GUI
     */
    @Override
    public void stop() {
        SceneController.stopMediaPlayer();
        Platform.exit();
        System.exit(0);
    }
}
