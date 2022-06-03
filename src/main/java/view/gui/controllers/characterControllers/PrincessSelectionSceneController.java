package view.gui.controllers.characterControllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EUsePrincessEffect;
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

public class PrincessSelectionSceneController implements GenericSceneController {
    @FXML
    private ImageView character_A, student_A_0, student_A_1, student_A_2, student_A_3, student_A_4, student_A_5;
    @FXML
    private Label coinsA;

    private CharacterCard princess;

    @FXML
    private void onStudent0(MouseEvent mouseEvent) {
        EventManager.notify(new EUsePrincessEffect(princess.getStudents().get(0).getID()));
    }

    @FXML
    private void onStudent1(MouseEvent mouseEvent) {
        EventManager.notify(new EUsePrincessEffect(princess.getStudents().get(1).getID()));
    }

    @FXML
    private void onStudent2(MouseEvent mouseEvent) {
        EventManager.notify(new EUsePrincessEffect(princess.getStudents().get(2).getID()));
    }

    @FXML
    private void onStudent3(MouseEvent mouseEvent) {
        EventManager.notify(new EUsePrincessEffect(princess.getStudents().get(3).getID()));
    }

    private List<Node> student_A;
    private LightModel model;

    private void setCharactersUp() {
        student_A = new ArrayList<>();
        student_A.add(student_A_0);
        student_A.add(student_A_1);
        student_A.add(student_A_2);
        student_A.add(student_A_3);
        student_A.add(student_A_4);
        student_A.add(student_A_5);
    }

    public void setUpCharacterChoice(LightModel model) {
        this.model = model;
        setCharactersUp();

        for (CharacterCard character : model.getCharacters()) {
            if (character.getCharacter() == CharacterType.PRINCESS) {
                princess = character;
                loadCharacterPieces(coinsA, character_A, student_A);
            }
        }
    }

    private void loadCharacterPieces(Label coinsTmp, ImageView character_Tmp, List<Node> student_Tmp) {
        coinsTmp.setText(String.valueOf(princess.getCost()));
        character_Tmp.setImage(new Image("/Graphical_Assets/Personaggi/Character_" + princess.getCharacter().getNumber() + ".jpg"));
        if (princess.getStudents() != null) {
            for (int i = 0; i < princess.getStudents().size(); i++) {
                ImageView student = (ImageView) student_Tmp.get(i);
                student.setImage(new Image("/Graphical_Assets/students/" + princess.getStudents().get(i).getColor().toString() + "StudentResized.png"));
                student.setVisible(true);
            }
        }
    }
}
