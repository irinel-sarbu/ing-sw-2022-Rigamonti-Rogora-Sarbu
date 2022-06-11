package view.gui.controllers.characterControllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EUseFanaticEffect;
import eventSystem.events.network.client.EUseThiefEffect;
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
import java.util.Objects;

public class ThiefSelectionSceneController implements GenericSceneController {
    @FXML
    private ImageView character_A, student_A_0, student_A_1, student_A_2, student_A_3, student_A_4;
    @FXML
    private Label coinsA;

    private CharacterCard thief;

    /**
     * Button controller
     */
    @FXML
    private void onStudent0(MouseEvent mouseEvent) {
        EventManager.notify(new EUseThiefEffect(Color.BLUE));
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent1(MouseEvent mouseEvent) {
        EventManager.notify(new EUseThiefEffect(Color.GREEN));
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent2(MouseEvent mouseEvent) {
        EventManager.notify(new EUseThiefEffect(Color.PINK));
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent3(MouseEvent mouseEvent) {
        EventManager.notify(new EUseThiefEffect(Color.RED));
    }

    /**
     * Button controller
     */
    @FXML
    private void onStudent4(MouseEvent mouseEvent) {
        EventManager.notify(new EUseThiefEffect(Color.YELLOW));
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
    }

    /**
     * method called to set up the scene and initialize the components
     */
    public void setUpCharacterChoice(LightModel model) {
        this.model = model;
        setCharactersUp();

        for (CharacterCard character : model.getCharacters()) {
            if (character.getCharacter() == CharacterType.THIEF) {
                thief = character;
                loadCharacter(coinsA, character_A);
            }
        }
    }

    /**
     * Loads the elements on the extracted character card
     */
    private void loadCharacter(Label coinsTmp, ImageView character_Tmp) {
        coinsTmp.setText(String.valueOf(thief.getCost()));
        character_Tmp.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Personaggi/Character_" + thief.getCharacter().getNumber() + ".jpg"))));
    }
}
