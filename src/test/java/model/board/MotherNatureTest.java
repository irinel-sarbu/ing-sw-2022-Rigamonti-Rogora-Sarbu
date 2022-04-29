package model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MotherNatureTest {
    MotherNature motherNature;

    @BeforeEach
    public void setup() {
        motherNature = new MotherNature();
        motherNature.setPosition(10);
    }

    @Test
    public void getPosition() {
        assertTrue(motherNature.getPosition() >= 0 && motherNature.getPosition() <= 11);
    }

    @Test
    public void progress() {
        int initial = motherNature.getPosition();
        motherNature.progress(3, 12);
        System.out.println("Mother nature initial position: " + initial);
        System.out.println("Mother nature position after 3 steps (with 12 islandGroups, last island ID=11): " + motherNature.getPosition());
        assertTrue(motherNature.getPosition() != initial);
        motherNature.progress(-3, 12);
        System.out.println("Mother nature position after -3 steps (with 12 islandGroups, last island ID=11): " + motherNature.getPosition());
        assertEquals(motherNature.getPosition(), initial);
    }
}
