package eventSystem;

import eventSystem.annotations.EventHandler;
import eventSystem.annotations.NetworkEvent;
import events.Event;
import util.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final Map<Class<? extends Event>, CopyOnWriteArrayList<EventListener>> listenersMap;

    private static EventManager eventManagerInstance;

    private EventManager() {
        this.listenersMap = new HashMap<>();
    }

    public synchronized static EventManager get() {
        if (eventManagerInstance == null) {
            eventManagerInstance = new EventManager();
        }

        return eventManagerInstance;
    }

    public void register(final Object listenerInstance) {
        Logger.info("Registering event handlers for class " + listenerInstance.getClass().getName());
        for (Method method : listenerInstance.getClass().getMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) {
                Logger.warning("Ignoring method " + method.getName(), "No annotation found");
                continue;
            }

            int parameterCount = 1;

            if (method.isAnnotationPresent(NetworkEvent.class)) {
                parameterCount = 2;
            }

            if (method.getParameterCount() != parameterCount) {
                Logger.error("Ignoring event handler " + method.getName(), "Wrong number of arguments (required " + parameterCount + ")");
                continue;
            }

            if (!Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                Logger.error("Ignoring event handler " + method.getName(), "Argument must extend " + Event.class.getName());
                continue;
            }

            Class<?> eventType = method.getParameterTypes()[0];
            Logger.debug("Registering callback function " + method.getName() + " for " + eventType.getName());

            addListener(eventType, new EventListener(listenerInstance, method));
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void addListener(final Class<?> eventType, final EventListener listener) {
        if (!listenersMap.containsKey(eventType)) {
            listenersMap.put((Class<T>) eventType, new CopyOnWriteArrayList<>());
        }

        listenersMap.get(eventType).add(listener);
    }

    public void unregisterListener(final Object listener) {
        for (CopyOnWriteArrayList<EventListener> listenerList : listenersMap.values()) {
            for (int i = 0; i < listenerList.size(); i++) {
                if (listenerList.get(i).listenerInstance == listener) {
                    listenerList.remove(i);
                    i--;
                }
            }
        }
    }

    public <T extends Event> void unregisterListenersOfEvent(final Class<T> eventType) {
        listenersMap.get(eventType).clear();
    }

    public static void notify(final Event event) {
        get().dispatch(event);
    }

    private synchronized void dispatch(final Event event) {
        CopyOnWriteArrayList<EventListener> listeners = listenersMap.get(event.getClass());
        if (listeners != null) {
            for (EventListener listener : listeners) {
                try {
                    listener.callbackMethod.setAccessible(true);
                    listener.callbackMethod.invoke(listener.listenerInstance, event);
                } catch (InvocationTargetException e) {
                    Logger.error("Could not dispatch event to handler:", e.getMessage());
                } catch (IllegalAccessException e) {
                    Logger.warning("Could not access event handler method:", e.getMessage());
                }
            }
        }
    }

    private record EventListener(Object listenerInstance, Method callbackMethod) {
    }
}
