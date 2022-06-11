package view.gui.controllers.characterControllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.expert.CharacterCard;
import network.LightModel;
import util.CharacterType;
import view.gui.SceneController;
import view.gui.controllers.GenericSceneController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JesterCardSelectionSceneController implements GenericSceneController {
    @FXML
    private ImageView character_A, student_A_0, student_A_1, student_A_2, student_A_3, student_A_4, student_A_5;
    @FXML
    private Label coinsA;

    private CharacterCard jester;
    private List<Integer> chosenStudents;

    /**
     * Button controller
     */
    @FXML
    private void onStudent0(MouseEvent mouseEvent) {
        chosenStudents.add(jester.getStudents().get(0).getID());
        student_A.get(0).setVisible(false);
        if (chosenStudents.size() >= 3) {
            SceneController.switchScene("jesterEntranceScene.fxml");
            JesterEntranceSceneController controller = (JesterEntranceSceneController) SceneController.getCurrentSceneController();
            controller.setUp(model, chosenStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent1(MouseEvent mouseEvent) {
        chosenStudents.add(jester.getStudents().get(1).getID());
        student_A.get(1).setVisible(false);
        if (chosenStudents.size() >= 3) {
            SceneController.switchScene("jesterEntranceScene.fxml");
            JesterEntranceSceneController controller = (JesterEntranceSceneController) SceneController.getCurrentSceneController();
            controller.setUp(model, chosenStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent2(MouseEvent mouseEvent) {
        chosenStudents.add(jester.getStudents().get(2).getID());
        student_A.get(2).setVisible(false);
        if (chosenStudents.size() >= 3) {
            SceneController.switchScene("jesterEntranceScene.fxml");
            JesterEntranceSceneController controller = (JesterEntranceSceneController) SceneController.getCurrentSceneController();
            controller.setUp(model, chosenStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent3(MouseEvent mouseEvent) {
        chosenStudents.add(jester.getStudents().get(3).getID());
        student_A.get(3).setVisible(false);
        if (chosenStudents.size() >= 3) {
            SceneController.switchScene("jesterEntranceScene.fxml");
            JesterEntranceSceneController controller = (JesterEntranceSceneController) SceneController.getCurrentSceneController();
            controller.setUp(model, chosenStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent4(MouseEvent mouseEvent) {
        chosenStudents.add(jester.getStudents().get(4).getID());
        student_A.get(4).setVisible(false);
        if (chosenStudents.size() >= 3) {
            SceneController.switchScene("jesterEntranceScene.fxml");
            JesterEntranceSceneController controller = (JesterEntranceSceneController) SceneController.getCurrentSceneController();
            controller.setUp(model, chosenStudents);
        }
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent5(MouseEvent mouseEvent) {
        chosenStudents.add(jester.getStudents().get(5).getID());
        student_A.get(5).setVisible(false);
        if (chosenStudents.size() >= 3) {
            SceneController.switchScene("jesterEntranceScene.fxml");
            JesterEntranceSceneController controller = (JesterEntranceSceneController) SceneController.getCurrentSceneController();
            controller.setUp(model, chosenStudents);
        }
    }

    private List<Node> student_A;
    private LightModel model;

    /**
     * initializes the Lists of nodes
     */
    private void setCharactersUp() {
        student_A = new ArrayList<>();
        student_A.add(student_A_0);
        student_A.add(student_A_1);
        student_A.add(student_A_2);
        student_A.add(student_A_3);
        student_A.add(student_A_4);
        student_A.add(student_A_5);
    }

    /**
     * method called to set up the scene and initialize the components
     */
    public void setUpCharacterChoice(LightModel model) {
        this.model = model;
        chosenStudents = new ArrayList<>();
        setCharactersUp();

        for (CharacterCard character : model.getCharacters()) {
            if (character.getCharacter() == CharacterType.JESTER) {
                jester = character;
                loadCharacterPieces(coinsA, character_A, student_A);
            }
        }
    }

    /**
     * Loads the elements on the extracted character card
     */
    private void loadCharacterPieces(Label coinsTmp, ImageView character_Tmp, List<Node> student_Tmp) {
        coinsTmp.setText(String.valueOf(jester.getCost()));
        character_Tmp.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Personaggi/Character_" + jester.getCharacter().getNumber() + ".jpg"))));
        if (jester.getStudents() != null) {
            for (int i = 0; i < jester.getStudents().size(); i++) {
                ImageView student = (ImageView) student_Tmp.get(i);
                student.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/students/" + jester.getStudents().get(i).getColor().toString() + "StudentResized.png"))));
                student.setVisible(true);
            }
        }
    }
}
