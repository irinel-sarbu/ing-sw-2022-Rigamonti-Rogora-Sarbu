package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import events.Event;
import util.Logger;
import util.Tuple;

public class ClientConnection extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    Socket socket;

    ClientConnection(Server server, Socket socket) throws IOException {
        Logger.info("New client connected!");
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
                server.pushEvent(new Tuple<Event, ClientConnection>(event, this));
            } catch (IOException | ClassNotFoundException e) {
                disconnect();
            }
        }
    }

    public void send(Event event) {
        try {
            out.writeObject(event);
        } catch (IOException e) {
            Logger.severe(e.getMessage());
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
            Logger.severe(e.getMessage());
        }

        server.onDisconnect(this);
    }
}
