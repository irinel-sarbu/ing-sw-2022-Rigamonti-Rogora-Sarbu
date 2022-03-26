package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import events.Event;
import util.Tuple;

public class ClientConnection extends Thread {
    private final Logger LOGGER = Logger.getLogger(ClientConnection.class.getName());
    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    Socket socket;

    ClientConnection(Server server, Socket socket) throws IOException {
        LOGGER.info("New client connected!");
        this.server = server;
        this.socket = socket;

        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Event event = (Event) in.readObject();
                if (event == null)
                    disconnect();
                server.pushEvent(new Tuple<Event,ClientConnection>(event, this));
            } catch (IOException | ClassNotFoundException e) {
                disconnect();
            }
        }
    }

    public void write(Event event) {
        try {
            out.writeObject(event);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        server.onDisconnect(this);
    }
}
