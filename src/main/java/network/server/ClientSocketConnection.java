package network.server;

import events.Event;
import events.EventType;
import events.types.Messages;
import events.types.serverToClient.Message;
import events.types.serverToClient.Ping;
import util.Logger;
import util.Tuple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSocketConnection extends Thread implements ClientConnection {
    private boolean isInLobby;
    private boolean isReady;

    private String lobbyCode;

    private final Timer pingTimer;

    private final LinkedBlockingQueue<Event> events;

    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    Socket socket;

    ClientSocketConnection(Server server, Socket socket) throws IOException {
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
                server.pushEvent(new Tuple<>(event, this));
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

    @Override
    public synchronized void send(Event event) {
        try {
            if (event.getType() != EventType.PING)
                Logger.info("Sending event " + event + " to " + socketToString());
            out.writeObject(event);
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
            server.pushEvent(new Tuple<>(new Message(Messages.CLIENT_DISCONNECTED), this));
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
