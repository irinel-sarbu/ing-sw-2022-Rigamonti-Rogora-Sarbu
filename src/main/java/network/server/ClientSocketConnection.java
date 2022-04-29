package network.server;

import eventSystem.events.Event;
import eventSystem.events.network.EConnectionAccepted;
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
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSocketConnection extends Thread {
    private final UUID clientIdentifier;

    private boolean isInLobby;
    private boolean isReady;

    private String lobbyCode;

    private final Timer pingTimer;

    private final LinkedBlockingQueue<Event> events;

    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    Socket socket;

    ClientSocketConnection(Server server, Socket socket, UUID clientIdentifier) throws IOException {
        Logger.info("New client connected " + socket);
        this.server = server;
        this.socket = socket;
        this.clientIdentifier = clientIdentifier;

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

            send(new EConnectionAccepted(clientIdentifier));

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
                server.pushEvent(event);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady() {
        isReady = true;
    }

    public boolean isInLobby() {
        return isInLobby;
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void joinLobby(String lobbyCode) {
        this.lobbyCode = lobbyCode;
        this.isInLobby = true;
    }

    public synchronized void send(Event event) {
        try {
            out.writeObject(event);
            out.flush();
            out.reset();
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

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
