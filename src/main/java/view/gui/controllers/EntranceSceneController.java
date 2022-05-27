package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToDining;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.board.Student;
import network.LightModel;

import java.util.ArrayList;
import java.util.List;

public class EntranceSceneController implements GenericSceneController {
    @FXML
    private ImageView student_0, student_1, student_2, student_3, student_4, student_5, student_6, student_7, student_8;

    private List<ImageView> students = new ArrayList<>();
    private List<Student> entrance = new ArrayList<>();
    private LightModel model;
    //TODO: add dynamic scene switch, if toIsland is true go to islandScene selector, otherwise Dining Color Scene Selector
    private boolean toIsland;

    @FXML
    public void onStudent0(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(0).getID()));
        }
    }

    @FXML
    public void onStudent1(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(1).getID()));
        }
    }

    @FXML
    public void onStudent2(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(2).getID()));
        }
    }

    @FXML
    public void onStudent3(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(3).getID()));
        }
    }

    @FXML
    public void onStudent4(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(4).getID()));
        }
    }

    @FXML
    public void onStudent5(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(5).getID()));
        }
    }

    @FXML
    public void onStudent6(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(6).getID()));
        }
    }

    @FXML
    public void onStudent7(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(7).getID()));
        }
    }

    @FXML
    public void onStudent8(MouseEvent mouseEvent) {
        if (toIsland) {

        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(8).getID()));
        }
    }

    public void setUp(LightModel model, String playerName, boolean toIsland) {
        this.model = model;
        this.toIsland = toIsland;
        students.add(student_0);
        students.add(student_1);
        students.add(student_2);
        students.add(student_3);
        students.add(student_4);
        students.add(student_5);
        students.add(student_6);
        students.add(student_7);
        students.add(student_8);

        entrance = model.getSchoolBoardMap().get(playerName).getEntranceStudents();

        for (int i = 0; i < entrance.size(); i++) {
            students.get(i).setImage(new Image("/Graphical_Assets/students/" + entrance.get(i).getColor().toString() + "Student.png"));
            students.get(i).setVisible(true);
        }
    }
}
