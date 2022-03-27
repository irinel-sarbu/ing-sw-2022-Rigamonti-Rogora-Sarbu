package controller.server;

import controller.server.states.*;
import events.*;
import exceptions.GameNotFoundException;
import model.GameModel;
import util.GameMode;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public class GameController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final HashMap<String, GameModel> games;

    private final TurnEpilogue epilogue;
    private final StudentMovement studentMovement;
    private final ResolveIsland resolveIsland;
    private final PlanningPhase planningPhase;
    private final MotherNatureMovement motherNatureMovement;
    private final GameOver gameOver;
    private final CharacterEffectHandler characterEffectHandler;

    public GameController() {
        this.games = new HashMap<>();
        this.epilogue = new TurnEpilogue();
        this.studentMovement = new StudentMovement();
        this.resolveIsland = new ResolveIsland();
        this.planningPhase = new PlanningPhase();
        this.motherNatureMovement = new MotherNatureMovement();
        this.gameOver = new GameOver();
        this.characterEffectHandler = new CharacterEffectHandler();
    }

    private String codeGen() {
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

    public String addGame(int numOfPlayers, GameMode gameMode){
        String code = codeGen();
        games.put(code, new GameModel(numOfPlayers, gameMode, code));
        return code;
    }

    public GameModel getGame(String code) throws GameNotFoundException {
        if (games.get(code) == null) throw new GameNotFoundException();
        return games.get(code);
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
    }
}
