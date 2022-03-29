package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import events.Event;
import events.types.serverToServer.ClientDisconnect;
import events.types.serverToClient.PingEvent;
import util.Tuple;

public class ClientConnection extends Thread {
    private final Logger LOGGER = Logger.getLogger(ClientConnection.class.getName());

    private boolean isInLobby;
    private String lobbyCode;

    private final Timer pingTimer;

    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    Socket socket;

    ClientConnection(Server server, Socket socket) throws IOException {
        LOGGER.info("New client connected!");
        this.server = server;
        this.socket = socket;

        this.isInLobby = false;

        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());

        this.pingTimer = new Timer();
        TimerTask ping = new TimerTask() {
            @Override
            public void run() {
                send(new PingEvent());
            }
        };

        this.pingTimer.schedule(ping, 0, 5000);
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Event event = (Event) in.readObject();
                if (event == null)
                    disconnect();
                server.pushEvent(new Tuple<>(event, this));
            } catch (IOException | ClassNotFoundException e) {
                disconnect();
            }
        }

        System.out.println("Client thread stopping...");
    }

    @Override
    public String toString() {
        return socket.getInetAddress().toString();
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

    public void send(Event event) {
        try {
            System.out.println("Sending event " + event);
            out.writeObject(event);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
            pingTimer.cancel();

            server.pushEvent(new Tuple<>(new ClientDisconnect(), this));
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}
