package view.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = null;
//        FXMLLoader loader = new FXMLLoader();
//
//        // TODO Create login.fxml
//        loader.setLocation(getClass().getResource("/fxml/login.fxml"));
//
//        try {
//            rootLayout = loader.load();
//        } catch (IOException e) {
//            Logger.severe(e.getMessage());
//            System.exit(1);
//        }
//        Scene scene = new Scene(rootLayout);

        // TEMP
        Group root = new Group();

        Scene scene = new Scene(root);
        stage.setHeight(500);
        stage.setWidth(500);
        stage.setResizable(false);
        stage.setTitle("Eryantis Board Game");
        stage.setScene(scene);

        stage.show();
    }
}
