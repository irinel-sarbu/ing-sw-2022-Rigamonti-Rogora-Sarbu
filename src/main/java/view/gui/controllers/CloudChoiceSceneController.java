package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.actionPhaseRelated.ESelectRefillCloud;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.board.Student;
import network.LightModel;
import util.Color;

import java.util.ArrayList;
import java.util.List;

public class CloudChoiceSceneController implements GenericSceneController {

    LightModel model;

    String pathPrefix = "/Graphical_Assets/";

    @FXML
    private GridPane realm_parent;
    @FXML
    private Pane cloudButtons;

    private List<Node> clouds;
    private List<Node> cloudButtonsList;

    private int numOfClouds;

    @FXML
    public void onCloud0(MouseEvent mouseEvent) {
        EventManager.notify(new ESelectRefillCloud(0));
    }

    @FXML
    public void onCloud1(MouseEvent mouseEvent) {
        EventManager.notify(new ESelectRefillCloud(1));
    }

    @FXML
    public void onCloud2(MouseEvent mouseEvent) {
        EventManager.notify(new ESelectRefillCloud(2));
    }

    private void init(LightModel model) {
        this.model = model;
        numOfClouds = model.getCloudTiles().size();
        clouds = new ArrayList<>(realm_parent.getChildren().subList(0, 3));
        cloudButtonsList = new ArrayList<>(cloudButtons.getChildren());
    }

    // Display functions
    private void updateClouds(int cloudID, List<Color> studentList) {
        AnchorPane cloud = (AnchorPane) clouds.get(cloudID);
        List<Node> students = new ArrayList<>(cloud.getChildren().subList(1, 5));
        for (int i = 0; i < studentList.size(); i++) {
            (students.get(i)).setVisible(true);
            ((ImageView) students.get(i)).setImage(new Image(pathPrefix + "students/" + studentList.get(i) + "Student.png"));
        }
    }

    private void resetClouds() {
        for (int i = 0; i < 3; i++) {
            AnchorPane cloud = (AnchorPane) clouds.get(i);
            cloud.setVisible(i < numOfClouds);
        }
    }

    public void setupClouds(LightModel model) {
        init(model);
        resetClouds();
        for (int i = 0; i < numOfClouds; i++) {
            updateClouds(i, model.getCloudTiles().get(i).getStudents().stream().map(Student::getColor).toList());
            Button button = (Button) cloudButtonsList.get(i);
            if (model.getCloudTiles().get(i).getStudents().size() > 0 || model.isLastRound()) {
                button.setVisible(true);
            }
        }
    }
}
