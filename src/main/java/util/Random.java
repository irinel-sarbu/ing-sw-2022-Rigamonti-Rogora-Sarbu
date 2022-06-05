package util;


import java.awt.event.WindowStateListener;

/**
 * Singleton Random wrapper class
 * ensures predictable behaviour by setting a single generator through all program,
 * seed settable before instantiating class
 */
public class Random {
    private static java.util.Random rng = null;

    /**
     * creates an instance of the Random class
     */
    public static java.util.Random instance() {
        if (rng == null)
            rng = new java.util.Random();
        return rng;
    }

    /**
     * sets a seed for the random class
     */
    public static void setSeed(long seed) {
        instance().setSeed(seed);
    }

    /**
     * uses base nextInt() of the random class
     */
    public static int nextInt() {
        return instance().nextInt();
    }

    /**
     * uses base nextInt(bound) of the random class
     */
    public static int nextInt(int bound) {
        return instance().nextInt(bound);
    }
}