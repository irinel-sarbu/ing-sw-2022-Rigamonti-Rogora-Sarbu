package util;

import java.util.Arrays;
import java.util.Scanner;

public class CliHelper {
    private final Scanner consoleScanner;

    // ANSI Codes 256 bit
    private static final String ANSI_PREFIX = "\u001b[38;5;";
    private static final String CURSOR_UP = "\u001b[%sA";

    private static final String CODE_BLUE = "33";
    private static final String CODE_LIGHT_BLUE = "75";
    private static final String CODE_GRAY = "244";
    private static final String CODE_GREEN = "41";
    private static final String CODE_LIGHT_GREEN = "83";
    private static final String CODE_BROWN = "130";
    private static final String CODE_RED = "197";
    private static final String CODE_PURPLE = "99";
    private static final String CODE_ORANGE = "215";
    private static final String CODE_BLACK = "232";
    private static final String CODE_WHITE = "255";

    public static final String ANSI_BLUE = ANSI_PREFIX + CODE_BLUE + "m";
    public static final String ANSI_LIGHT_BLUE = ANSI_PREFIX + CODE_LIGHT_BLUE + "m";
    public static final String ANSI_GRAY = ANSI_PREFIX + CODE_GRAY + "m";
    public static final String ANSI_GREEN = ANSI_PREFIX + CODE_GREEN + "m";
    public static final String ANSI_LIGHT_GREEN = ANSI_PREFIX + CODE_LIGHT_GREEN + "m";
    public static final String ANSI_BROWN = ANSI_PREFIX + CODE_BROWN + "m";
    public static final String ANSI_PURPLE = ANSI_PREFIX + CODE_PURPLE + "m";
    public static final String ANSI_RED = ANSI_PREFIX + CODE_RED + "m";
    public static final String ANSI_ORANGE = ANSI_PREFIX + CODE_ORANGE + "m";
    public static final String ANSI_BLACK = ANSI_PREFIX + CODE_BLACK + "m";
    public static final String ANSI_WHITE = ANSI_PREFIX + CODE_WHITE + "m";

    public static final String ANSI_RESET = "\u001b[0m";
    public static final String CLEAN = "\u001b[0J";
    public static final String INITIALIZE_SCREEN = "\033[0;0H";

    public static final char NEW_LINE = '\n';
    public static final char BLANK = ' ';

    // Board
    protected static final char L_T_CORNER = '┌';
    protected static final char R_T_CORNER = '┐';
    protected static final char L_B_CORNER = '└';
    protected static final char R_B_CORNER = '┘';
    protected static final char H_LINE = '─';
    protected static final char V_LINE = '│';

    public CliHelper() {
        this.consoleScanner = new Scanner(System.in);
    }

    public String readString(String defaultValue) {
        String line = consoleScanner.nextLine();
        if (line.matches("[a-zA-Z\\d]+")) {
            return line;
        }

        return defaultValue;
    }

    public int readInt(int defaultValue) {
        String line = consoleScanner.nextLine();
        if (line.matches("\\d+")) {
            return Integer.parseInt(line);
        }

        return defaultValue;
    }

    /**
     * @param c char to repeat
     * @param n how many times to repeat
     * @return <code>String</code> made of <code>c</code>, <code>n</code> times
     */
    private String repeat(char c, int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be a positive integer");
        char[] string = new char[n];
        Arrays.fill(string, c);
        return new String(string);
    }

    public void drawBox(String color, String text) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(color);

        // Line 0
        stringBuilder.append(L_T_CORNER)
                .append(repeat(H_LINE, text.length() + 2))
                .append(R_T_CORNER)
                .append(NEW_LINE);

        // Line 1
        stringBuilder.append(V_LINE)
                .append(BLANK)
                .append(text)
                .append(BLANK)
                .append(V_LINE)
                .append(NEW_LINE);

        // Line 2
        stringBuilder.append(L_B_CORNER)
                .append(repeat(H_LINE, text.length() + 2))
                .append(R_B_CORNER)
                .append(NEW_LINE)
                .append(ANSI_RESET);

        System.out.println(stringBuilder);
    }

    /**
     * Cleans the terminal window from all the past data
     */
    public void resetScreen() {
        System.out.print(INITIALIZE_SCREEN);
        System.out.print(CLEAN);
        System.out.printf("Welcome to %sEryantis \uD83E\uDDDA \n", CliHelper.ANSI_ORANGE);
    }
}
