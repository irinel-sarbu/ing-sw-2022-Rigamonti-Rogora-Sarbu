package model.board;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Color;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ProfessorTest {
    Professor professor1, professor2;

    @BeforeEach
    public void setup() {
        professor1 = new Professor(Color.RED);
        professor2 = new Professor(Color.BLUE);
    }

    @Test
    public void testProfessor() {
        assertSame(professor1.getColor(), Color.RED);
        assertSame(professor2.getColor(), Color.BLUE);
    }
}
