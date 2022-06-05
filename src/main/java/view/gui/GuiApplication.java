package view.gui;

import controller.client.ClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.View;


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
    }

    @Override
    public void stop() {
        SceneController.stopMediaPlayer();
        Platform.exit();
        System.exit(0);
    }
}
