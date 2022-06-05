package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.local.EUpdateServerInfo;
import eventSystem.events.network.client.ECreateLobbyRequest;
import eventSystem.events.network.client.EJoinLobbyRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import util.GameMode;;

public class CreateOrJoinSceneController implements GenericSceneController {
    @FXML
    private Button backButton;
    @FXML
    private Pane createOrJoinPane, createPane, createPane1, createPane2, joinPane, balloonText;
    @FXML
    private TextField textField;

    private GameMode gameMode;
    private int players;
    private String code;


    /**
     * setup create lobby view
     * @param mouseEvent
     */
    @FXML
    public void onCreate(MouseEvent mouseEvent) {
        createOrJoinPane.setVisible(false);
        createPane.setVisible(true);
        backButton.setVisible(true);
        createPane1.setVisible(true);
    }

    /**
     * setup join lobby view
     * @param mouseEvent
     */
    @FXML
    public void onJoin(MouseEvent mouseEvent) {
        createOrJoinPane.setVisible(false);
        joinPane.setVisible(true);
        backButton.setVisible(true);
    }

    /**
     * return to previous scene
     * @param mouseEvent
     */
    @FXML
    public void onBack(MouseEvent mouseEvent) {
        textField.setText("");
        createPane.setVisible(false);
        backButton.setVisible(false);
        createPane1.setVisible(false);
        createPane2.setVisible(false);
        joinPane.setVisible(false);
        balloonText.setVisible(false);
        createOrJoinPane.setVisible(true);
    }

    /**
     * selected normal mode
     * @param mouseEvent
     */
    @FXML
    public void onNormal(MouseEvent mouseEvent) {
        createPane1.setVisible(false);
        createPane2.setVisible(true);
        gameMode = GameMode.NORMAL;
    }

    /**
     * selected expert mode
     * @param mouseEvent
     */
    @FXML
    public void onExpert(MouseEvent mouseEvent) {
        createPane1.setVisible(false);
        createPane2.setVisible(true);
        gameMode = GameMode.EXPERT;
    }

    /**
     * selected 2 players
     * @param mouseEvent
     */
    @FXML
    public void onTwoPlayers(MouseEvent mouseEvent) {
        players = 2;
        EventManager.notify(new ECreateLobbyRequest(gameMode, players));
    }

    /**
     * selected 3 players
     * @param mouseEvent
     */
    @FXML
    public void onThreePlayers(MouseEvent mouseEvent) {
        players = 3;
        EventManager.notify(new ECreateLobbyRequest(gameMode, players));
    }

    /**
     * join lobby by code inserted
     * @param mouseEvent
     */
    @FXML
    public void onJoinConfirm(MouseEvent mouseEvent) {
        if (textField.getText().length() != 5) {
            balloonText.setVisible(true);
        } else {
            balloonText.setVisible(false);
            code = textField.getText();
            EventManager.notify(new EJoinLobbyRequest(code));
        }
    }
}
