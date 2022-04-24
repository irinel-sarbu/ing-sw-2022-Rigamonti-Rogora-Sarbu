package view.cli;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private final String name;
    private final String description;
    private final Map<String, ActionHandler> actionsMap;

    public Menu(String name, String description) {
        this.name = name;
        this.description = description;

        this.actionsMap = new LinkedHashMap<>();
    }

    public Menu(String name) {
        this.name = name;
        this.description = null;

        this.actionsMap = new LinkedHashMap<>();
    }

    public void putAction(String actionName, ActionHandler action) {
        actionsMap.put(actionName, action);
    }

    public boolean executeAction(int actionNumber) {
        if (actionNumber < 0 || actionNumber >= actionsMap.size()) {
            printError("Invalid input");
            return false;
        }

        List<ActionHandler> actions = new ArrayList<>(actionsMap.values());
        return actions.get(actionNumber).execute();
    }

    public void show() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(name);
        if (description != null) {
            stringBuilder.append(" : ").append(description);
        }
        stringBuilder.append("\n");

        List<String> actionNames = new ArrayList<>(actionsMap.keySet());
        for (int i = 0; i < actionNames.size(); i++) {
            String action = actionNames.get(i);
            stringBuilder.append(String.format("\t%2d - %s\n", i, action));
        }

        System.out.print(stringBuilder);
    }

    protected void printError(String message) {
        System.out.print("\u001B[31m"); // ANSI RED
        System.out.println("ERROR > " + message);
        System.out.print("\u001B[0m");  // ANSI RESET
    }
}
