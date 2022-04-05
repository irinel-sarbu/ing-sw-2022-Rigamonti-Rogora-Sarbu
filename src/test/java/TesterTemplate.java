import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesterTemplate {

    //Verify tester template
    @Test
    public void testingTester() {
        assertTrue(true);
    }

    //Verify template failure
    @Test
    public void failingTester() {
        assertFalse(false);
    }
}
