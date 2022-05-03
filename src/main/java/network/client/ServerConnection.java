package network.client;

import eventSystem.events.Event;
import util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection extends Thread {

    ObjectInputStream in;
    ObjectOutputStream out;
    Client client;
    Socket socket;

    ServerConnection(Client client, Socket socket) throws IOException {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (!socket.isClosed()) {
                Event event = (Event) in.readObject();
                client.pushEvent(event);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
    }

    public synchronized void send(Event event) {
        Logger.debug("Sending " + event + " to server");
        try {
            out.writeObject(event);
            out.flush();
            out.reset();
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();

            System.out.println("\rConnection with server closed...");
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }
}
