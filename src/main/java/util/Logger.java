package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final static String ANSI_RESET = "\u001B[0m";

    private final static String TEXT_WHITE_UNDERLINED = "\033[4;37m";
    private final static String TEXT_YELLOW = "\033[0;33m";
    private final static String TEXT_RED = "\033[0;31m";
    private final static String TEXT_RED_BOLD = "\033[1;31m";

    public enum LoggerLevel {
        ALL(0),
        DEBUG(10),
        INFO(20),
        WARNING(30),
        ERROR(40),
        SEVERE(50),
        DISABLED(100);

        private final int value;

        LoggerLevel(int num) {
            this.value = num;
        }

        public int getValue() {
            return value;
        }
    }

    private LoggerLevel level;

    private Logger() {
        this.level = LoggerLevel.ALL;
    }

    public static synchronized Logger get() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    public static void setLevel(LoggerLevel level) {
        get().level = level;
    }

    private synchronized void print(String msg) {
        System.out.println(msg);
    }

    private synchronized void printIndented(String msg) {
        System.out.println("\t" + msg);
    }

    private static String getCallerClassName() {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
        return ste.getClassName();
    }

    public static void debug(String... msg) {
        if (LoggerLevel.DEBUG.getValue() >= get().level.getValue() && msg.length > 0) {
            String date = get().getDate();
            get().print(date + " - " + TEXT_WHITE_UNDERLINED + "DEBUG" + ANSI_RESET + "   - " + getCallerClassName() + " - " + msg[0]);
            for (int i = 1; i < msg.length; i++) {
                get().printIndented(msg[i]);
            }
        }
    }

    public static void info(String... msg) {
        if (LoggerLevel.INFO.getValue() >= get().level.getValue() && msg.length > 0) {
            String date = get().getDate();
            get().print(date + " - INFO    - " + getCallerClassName() + " - " + msg[0]);
            for (int i = 1; i < msg.length; i++) {
                get().printIndented(msg[i]);
            }
        }
    }

    public static void warning(String... msg) {
        if (LoggerLevel.WARNING.getValue() >= get().level.getValue() && msg.length > 0) {
            String date = get().getDate();
            get().print(date + " - " + TEXT_YELLOW + "WARNING" + ANSI_RESET + " - " + getCallerClassName() + " - " + msg[0]);
            for (int i = 1; i < msg.length; i++) {
                get().printIndented(msg[i]);
            }
        }
    }

    public static void error(String... msg) {
        if (LoggerLevel.ERROR.getValue() >= get().level.getValue() && msg.length > 0) {
            String date = get().getDate();
            get().print(date + " - " + TEXT_RED + "ERROR" + ANSI_RESET + "   - " + getCallerClassName() + " - " + msg[0]);
            for (int i = 1; i < msg.length; i++) {
                get().printIndented(msg[i]);
            }
        }
    }

    public static void severe(String... msg) {
        if (LoggerLevel.SEVERE.getValue() >= get().level.getValue() && msg.length > 0) {
            String date = get().getDate();
            get().print(date + " - " + TEXT_RED_BOLD + "SEVERE" + ANSI_RESET + "  - " + getCallerClassName() + " - " + msg[0]);
            for (int i = 1; i < msg.length; i++) {
                get().printIndented(msg[i]);
            }
        }
    }

    private String getDate() {
        return LocalDateTime.now().format(dtf);
    }
}
