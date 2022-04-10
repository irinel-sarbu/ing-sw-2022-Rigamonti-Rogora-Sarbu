package model.board;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TowerColor;

import static org.junit.jupiter.api.Assertions.*;

public class TowerTest {
    private Tower tower1, tower2;

    @BeforeEach
    public void setUp() {
        tower1 = new Tower(TowerColor.BLACK);
        tower2 = new Tower(TowerColor.WHITE);
    }

    @Test
    public void getColor() {
        assertSame(tower1.getColor(), TowerColor.BLACK);
        assertSame(tower2.getColor(), TowerColor.WHITE);
    }
}
