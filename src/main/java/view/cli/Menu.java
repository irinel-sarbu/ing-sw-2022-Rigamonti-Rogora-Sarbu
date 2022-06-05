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

    /**
     * Menu constructor
     */
    public Menu(String name, String description) {
        this.name = name;
        this.description = description;

        this.actionsMap = new LinkedHashMap<>();
    }

    /**
     * Menu constructor overload with name of the menu
     */
    public Menu(String name) {
        this.name = name;
        this.description = null;

        this.actionsMap = new LinkedHashMap<>();
    }

    /**
     * puts an action in the actionsMap
     */
    public void putAction(String actionName, ActionHandler action) {
        actionsMap.put(actionName, action);
        size++;
    }

    /**
     * executes a chosen action
     */
    public boolean executeAction(int actionNumber) {
        if (actionNumber < 0 || actionNumber >= actionsMap.size()) {
            printError("Invalid input");
            return false;
        }
        consecutiveErrors = 0;

        List<ActionHandler> actions = new ArrayList<>(actionsMap.values());
        return actions.get(actionNumber).execute();
    }

    /**
     * Cli special print of the menu
     */
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

    /**
     * returns the size of the menu, used by cli when clear is needed
     */
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

    /**
     * prints an error message passed
     */
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

    /**
     * clears the wanted number of lines
     */
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
