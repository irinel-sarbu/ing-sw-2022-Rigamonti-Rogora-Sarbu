package view.gui.controllers.characterControllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EUseJesterEffect;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.board.Student;
import network.LightModel;
import view.gui.SceneController;
import view.gui.controllers.GenericSceneController;

import java.util.ArrayList;
import java.util.List;

public class MinstrelEntranceSceneController implements GenericSceneController {
    @FXML
    private ImageView student_0, student_1, student_2, student_3, student_4, student_5, student_6, student_7, student_8;

    private List<ImageView> students = new ArrayList<>();
    private List<Student> entrance = new ArrayList<>();
    private LightModel model;
    private List<Integer> entranceStudents;

    /**
     * Button controller
     */
    @FXML
    public void onStudent0(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(0).getID());
        students.get(0).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent1(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(1).getID());
        students.get(1).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent2(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(2).getID());
        students.get(2).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent3(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(3).getID());
        students.get(3).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent4(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(4).getID());
        students.get(4).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent5(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(5).getID());
        students.get(5).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent6(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(6).getID());
        students.get(6).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent7(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(7).getID());
        students.get(7).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    public void onStudent8(MouseEvent mouseEvent) {
        entranceStudents.add(entrance.get(8).getID());
        students.get(8).setVisible(false);
        if (entranceStudents.size() >= 2) {
            SceneController.switchScene("diningScene.fxml");
            DiningSelectorSceneController controller = (DiningSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setUpDining(model, entranceStudents);
        }
    }

    /**
     * method called to set up the scene and initialize the components
     */
    public void setUp(LightModel model) {
        this.model = model;
        this.entranceStudents = new ArrayList<>();
        students.add(student_0);
        students.add(student_1);
        students.add(student_2);
        students.add(student_3);
        students.add(student_4);
        students.add(student_5);
        students.add(student_6);
        students.add(student_7);
        students.add(student_8);

        entrance = model.getSchoolBoardMap().get(model.getPlayerName()).getEntranceStudents();

        for (int i = 0; i < entrance.size(); i++) {
            students.get(i).setImage(new Image("/Graphical_Assets/students/" + entrance.get(i).getColor().toString() + "Student.png"));
            students.get(i).setVisible(true);
        }
    }
}
