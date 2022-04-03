package network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import events.Event;

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

    private synchronized void send(Event event) {
        try {
            out.writeObject(event);
        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public synchronized void asyncSend(Event event) {
        new Thread(() -> send(event)).start();
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();

            System.out.println("\rConnection with server closed...");
        } catch (IOException e) {
            System.err.println("[ERROR] " +e.getMessage());
        }
    }
}
