package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EAssistantChosen;
import exceptions.AssistantNotInDeckException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.board.Assistant;
import util.Logger;
import view.gui.SceneController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssistantChoiceSceneController implements GenericSceneController {
    @FXML
    private Pane assistants, assistantChosen, otherChoicesPane;
    @FXML
    private Label assistantLabel, chooseAssistantLabel, assistantChosenLabel, nothingLabel;
    @FXML
    private ImageView chosenAssistantImage, otherPlayer1, otherPlayer2, otherPlayer3;
    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10;

    private List<Assistant> deck;
    private List<Integer> otherAssistantValues = new ArrayList<>();

    /**
     * Choose assistant 1 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant1(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_1.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(1)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 2 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant2(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_2.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(2)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 3 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant3(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_3.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(3)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 4 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant4(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_4.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(4)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 5 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant5(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_5.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(5)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 6 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant6(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_6.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(6)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 7 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant7(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_7.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(7)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 8 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant8(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_8.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(8)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 9 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant9(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_9.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(9)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Choose assistant 10 using mouse if available
     * @param mouseEvent
     */
    @FXML
    public void onAssistant10(MouseEvent mouseEvent) {
        chosenAssistantImage.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_10.png"))));
        try {
            EventManager.notify(new EAssistantChosen(findAssistant(10)));
        } catch (AssistantNotInDeckException e) {
            Logger.warning("Assistant chosen is not in available assistants");
        }
        showChosenAssistant();
    }

    /**
     * Display available assistants
     * @param deck list of assistants
     */
    public void showAssistants(List<Assistant> deck) {
        this.deck = deck;
        assistantLabel.setVisible(false);
        assistantChosen.setVisible(false);
        otherChoicesPane.setVisible(true);
        resetAssistants();

        for (Assistant assistant : deck) {
            switch (assistant.getValue()) {
                case 1 -> assistant1.setVisible(true);
                case 2 -> assistant2.setVisible(true);
                case 3 -> assistant3.setVisible(true);
                case 4 -> assistant4.setVisible(true);
                case 5 -> assistant5.setVisible(true);
                case 6 -> assistant6.setVisible(true);
                case 7 -> assistant7.setVisible(true);
                case 8 -> assistant8.setVisible(true);
                case 9 -> assistant9.setVisible(true);
                case 10 -> assistant10.setVisible(true);
            }
        }
        assistants.setVisible(true);
    }

    /**
     * Display assistants selected by other players
     * @param assistantValue
     */
    //IMPORTANT! DO NOT CHANGE NAME OF ASSISTANT CARDS
    public void updateOtherChoices(int assistantValue) {
        switch (otherAssistantValues.size()) {
            case 0 -> {
                nothingLabel.setVisible(false);
                otherAssistantValues.add(assistantValue);
                otherPlayer1.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_" + assistantValue + ".png"))));
            }
            case 1 -> {
                otherAssistantValues.add(assistantValue);
                otherPlayer2.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_" + assistantValue + ".png"))));
            }
            case 2 -> {
                otherAssistantValues.add(assistantValue);
                otherPlayer3.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Assistenti/2x/Assistente_" + assistantValue + ".png"))));
            }
        }
    }

    /**
     * Display chosen assistant
     */
    public void showChosenAssistant() {
        otherChoicesPane.setVisible(false);
        assistants.setVisible(false);
        assistantChosen.setVisible(true);
    }

    /**
     * Reset assistants visibility
     */
    private void resetAssistants() {
        assistant1.setVisible(false);
        assistant2.setVisible(false);
        assistant3.setVisible(false);
        assistant4.setVisible(false);
        assistant5.setVisible(false);
        assistant6.setVisible(false);
        assistant7.setVisible(false);
        assistant8.setVisible(false);
        assistant9.setVisible(false);
        assistant10.setVisible(false);
    }

    /**
     * Get assistant by value
     * @param value to search
     * @return Reference to the assistant
     * @throws AssistantNotInDeckException if no assistant with the value is present inside the deck
     */
    private Assistant findAssistant(int value) throws AssistantNotInDeckException {
        for (Assistant assistant : deck) {
            if (assistant.getValue() == value) return assistant;
        }
        throw new AssistantNotInDeckException();
    }
}
