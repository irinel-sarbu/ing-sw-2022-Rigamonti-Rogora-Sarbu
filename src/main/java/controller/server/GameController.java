package controller.server;

import controller.server.states.*;
import events.*;
import events.types.clientToServer.CreateGameEvent;
import events.types.clientToServer.JoinGameEvent;
import events.types.clientToServer.RegisterEvent;
import events.types.serverToClient.*;
import exceptions.GameNotFoundException;
import exceptions.MaxPlayersException;
import model.GameModel;
import model.Player;
import network.ClientConnection;
import network.Server;
import util.GameMode;
import util.Tuple;
import util.Wizard;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class GameController implements NetworkEventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    protected static final Map<String, GameLobby> games = new HashMap<>();
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

    public static GameLobby getGame(String code) throws GameNotFoundException {
        if (games.get(code) == null) throw new GameNotFoundException();
        return games.get(code);
    }

    public String addGame(int numOfPlayers, GameMode gameMode) {
        String code = codeGen();
        games.put(code, new GameLobby(numOfPlayers, gameMode, code));
        return code;
    }

    @Override
    public synchronized void onEvent(Tuple<Event, ClientConnection> networkEvent) {
        EventDispatcher dispatcher = new EventDispatcher(networkEvent);

        dispatcher.dispatch(EventType.REGISTER, (Tuple<Event, ClientConnection> t) -> onRegister((RegisterEvent) t.getKey(), t.getValue()));
        dispatcher.dispatch(EventType.CREATE_GAME, (Tuple<Event, ClientConnection> t) -> onCreateGame((CreateGameEvent) t.getKey(), t.getValue()));
        dispatcher.dispatch(EventType.JOIN_GAME, (Tuple<Event, ClientConnection> t) -> onJoinGame((JoinGameEvent) t.getKey(), t.getValue()));
    }

    private void broadcast(Event event, List<String> receivers, String excludedReceiver) {
        for (String playerName : receivers) {
            if (!playerName.equals(excludedReceiver)) {
                ClientConnection clientConnection = server.getClientConnectionByName(playerName);
                clientConnection.send(event);
            }
        }
    }

    // Handlers
    private boolean onRegister(RegisterEvent event, ClientConnection client) {
        if (server.getClientList().containsKey(event.getName())) {
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
        String clientName = server.getNameByClientConnection(client);

        try {
            games.get(code).getModel().addPlayer(new Player(clientName, Wizard.WIZARD_1));
        } catch (MaxPlayersException e) {
            e.printStackTrace();
        }

        LOGGER.info("Created lobby " + code);
        client.send(new GameCreatedEvent(code));
        return true;
    }

    private boolean onJoinGame(JoinGameEvent event, ClientConnection client) {
        String clientName = server.getNameByClientConnection(client);

        try {
            GameLobby lobby = getGame(event.getCode());
            lobby.getModel().addPlayer(new Player(clientName, Wizard.WIZARD_1));
            LOGGER.info(clientName + " joined lobby " + event.getCode());
            client.send(new GameJoinedEvent(event.getCode()));
            broadcast(new PlayerConnectedEvent(clientName), lobby.getModel().getPlayerNames(), clientName);
        } catch (GameNotFoundException e) {
            LOGGER.warning(clientName + " trying to connect to non existent lobby");
            client.send(new GameNotFoundEvent(event.getCode()));
        } catch (MaxPlayersException e) {
            LOGGER.warning(clientName + " trying to connect to full lobby");
        }

        return true;
    }
}
