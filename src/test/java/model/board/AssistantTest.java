package model.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Wizard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AssistantTest {

    private static List<Assistant> deck1;
    private static List<Assistant> deck2;

    @BeforeAll
    public static void setup() {
        deck1 = Assistant.getWizardDeck(Wizard.WIZARD_1);
        deck2 = Assistant.getWizardDeck(Wizard.WIZARD_2);
        for (Assistant assistant : deck1) {
            assertEquals(1, deck2.stream().filter(a -> a.equals(assistant)).count());
        }
        for (Assistant assistant : deck2) {
            assertEquals(1, deck1.stream().filter(a -> a.equals(assistant)).count());
        }
    }

    @Test
    public void getter() {
        String[] names = {"Lion", "Goose", "Cat", "Eagle", "Fox", "Snake", "Octopus", "Dog", "Elephant", "Tortoise"};
        assertEquals(names.length, deck1.size());
        assertEquals(names.length, deck2.size());
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], deck1.get(i).getName());
            assertEquals(i + 1, deck1.get(i).getValue());
            assertEquals(i / 2 + 1, deck1.get(i).getMovements());
        }
    }

    @Test
    public void equals() {
        assertEquals(deck1.get(0), deck1.get(0));
        assertEquals(deck1.get(0), deck2.get(0));
        assertNotEquals(deck1.get(0), deck2.get(1));
    }

    @AfterEach
    public void resetAssistant() {
        deck1 = Assistant.getWizardDeck(Wizard.WIZARD_1);
        deck2 = Assistant.getWizardDeck(Wizard.WIZARD_2);

    }
}
