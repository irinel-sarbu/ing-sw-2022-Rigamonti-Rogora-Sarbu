# Esempio di implementazione

Il seguente è un esempio di implementazione delle classi che coinvolgono l'Event System

```java
public class EventManager {
    public static synchronized void register(EventListener listenerInstance) {
        // Register function implementation
    }

    public static synchronized void notify(final Event event) {
        // Notify function implementation
    }
    
    // Rest of implementation
}
```

```java
public class Message extends Event {
    private final String message;

    public Message(final String message) {
        this.message = message;
    }

    public String content() {
        return message;
    }
}
```

```java
public class ExampleListener implements EventListener {
    public ExampleListener() {
        EventManager.register(this);
    }

    @EventHandler
    public void onMessage(Message message) {
        System.out.printf("Received new message: %s\n", message.content());
    }
}
```

```java
public class Main {
    public static void main(String... args) {
        ExampleListener listener = new ExampleListener();
        EventManager.notify(new Message("test"));
    }
}
```

Eseguendo il codice si avrà il seguente risultato

```
Received new message: test
```