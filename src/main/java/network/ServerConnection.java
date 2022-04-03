package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import events.Event;
import events.types.clientToServer.RegisterEvent;

public class ServerConnection extends Thread {
    private final Logger LOGGER = Logger.getLogger(ServerConnection.class.getName());

    ObjectInputStream in;
    ObjectOutputStream out;
    Client client;
    Socket socket;

    ServerConnection(Client client, Socket socket) throws IOException {
        this.client = client;
        this.socket = socket;

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Event event = (Event) in.readObject();
                if (event == null)
                    disconnect();
                client.pushEvent(event);
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.severe(e.getMessage());
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
    }
}
