package controller.server;

import events.*;
import model.GameModel;
import util.GameMode;
import view.View;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public class GameController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final HashMap<String, GameModel> games;
    private final View view;

    public GameController(View view) {
        this.games = new HashMap<String, GameModel>();
        this.view = view;
    }

    public String codeGen() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();

        String codeGenerated = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        while (games.containsKey(codeGenerated)) {
            codeGenerated = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }

        return codeGenerated;
    }

    public void addGame(int numOfPlayers, GameMode gameMode){
        String code = codeGen();
        games.put(code, new GameModel(numOfPlayers, gameMode, code));
    }

    public GameModel getGame(String code){
        return games.get(code);
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
    }

}
