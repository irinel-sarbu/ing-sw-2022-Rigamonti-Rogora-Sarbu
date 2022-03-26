package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import events.Event;
import events.EventDispatcher;
import events.EventListener;
import events.EventType;
import events.types.network.ClientToServerInfoEvent;
import events.types.network.ServerACKEvent;

public class Client implements EventListener {
    private final Logger LOGGER = Logger.getLogger(Client.class.getName());
    
    private ServerConnection server;
    private LinkedBlockingQueue<Event> messages;
    private Socket socket;

    public Client(String IPAddress, int port) throws IOException {
        socket = new Socket(IPAddress, port);
        messages = new LinkedBlockingQueue<>();
        server = new ServerConnection(socket);

        Thread messageHandling = new Thread() {
            public void run() {
                while (true) {
                    try {
                        onEvent(messages.take());
                    } catch (InterruptedException e) {
                        LOGGER.severe("Interruption!");
                    }
                }
            }
        };
        messageHandling.start();
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);

        dispatcher.dispatch(EventType.SERVER_ACK, (Event e) -> onServerAck((ServerACKEvent) e));
    }

    public void send(Event obj) {
        server.write(obj);
    }

    // Handlers

    private boolean onServerAck(ServerACKEvent event) {
        LOGGER.info("New SERVER_ACK event: " + event.getMessage());
        return true;
    }

    private class ServerConnection {
        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;

        ServerConnection(Socket socket) throws IOException {
            LOGGER.info(">\tConnecting to server...");
            this.socket = socket;
            out = new ObjectOutputStream(this.socket.getOutputStream());
            
            LOGGER.info(">\tSending info to server...");
            write(new ClientToServerInfoEvent("Hey server, my name is irinel"));

            in = new ObjectInputStream(this.socket.getInputStream());

            Thread read = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            messages.put((Event) in.readObject());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };

            read.setDaemon(true);
            read.start();

            LOGGER.info("Connection enstablished");
        }

        private void write(Event event) {
            try {
                out.writeObject(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}