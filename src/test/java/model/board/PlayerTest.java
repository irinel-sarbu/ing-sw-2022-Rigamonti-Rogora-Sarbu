package model.board;

import exceptions.AssistantNotInDeckException;
import model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.GameMode;
import util.TowerColor;
import util.Wizard;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private static Player alberto;
    private static Player irinel;
    private static Player matteo;
    private static Player empty;

    @BeforeAll
    public static void setup() {
        try {
            alberto = new Player("alberto", Wizard.WIZARD_1, TowerColor.BLACK, GameMode.EXPERT);
            alberto.pushFoldDeck(alberto.removeCard(alberto.getAssistants().get(0)));
            alberto.pushFoldDeck(alberto.removeCard(alberto.getAssistants().get(0)));
            irinel = new Player("irinel", Wizard.WIZARD_2, TowerColor.WHITE, GameMode.EXPERT);
            irinel.pushFoldDeck(irinel.removeCard(irinel.getAssistants().get(0)));
            matteo = new Player("matteo", Wizard.WIZARD_3, TowerColor.GRAY, GameMode.EXPERT);
        } catch (AssistantNotInDeckException e) {
            System.err.println("Unexpected AssistantNotInDeckException");
            fail();
        }
        empty = new Player("empty", Wizard.WIZARD_4, TowerColor.WHITE, GameMode.EXPERT);
        for (int i = 0; i < 10; i++) {
            try {
                empty.removeCard(0);
            } catch (AssistantNotInDeckException e) {
                fail();
            }
        }
    }

    @Test
    public void getGameMode() {
        assertNotNull(alberto.getGameMode());
    }

    @Test
    public void peekFoldDeck() {
        assertEquals(alberto.peekFoldDeck(), irinel.getAssistants().get(0));
    }

    @Test
    public void getAssistants() {

        for (Assistant sa : Assistant.getWizardDeck(Wizard.WIZARD_4)) {
            assertEquals(matteo.getAssistants().stream()
                    .filter(a -> a.equals(sa)).count(), 1);
        }
        for (Assistant sa : matteo.getAssistants()) {
            assertEquals(Assistant.getWizardDeck(Wizard.WIZARD_4).stream()
                    .filter(a -> a.equals(sa)).count(), 1);
        }

    }

    @Test
    public void getSchoolBoard() {
        assertNotNull(matteo.getSchoolBoard());
    }

    @Test
    public void getColor() {
        assertEquals(TowerColor.BLACK, alberto.getColor());
        assertEquals(TowerColor.WHITE, irinel.getColor());
        assertEquals(TowerColor.GRAY, matteo.getColor());
    }

    @Test
    public void getName() {
        assertEquals("alberto", alberto.getName());
        assertEquals("irinel", irinel.getName());
        assertEquals("matteo", matteo.getName());
    }

    @Test
    public void equals() {
        assertEquals(matteo, new Player("matteo", Wizard.WIZARD_3, TowerColor.BLACK, GameMode.EXPERT));
    }

    @Test
    public void compare() {
        assertEquals(1, alberto.compareTo(irinel));
        assertEquals(-1, irinel.compareTo(matteo));
        assertEquals(0, matteo.compareTo(empty));
    }

    @AfterEach
    public void resetPlayer() {
        try {
            alberto = new Player("alberto", Wizard.WIZARD_1, TowerColor.BLACK, GameMode.EXPERT);
            alberto.pushFoldDeck(alberto.removeCard(alberto.getAssistants().get(0)));
            alberto.pushFoldDeck(alberto.removeCard(alberto.getAssistants().get(0)));
            irinel = new Player("irinel", Wizard.WIZARD_2, TowerColor.WHITE, GameMode.EXPERT);
            irinel.pushFoldDeck(irinel.removeCard(irinel.getAssistants().get(0)));
            matteo = new Player("matteo", Wizard.WIZARD_3, TowerColor.GRAY, GameMode.EXPERT);
        } catch (AssistantNotInDeckException e) {
            System.err.println("Unexpected AssistantNotInDeckException");
            fail();
        }
        empty = new Player("empty", Wizard.WIZARD_4, TowerColor.WHITE, GameMode.EXPERT);
        for (int i = 0; i < 10; i++) {
            try {
                empty.removeCard(0);
            } catch (AssistantNotInDeckException e) {
                fail();
            }
        }
    }

    @Test
    public void clearFoldDeck() {
        alberto.clearFoldDeck();
        assertNull(alberto.peekFoldDeck());
    }

    @Test
    public void pushFoldDeck() {
        Assistant tmpAssistant = alberto.getAssistants().get(0);
        try {
            alberto.pushFoldDeck(alberto.removeCard(alberto.getAssistants().get(0)));
            assertSame(tmpAssistant, alberto.peekFoldDeck());
        } catch (AssistantNotInDeckException e) {
            System.err.println("Unexpected AssistantNotInDeckException");
            fail();
        }
    }

    @Test
    public void removeCard() {
        Assistant removed = matteo.getAssistants().get(0);
        int deckSize = matteo.getAssistants().size();

        try {
            matteo.removeCard(0);
        } catch (AssistantNotInDeckException e) {
            System.err.println("Unexpected AssistantNotInDeckException");
            fail();
        }

        assertEquals(deckSize - 1, matteo.getAssistants().size());
        assertFalse(matteo.getAssistants().contains(removed));

        removed = alberto.getAssistants().get(0);
        try {
            alberto.removeCard(0);
        } catch (AssistantNotInDeckException e) {
            System.err.println("Unexpected AssistantNotInDeckException");
            fail();
        }

        deckSize = alberto.getAssistants().size();
        try {
            alberto.removeCard(removed);
            System.err.println("Expected AssistantNotInDeckException");
            fail();
        } catch (AssistantNotInDeckException e) {
            assertEquals(deckSize, alberto.getAssistants().size());
        }
    }
}
