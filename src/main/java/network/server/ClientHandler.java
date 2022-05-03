package network.server;

import eventSystem.events.Event;
import eventSystem.events.network.ERegister;
import eventSystem.events.network.Messages;
import eventSystem.events.network.server.Ping;
import eventSystem.events.network.server.ServerMessage;
import util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler extends Thread implements IClientHandler {
    private boolean isInLobby;
    private boolean isReady;

    private String lobbyCode;

    private final Timer pingTimer;

    private final LinkedBlockingQueue<Event> events;

    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    Socket socket;

    public ClientHandler(Server server, Socket socket) throws IOException {
        Logger.info("New client connected " + socket);
        this.server = server;
        this.socket = socket;

        this.isInLobby = false;
        this.isReady = false;

        this.pingTimer = new Timer();

        events = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());

            TimerTask ping = new TimerTask() {
                @Override
                public void run() {
                    send(new Ping());
                }
            };

            this.pingTimer.schedule(ping, 0, 5000);

            while (!socket.isClosed()) {
                Event event = (Event) in.readObject();
                Logger.info("New event " + event + " from " + socketToString());

                if (event instanceof ERegister) {
                    if (server.checkPlayerIsRegistered(((ERegister) event).getNickname())) {
                        send(new ServerMessage(Messages.NAME_NOT_AVAILABLE));
                        continue;
                    }

                    server.register(((ERegister) event).getNickname().toLowerCase(), this);
                    send(new ServerMessage(Messages.REGISTRATION_OK));

                    continue;
                }
                server.pushEvent(event);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public void setReady() {
        isReady = true;
    }

    @Override
    public boolean isInLobby() {
        return isInLobby;
    }

    @Override
    public String getLobbyCode() {
        return lobbyCode;
    }

    @Override
    public void joinLobby(String lobbyCode) {
        this.lobbyCode = lobbyCode;
        this.isInLobby = true;
    }

    @Override
    public synchronized void send(Event event) {
        if (!(event instanceof Ping))
            Logger.debug("Sending " + event + " to " + this);
        try {
            out.writeObject(event);
            out.flush();
            out.reset();
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
            pingTimer.cancel();
            Logger.info("Connection with client " + socketToString() + " closed...");
            server.pushEvent(new ServerMessage(Messages.CLIENT_DISCONNECTED));
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    private String socketToString() {
        return socket.getInetAddress() + ":" + socket.getPort();
    }

    @Override
    public String toString() {
        return socketToString();
    }
}
