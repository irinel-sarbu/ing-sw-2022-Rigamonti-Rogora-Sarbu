package util;


import java.awt.event.WindowStateListener;

/**
 * Singleton Random wrapper class
 *  ensures predictable behaviour by setting a single generator through all program,
 *  seed settable before instantiating class
 */
public class Random {
    private static java.util.Random rng = null;

    public static java.util.Random instance() {
        if(rng == null)
            rng = new java.util.Random();
        return rng;
    }

    public static void setSeed(long seed) {
        instance().setSeed(seed);
    }

    public static int nextInt() {
        return instance().nextInt();
    }

    public static int nextInt(int bound) {
        return instance().nextInt(bound);
    }
}