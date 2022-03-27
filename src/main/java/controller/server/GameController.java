package controller.server;

import controller.server.states.*;
import events.*;
import exceptions.GameNotFoundException;
import util.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class GameController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private static final Map<String, GameLobby> games = new HashMap<>();

    private final TurnEpilogue epilogue;
    private final StudentMovement studentMovement;
    private final ResolveIsland resolveIsland;
    private final PlanningPhase planningPhase;
    private final MotherNatureMovement motherNatureMovement;
    private final GameOver gameOver;
    private final CharacterEffectHandler characterEffectHandler;

    public GameController() {
        this.epilogue = new TurnEpilogue();
        this.studentMovement = new StudentMovement();
        this.resolveIsland = new ResolveIsland();
        this.planningPhase = new PlanningPhase();
        this.motherNatureMovement = new MotherNatureMovement();
        this.gameOver = new GameOver();
        this.characterEffectHandler = new CharacterEffectHandler();
    }

    private String codeGen() {
        int leftLimit = '0'; // numeral '0'
        int rightLimit = 'z'; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        String codeGenerated;

        do {
            codeGenerated = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= '9' || i >= 'A') && (i <= 'Z' || i >= 'a'))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (games.containsKey(codeGenerated));

        return codeGenerated;
    }

    public String addGame(int numOfPlayers, GameMode gameMode) {
        String code = codeGen();
        games.put(code, new GameLobby(numOfPlayers, gameMode, code));
        return code;
    }

    public GameLobby getGame(String code) throws GameNotFoundException {
        if (games.get(code) == null) throw new GameNotFoundException();
        return games.get(code);
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
    }

}
