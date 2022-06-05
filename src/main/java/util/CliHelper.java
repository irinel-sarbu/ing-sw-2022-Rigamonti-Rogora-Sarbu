package util;

import java.util.Arrays;
import java.util.Scanner;

public class CliHelper {
    private final Scanner consoleScanner;

    // ANSI Codes 256 bit
    private static final String ANSI_PREFIX = "\u001b[38;5;";
    private static final String CURSOR_UP = "\u001b[%sA";

    private static final String CODE_RED = "196";
    private static final String CODE_GREEN = "34";
    private static final String CODE_YELLOW = "220";
    private static final String CODE_GOLD = "214";
    private static final String CODE_BROWN = "130";
    private static final String CODE_BLUE = "39";
    private static final String CODE_PURPLE = "99";
    private static final String CODE_PINK = "218";
    private static final String CODE_GRAY = "244";
    private static final String CODE_ORANGE = "215";
    private static final String CODE_BLACK = "232";
    private static final String CODE_WHITE = "255";

    public static final String ANSI_BLUE = ANSI_PREFIX + CODE_BLUE + "m";
    public static final String ANSI_GRAY = ANSI_PREFIX + CODE_GRAY + "m";
    public static final String ANSI_GREEN = ANSI_PREFIX + CODE_GREEN + "m";
    public static final String ANSI_PURPLE = ANSI_PREFIX + CODE_PURPLE + "m";
    public static final String ANSI_RED = ANSI_PREFIX + CODE_RED + "m";
    public static final String ANSI_YELLOW = ANSI_PREFIX + CODE_YELLOW + "m";
    public static final String ANSI_GOLD = ANSI_PREFIX + CODE_GOLD + "m";
    public static final String ANSI_ORANGE = ANSI_PREFIX + CODE_ORANGE + "m";
    public static final String ANSI_PINK = ANSI_PREFIX + CODE_PINK + "m";
    public static final String ANSI_BLACK = ANSI_PREFIX + CODE_BLACK + "m";
    public static final String ANSI_WHITE = ANSI_PREFIX + CODE_WHITE + "m";
    public static final String ANSI_BROWN = ANSI_PREFIX + CODE_BROWN + "m";

    public static final String ANSI_RESET = "\u001b[0m";
    public static final String INITIALIZE_SCREEN = "\033[H\033[2J";

    public static final char NEW_LINE = '\n';
    public static final char BLANK = ' ';

    // Board
    public static final char L_T_CORNER = '╭';
    public static final char R_T_CORNER = '╮';
    public static final char L_B_CORNER = '╰';
    public static final char R_B_CORNER = '╯';

    public static final char H_LINE = '─';
    public static final char H_T_EDGE = '┴';
    public static final char H_B_EDGE = '┬';

    public static final char V_LINE = '│';
    public static final char V_L_EDGE = '┤';
    public static final char V_R_EDGE = '├';

    /**
     * constructor of CliHelper
     */
    public CliHelper() {
        this.consoleScanner = new Scanner(System.in);
    }

    public String readString(String defaultValue) {
        String line = consoleScanner.nextLine();
        if (line.matches("[a-zA-Z\\d.]+")) {
            return line;
        }

        return defaultValue;
    }

    /**
     * reads an int from consoleScanner. If it can't, it will return defaultValue
     *
     * @param defaultValue the default value
     * @return the int /default value
     */
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
    public static String repeat(char c, int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be a positive integer");
        char[] string = new char[n];
        Arrays.fill(string, c);
        return new String(string);
    }

    /**
     * draws a box
     */
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
        System.out.flush();
        System.out.printf("Welcome to %sEryantis \uD83E\uDDDA \n", CliHelper.ANSI_ORANGE);
    }

    /**
     * Prints the colored icon based on a student color
     *
     * @param color is the student color
     * @return the ANSI colored dot
     */
    public static String getStudentIcon(Color color) {
        String studentColor;
        switch (color) {
            case BLUE -> studentColor = ANSI_BLUE;

            case RED -> studentColor = ANSI_RED;

            case GREEN -> studentColor = ANSI_GREEN;

            case PINK -> studentColor = ANSI_PINK;

            case YELLOW -> studentColor = ANSI_YELLOW;

            default -> studentColor = ANSI_WHITE;
        }

        return studentColor + '●' + ANSI_RESET;
    }

    /**
     * Prints the colored icon based on a professor color
     *
     * @param color is the professor color
     * @return the ANSI colored square
     */
    public static String getProfessorIcon(Color color) {
        String professorColor;
        switch (color) {
            case BLUE -> professorColor = ANSI_BLUE;

            case RED -> professorColor = ANSI_RED;

            case GREEN -> professorColor = ANSI_GREEN;

            case PINK -> professorColor = ANSI_PINK;

            case YELLOW -> professorColor = ANSI_YELLOW;

            default -> professorColor = ANSI_WHITE;
        }

        return professorColor + '■' + ANSI_RESET;
    }

    /**
     * Prints the colored icon based on a tower color
     *
     * @param color is the tower color
     * @return the ANSI colored triangle
     */
    public static String getTowerIcon(TowerColor color) {
        String towerColor = ANSI_WHITE;
        switch (color) {
            case BLACK -> towerColor = ANSI_BLACK;
            case WHITE -> towerColor = ANSI_WHITE;
            case GRAY -> towerColor = ANSI_GRAY;
        }

        return towerColor + '▲' + ANSI_RESET;
    }

    /**
     * Prints the colored icon of mother nature
     *
     * @return the ANSI colored brown triangle
     */
    public static String getMotherNatureIcon() {
        return ANSI_BROWN + '▲' + ANSI_RESET;
    }

    /**
     * Prints the icon representing the noEntryTile
     */
    public static String getNoEntryIcon() {
        return ANSI_RED + "□" + ANSI_RESET;
    }
}
