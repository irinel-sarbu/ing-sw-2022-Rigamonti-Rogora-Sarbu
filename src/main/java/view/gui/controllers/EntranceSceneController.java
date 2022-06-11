package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToDining;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.board.Student;
import network.LightModel;
import view.gui.SceneController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntranceSceneController implements GenericSceneController {
    @FXML
    private ImageView student_0, student_1, student_2, student_3, student_4, student_5, student_6, student_7, student_8;

    private List<ImageView> students = new ArrayList<>();
    private List<Student> entrance = new ArrayList<>();
    private LightModel model;
    private boolean toIsland;

    /**
     * selected student 0, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent0(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(0).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(0).getID()));
        }
    }

    /**
     * selected student 1, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent1(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(1).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(1).getID()));
        }
    }

    /**
     * selected student 2, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent2(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(2).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(2).getID()));
        }
    }

    /**
     * selected student 3, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent3(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(3).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(3).getID()));
        }
    }

    /**
     * selected student 4, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent4(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(4).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(4).getID()));
        }
    }

    /**
     * selected student 5, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent5(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(5).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(5).getID()));
        }
    }

    /**
     * selected student 6, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent6(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(6).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(6).getID()));
        }
    }

    /**
     * selected student 7, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent7(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(7).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(7).getID()));
        }
    }

    /**
     * selected student 8, switch to scene to select island
     * @param mouseEvent
     */
    @FXML
    public void onStudent8(MouseEvent mouseEvent) {
        if (toIsland) {
            SceneController.switchScene("islandSelection.fxml");
            IslandSelectorSceneController controller = (IslandSelectorSceneController) SceneController.getCurrentSceneController();
            controller.setupIslands(model, entrance.get(8).getID());
        } else {
            EventManager.notify(new EStudentMovementToDining(entrance.get(8).getID()));
        }
    }

    /**
     * setup entrance student selection
     * @param model reference to light model
     * @param playerName name of the player
     * @param toIsland {@link true} to move the student on the island, {@link false} otherwise
     */
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
            students.get(i).setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/students/" + entrance.get(i).getColor().toString() + "Student.png"))));
            students.get(i).setVisible(true);
        }
    }
}
