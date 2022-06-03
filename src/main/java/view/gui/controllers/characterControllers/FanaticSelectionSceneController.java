package view.gui.controllers.characterControllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EUseFanaticEffect;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.expert.CharacterCard;
import network.LightModel;
import util.CharacterType;
import util.Color;
import view.gui.SceneController;
import view.gui.controllers.GenericSceneController;

import java.util.ArrayList;
import java.util.List;

public class FanaticSelectionSceneController implements GenericSceneController {
    @FXML
    private ImageView character_A, student_A_0, student_A_1, student_A_2, student_A_3, student_A_4;
    @FXML
    private Label coinsA;

    private CharacterCard fanatic;

    @FXML
    private void onStudent0(MouseEvent mouseEvent) {
        EventManager.notify(new EUseFanaticEffect(Color.BLUE));
    }

    @FXML
    private void onStudent1(MouseEvent mouseEvent) {
        EventManager.notify(new EUseFanaticEffect(Color.GREEN));
    }

    @FXML
    private void onStudent2(MouseEvent mouseEvent) {
        EventManager.notify(new EUseFanaticEffect(Color.PINK));
    }

    @FXML
    private void onStudent3(MouseEvent mouseEvent) {
        EventManager.notify(new EUseFanaticEffect(Color.RED));
    }

    @FXML
    private void onStudent4(MouseEvent mouseEvent) {
        EventManager.notify(new EUseFanaticEffect(Color.YELLOW));
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
    }

    public void setUpCharacterChoice(LightModel model) {
        this.model = model;
        setCharactersUp();

        for (CharacterCard character : model.getCharacters()) {
            if (character.getCharacter() == CharacterType.MUSHROOM_FANATIC) {
                fanatic = character;
                loadCharacter(coinsA, character_A);
            }
        }
    }

    private void loadCharacter(Label coinsTmp, ImageView character_Tmp) {
        coinsTmp.setText(String.valueOf(fanatic.getCost()));
        character_Tmp.setImage(new Image("/Graphical_Assets/Personaggi/Character_" + fanatic.getCharacter().getNumber() + ".jpg"));
    }
}
