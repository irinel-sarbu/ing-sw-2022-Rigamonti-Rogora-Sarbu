package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import events.Event;
import events.EventType;
import events.types.Messages;
import events.types.serverToClient.Message;
import events.types.serverToClient.PingEvent;
import util.Logger;
import util.Tuple;

public class ClientSocketConnection extends Thread implements ClientConnection {
    private boolean isInLobby;
    private String lobbyCode;

    private final Timer pingTimer;

    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    Socket socket;

    ClientSocketConnection(Server server, Socket socket) throws IOException {
        Logger.info("New client connected!");
        this.server = server;
        this.socket = socket;

        this.isInLobby = false;

        this.pingTimer = new Timer();
    }

    @Override
    public void run() {
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());

            TimerTask ping = new TimerTask() {
                @Override
                public void run() {
                    send(new PingEvent());
                }
            };

            this.pingTimer.schedule(ping, 0, 5000);

            while (!socket.isClosed()) {
                Event event = (Event) in.readObject();
                Logger.debug("New event " + event + " from " + socket.toString());
                server.pushEvent(new Tuple<>(event, this));
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
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

    private synchronized void send(Event event) {
        try {
            if(event.getType() != EventType.PING)
                Logger.debug("Sending event " + event + " to " + socket.toString());
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
            System.out.println("\rConnection with client " + socket.toString() + " closed...");
            server.pushEvent(new Tuple<>(new Message(Messages.CLIENT_DISCONNECTED), this));
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    @Override
    public synchronized void asyncSend(Event event) {
        new Thread(() -> send(event)).start();
    }

    @Override
    public String toString() {
        return socket.toString();
    }
}
