package view.cli;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private int consecutiveErrors = 0;
    private final String name;
    private final String description;
    private final Map<String, ActionHandler> actionsMap;
    private int size = 0;

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
        size++;
    }

    public boolean executeAction(int actionNumber) {
        if (actionNumber < 0 || actionNumber >= actionsMap.size()) {
            printError("Invalid input");
            return false;
        }
        consecutiveErrors = 0;

        List<ActionHandler> actions = new ArrayList<>(actionsMap.values());
        return actions.get(actionNumber).execute();
    }

    public void show() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);

        if (description != null) {
            stringBuilder.append(" • ").append(description);
        }
        stringBuilder.append("\n");

        List<String> actionNames = new ArrayList<>(actionsMap.keySet());
        for (int i = 0; i < actionNames.size(); i++) {
            String action = actionNames.get(i);
            stringBuilder.append(String.format("  %2d - %s\n", i, action));
        }

        System.out.println(stringBuilder);
    }

    public int getSize() {
        int total = 0;
        total += 1; // Title and description
        total += actionsMap.size(); // Number of options
        total += 1; // Spacer
        if (consecutiveErrors > 0)
            total += 1; // Error
        total += 1; // Input
        return total;
    }

    protected void printError(String message) {
        consecutiveErrors++;
        if (consecutiveErrors > 1)
            clearLines(2);
        else
            clearLines(1);
        System.out.print("\u001B[31m"); // ANSI RED
        System.out.print("ERROR > " + message + ", consecutive errors = " + consecutiveErrors + ", actionSize = " + actionsMap.size());
        System.out.print("\u001B[0m\n");  // ANSI RESET
    }

    private void clearLines(int numOfLines) {
        if (numOfLines <= 0) return;
        for (int i = 0; i < numOfLines; i++) {
            // Set cursor at start line
            System.out.print("\r");
            // Clear line
            System.out.print("\033[2K");
            // Move up by one
            System.out.printf("\033[%dA", 1);
            // Clear line
            System.out.print("\033[2K");
        }
    }
}
