package eventSystem;

import eventSystem.annotations.EventHandler;
import eventSystem.events.Event;
import util.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final Map<Class<? extends Event>, CopyOnWriteArrayList<EventListenerRecord>> listenersMap;

    private static EventManager eventManagerInstance;

    private EventManager() {
        this.listenersMap = new HashMap<>();
    }

    private synchronized static EventManager get() {
        if (eventManagerInstance == null) {
            eventManagerInstance = new EventManager();
        }

        return eventManagerInstance;
    }

    public static synchronized void register(EventListener listenerInstance, Filter filter) {
        Logger.info("Registering event handlers for class " + listenerInstance.getClass().getName());
        for (Method method : listenerInstance.getClass().getMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) {
                continue;
            }

            int parameterCount = 1;
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

            get().addListener(eventType, new EventListenerRecord(listenerInstance, method, filter));
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized <T extends Event> void addListener(Class<?> eventType, EventListenerRecord listener) {
        if (!listenersMap.containsKey(eventType)) {
            listenersMap.put((Class<T>) eventType, new CopyOnWriteArrayList<>());
        }

        listenersMap.get(eventType).add(listener);
    }

    public static synchronized void unregisterListener(final EventListener listener) {
        for (CopyOnWriteArrayList<EventListenerRecord> listenerList : get().listenersMap.values()) {
            for (int i = 0; i < listenerList.size(); i++) {
                if (listenerList.get(i).listenerInstance == listener) {
                    listenerList.remove(i);
                    i--;
                }
            }
        }
    }

    public static synchronized <T extends Event> void unregisterListenersOfEvent(Class<T> eventType) {
        get().listenersMap.get(eventType).clear();
    }

    public static void notify(final Event event) {
        get().dispatch(event);
    }

    private synchronized void dispatch(final Event event) {
        CopyOnWriteArrayList<EventListenerRecord> listeners = listenersMap.get(event.getClass());
        if (listeners != null) {
            for (EventListenerRecord listener : listeners) {
                if (listener.filter != null && !listener.filter.getScope().equals(event.getScope())) {
                    continue;
                }

                try {
                    listener.callbackMethod.setAccessible(true);
                    listener.callbackMethod.invoke(listener.listenerInstance, event);
                } catch (InvocationTargetException e) {
                    Logger.error("Could not dispatch event to handler: ", e.getMessage());
                    Logger.error("EventHandlers: ", List.of(listener).toString());
                } catch (IllegalAccessException e) {
                    Logger.warning("Could not access event handler method: ", e.getMessage());
                }
            }
        }
    }

    private record EventListenerRecord(Object listenerInstance, Method callbackMethod, Filter filter) {
    }
}
