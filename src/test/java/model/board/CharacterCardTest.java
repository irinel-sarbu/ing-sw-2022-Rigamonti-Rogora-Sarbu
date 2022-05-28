package model.board;

import exceptions.EmptyNoEntryListException;
import exceptions.EmptyStudentListException;
import exceptions.StudentNotFoundException;
import model.expert.CharacterCard;
import model.expert.NoEntryTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.CharacterType;
import util.Color;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardTest {
    private CharacterCard card1, card2, card3, card4, card5;

    @BeforeEach
    public void setup() {
        card1 = new CharacterCard(CharacterType.GRANNY_HERBS);
        card2 = new CharacterCard(CharacterType.MONK);
        card3 = new CharacterCard(CharacterType.JESTER);
        card4 = new CharacterCard(CharacterType.MUSHROOM_FANATIC);
        card5 = new CharacterCard(CharacterType.HERALD);

    }

    @Test
    public void testColor() {
        assertNull(card4.getColor());
        card4.setColor(Color.YELLOW);
        assertSame(card4.getColor(), Color.YELLOW);
    }

    @Test
    public void testStudents() {
        card2.addStudent(new Student(0, Color.RED));
        card2.addStudent(new Student(2, Color.YELLOW));
        card3.addStudent(new Student(1, Color.BLUE));
        card3.addStudent(new Student(3, Color.BLUE));
        card3.addStudent(new Student(4, Color.GREEN));
        assertEquals(2, card2.getStudents().size());
        assertNotNull(card2.getStudents().get(0));
        assertEquals(3, card3.getStudents().size());
        assertNotNull(card3.getStudents().get(0));
        try {
            card2.removeStudent(0);
        } catch (StudentNotFoundException e) {
            fail();
        }
        assertEquals(1, card2.getStudents().size());
        try {
            card2.removeStudent(2);
        } catch (StudentNotFoundException e) {
            fail();
        }
        try {
            card3.removeStudent(1);
        } catch (StudentNotFoundException e) {
            fail();
        }
        assertEquals(2, card3.getStudents().size());
        try {
            card3.removeStudent(3);
        } catch (StudentNotFoundException e) {
            fail();
        }
        assertThrows(StudentNotFoundException.class, () -> card3.removeStudent(999));
        try {
            card3.removeStudent(4);
        } catch (StudentNotFoundException e) {
            fail();
        }
        assertThrows(EmptyStudentListException.class, () -> card3.removeStudent(0));
    }

    @Test
    public void getCharacter() {
        assertSame(card1.getCharacter(), CharacterType.GRANNY_HERBS);
        assertSame(card4.getCharacter(), CharacterType.MUSHROOM_FANATIC);
        assertEquals(card1.getCost(), CharacterType.GRANNY_HERBS.getBaseCost());
        card1.setCost(card1.getCost() + 1);
        assertEquals(card1.getCost(), CharacterType.GRANNY_HERBS.getBaseCost() + 1);
        assertTrue(card2.toString().length() != 0);
        assertTrue(card1.toString().length() != 0);
        assertTrue(card3.toString().length() != 0);
        assertTrue(card4.toString().length() != 0);
        assertEquals(card1, card1);
        assertNotEquals(card1, null);
    }

    @Test
    public void testNoEntry() {
        card1.addNoEntryTile(new NoEntryTile());
        assertEquals(1, card1.getNoEntryTiles().size());
        try {
            card1.removeNoEntryTile();
        } catch (EmptyNoEntryListException e) {
            fail();
        }
        assertThrows(EmptyNoEntryListException.class, () -> card1.removeNoEntryTile());
        assertEquals(0, card1.getNoEntryTiles().size());
    }

    @Test
    public void equals() {
        assertNotEquals(null, new CharacterCard(CharacterType.GRANNY_HERBS));
        assertNotEquals(new CharacterCard(CharacterType.GRANNY_HERBS), new Student(1000, Color.BLUE));
    }

    @Test
    public void print() {
        List<CharacterCard> characterCardList = new ArrayList<>();
        card1.addNoEntryTile(new NoEntryTile());
        card1.addNoEntryTile(new NoEntryTile());
        card2.addStudent(new Student(0, Color.RED));
        card2.addStudent(new Student(2, Color.YELLOW));
        card3.addStudent(new Student(1, Color.BLUE));
        card3.addStudent(new Student(3, Color.BLUE));
        card3.addStudent(new Student(4, Color.GREEN));

        characterCardList.add(card1);
        characterCardList.add(card2);
        characterCardList.add(card3);
        characterCardList.add(card5);

        System.out.println(CharacterCard.allToString(characterCardList));
    }
}
