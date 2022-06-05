package util;

import java.util.Random;

public class Utils {
    /**
     * Used to create a random String
     */
    public static String randomString(int length) {
        Random random = new Random();

        return random.ints('0', 'z' + 1)
                .filter(i -> Character.isLetter(i) || Character.isDigit(i))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
