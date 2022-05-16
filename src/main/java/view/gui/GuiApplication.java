package view.gui;

import controller.client.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Logger;
import view.View;

import java.io.IOException;

public class GuiApplication extends Application {

    private ClientController clientController;
    private View guiView;

    @Override
    public void start(Stage stage) throws Exception {
        this.guiView = new GuiView();
        this.clientController = new ClientController(guiView);

        Parent root = null;
        FXMLLoader loader = new FXMLLoader();

        // TODO : change to loginScene.fxml
        loader.setLocation(getClass().getResource("/fxml/loginScene.fxml"));

        try {
            root = loader.load();
        } catch (IOException e) {
            Logger.severe(e.getMessage());
            System.exit(1);
        }

        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.setScene(scene);

        stage.show();
    }
}
