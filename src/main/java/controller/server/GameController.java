package controller.server;

import controller.server.states.*;
import events.*;
import events.types.clientToServer.CreateGameEvent;
import events.types.clientToServer.RegisterEvent;
import events.types.serverToClient.GameCreatedEvent;
import events.types.serverToClient.PlayerNameTakenEvent;
import events.types.serverToClient.RegistrationOkEvent;
import exceptions.GameNotFoundException;
import model.GameModel;
import network.ClientConnection;
import network.Server;
import util.GameMode;
import util.Tuple;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public class GameController implements NetworkEventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final HashMap<String, GameModel> games;
    private final Server server;

    private final TurnEpilogue epilogue;
    private final StudentMovement studentMovement;
    private final ResolveIsland resolveIsland;
    private final PlanningPhase planningPhase;
    private final MotherNatureMovement motherNatureMovement;
    private final GameOver gameOver;
    private final CharacterEffectHandler characterEffectHandler;

    public GameController(Server server) {
        this.server = server;

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

    public String addGame(int numOfPlayers, GameMode gameMode) {
        String code = codeGen();
        games.put(code, new GameModel(numOfPlayers, gameMode));
        return code;
    }

    public GameModel getGame(String code) throws GameNotFoundException {
        if (games.get(code) == null) throw new GameNotFoundException();
        return games.get(code);
    }

    @Override
    public synchronized void onEvent(Tuple<Event, ClientConnection> networkEvent) {
        EventDispatcher dispatcher = new EventDispatcher(networkEvent);

        dispatcher.dispatch(EventType.REGISTER, (Tuple<Event, ClientConnection> t) -> onRegister((RegisterEvent) t.getKey(), t.getValue()));
        dispatcher.dispatch(EventType.CREATE_GAME, (Tuple<Event, ClientConnection> t) -> onCreateGame((CreateGameEvent) t.getKey(), t.getValue()));
    }

    // Handlers
    private boolean onRegister(RegisterEvent event, ClientConnection client) {
        if(server.getClientList().containsKey(event.getName())) {
            LOGGER.info("Trying to register again " + event.getName() + ". Sending ERROR to client.");
            client.send(new PlayerNameTakenEvent());
        } else {
            LOGGER.info("Registering: " + event.getName());
            server.getClientList().put(event.getName(), client);
            client.send(new RegistrationOkEvent());
        }

        return true;
    }

    private boolean onCreateGame(CreateGameEvent event, ClientConnection client) {
        String code = addGame(event.getNumOfPlayers(), event.getGameMode());
        LOGGER.info("Created lobby " + code);
        client.send(new GameCreatedEvent(code));
        return true;
    }
}
