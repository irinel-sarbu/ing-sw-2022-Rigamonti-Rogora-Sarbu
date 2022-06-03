package model.board;

import exceptions.EmptyNoEntryListException;
import exceptions.IllegalIslandGroupJoinException;
import exceptions.IslandNotFoundException;
import exceptions.NullIslandGroupException;
import model.expert.NoEntryTile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Color;
import util.TowerColor;

import static org.junit.jupiter.api.Assertions.*;

public class IslandGroupTest {

    private static IslandGroup temp;
    private static IslandGroup singleIslandGroup;
    private static IslandGroup doubleIslandGroup;

    @BeforeAll
    public static void setup() {
        singleIslandGroup = new IslandGroup(0);

        doubleIslandGroup = new IslandGroup(1);
        doubleIslandGroup.setTowersColor(TowerColor.BLACK);
        doubleIslandGroup.addNoEntry(new NoEntryTile());

        doubleIslandGroup.getIslands().get(0).addStudent(new Student(0, Color.RED));

        temp = new IslandGroup(2);
        temp.setTowersColor(TowerColor.BLACK);

        try {
            doubleIslandGroup = doubleIslandGroup.join(temp);
        } catch (IllegalIslandGroupJoinException e) {
            System.err.println("Color error joining islands in ResetIslandGroup");
            fail();
        } catch (NullIslandGroupException e) {
            System.err.println("Joining island with no color in ResetIslandGroup");
            fail();
        }
        doubleIslandGroup.getIslands().get(1).addStudent(new Student(1, Color.RED));


        System.out.println("DoubleIslandGroup:\n" + doubleIslandGroup);
    }

    @Test
    public void getIslands() {
        assertEquals(1, singleIslandGroup.getIslands().size());
        assertEquals(2, doubleIslandGroup.getIslands().size());
        assertEquals(0, singleIslandGroup.getIslands().get(0).getIslandID());
//        assertEquals(1, doubleIslandGroup.getIslands().get(0).getIslandID());
//        assertEquals(2, doubleIslandGroup.getIslands().get(1).getIslandID());
        assertTrue(singleIslandGroup.toString().length() != 0);
    }

    @Test
    public void getIslandTileById() {
        try {
            assertEquals(0, singleIslandGroup.getIslandTileByID(0).getIslandID());
            assertEquals(1, doubleIslandGroup.getIslandTileByID(1).getIslandID());
            assertEquals(2, doubleIslandGroup.getIslandTileByID(2).getIslandID());
        } catch (IslandNotFoundException e) {
            System.err.println("Error getting IslandTileID");
            fail();
        }
        assertNull(doubleIslandGroup.getIslandTileByID(0));
    }

    @Test
    public void getIslandGroupID() {
        assertEquals(0, singleIslandGroup.getIslandGroupID());
        assertEquals(2, doubleIslandGroup.getIslandGroupID());
    }

    @Test
    public void getIslandTilesID() {
        assertEquals(0, singleIslandGroup.getIslandTilesID().get(0));
//        assertEquals(1, doubleIslandGroup.getIslandTilesID().get(0));
//        assertEquals(2, doubleIslandGroup.getIslandTilesID().get(1));
    }

    @Test
    public void getNoEntrySize() {
        assertEquals(0, singleIslandGroup.getNoEntrySize());
        assertEquals(1, doubleIslandGroup.getNoEntrySize());
    }

    @Test
    public void getSize() {
        assertEquals(1, singleIslandGroup.getSize());
        assertEquals(2, doubleIslandGroup.getSize());
    }

    @Test
    public void getTowerColor() {
        assertNull(singleIslandGroup.getTowersColor());
        assertEquals(TowerColor.BLACK, doubleIslandGroup.getTowersColor());
    }

    @Test
    public void getStudentsNumber() {
        for (Color color : Color.values()) {
            assertEquals(0, singleIslandGroup.getStudentsNumber(color));
            assertEquals(color.equals(Color.RED) ? 2 : 0, doubleIslandGroup.getStudentsNumber(color));
        }
    }

    @AfterEach
    public void resetIslandGroup() {
        singleIslandGroup = new IslandGroup(0);

        doubleIslandGroup = new IslandGroup(1);
        doubleIslandGroup.setTowersColor(TowerColor.BLACK);
        doubleIslandGroup.addNoEntry(new NoEntryTile());

        doubleIslandGroup.getIslands().get(0).addStudent(new Student(0, Color.RED));

        temp = new IslandGroup(2);
        temp.setTowersColor(TowerColor.BLACK);

        try {
            doubleIslandGroup = temp.join(doubleIslandGroup);
        } catch (IllegalIslandGroupJoinException e) {
            System.err.println("Color error joining islands in ResetIslandGroup");
            fail();
        } catch (NullIslandGroupException e) {
            System.err.println("Joining island with no color in ResetIslandGroup");
            fail();
        }
        doubleIslandGroup.getIslands().get(1).addStudent(new Student(1, Color.RED));

    }

    @Test
    public void addNoEntry() {
        singleIslandGroup.addNoEntry(new NoEntryTile());
        doubleIslandGroup.addNoEntry(new NoEntryTile());
        assertEquals(1, singleIslandGroup.getNoEntrySize());
        assertEquals(2, doubleIslandGroup.getNoEntrySize());
    }

    @Test
    public void removeNoEntry() {
        try {
            singleIslandGroup.removeNoEntry();
            System.err.println("Expected EmptyNoEntryListException");
            fail();
        } catch (EmptyNoEntryListException e) {
            assertTrue(true);
        }
        try {
            doubleIslandGroup.removeNoEntry();
            assertEquals(0, doubleIslandGroup.getNoEntrySize());
        } catch (EmptyNoEntryListException e) {
            System.err.println("Expected correct NoEntryTileRemoval");
            fail();
        }
    }

    @Test
    public void setTowerColor() {
        singleIslandGroup.setTowersColor(TowerColor.GRAY);
        doubleIslandGroup.setTowersColor(TowerColor.WHITE);
        assertEquals(TowerColor.WHITE, doubleIslandGroup.getTowersColor());
        assertEquals(TowerColor.GRAY, singleIslandGroup.getTowersColor());
    }

    @Test
    public void join() {
        try {
            singleIslandGroup = singleIslandGroup.join(null);
            System.out.println(singleIslandGroup);
            System.err.println("Expected NullIslandGroupException");
            fail();
        } catch (IllegalIslandGroupJoinException e) {
            System.err.println("IllegalIslandGroupJoinException not expected");
            fail();
        } catch (NullIslandGroupException e) {
            assertTrue(true);
        }

        try {
            singleIslandGroup = singleIslandGroup.join(doubleIslandGroup);
            System.out.println(singleIslandGroup);
            System.err.println("Expected NullPointerException");
            fail();
        } catch (NullIslandGroupException e) {
            System.err.println("NullIslandGroupException not excepted");
            fail();
        } catch (IllegalIslandGroupJoinException e) {
            assertTrue(true);
        }

        try {
            singleIslandGroup = doubleIslandGroup.join(singleIslandGroup);
            System.err.println("Expected IllegalIslandGroupJoinException");
            fail();
        } catch (NullIslandGroupException e) {
            System.err.println("NullIslandGroupException not excepted");
            fail();
        } catch (IllegalIslandGroupJoinException e) {
            assertTrue(true);
        }

        IslandGroup test = new IslandGroup(3);
        test.getIslands().get(0).addStudent(new Student(3, Color.RED));
        test.addNoEntry(new NoEntryTile());
        test.setTowersColor(TowerColor.WHITE);

        try {
            doubleIslandGroup.join(test);
            System.out.println(test);
            System.err.println("Expected IllegalIslandGroupJoinException");
            fail();
        } catch (NullIslandGroupException e) {
            System.err.println("NullIslandGroupException not excepted");
            fail();
        } catch (IllegalIslandGroupJoinException e) {
            assertTrue(true);
        }

        test.getIslands().get(0).addStudent(new Student(5, Color.RED));
        test.setTowersColor(TowerColor.BLACK);

        try {
            System.out.println("Before join:\ntest:\n" + test + "\ndoubleIsland:\n" + doubleIslandGroup);
            doubleIslandGroup = doubleIslandGroup.join(test);
            System.out.println("doubleIslandGroup.join(test)\n" + doubleIslandGroup);
            assertEquals(TowerColor.BLACK, doubleIslandGroup.getTowersColor());
            assertEquals(3, doubleIslandGroup.getSize());
            assertEquals(2, doubleIslandGroup.getNoEntrySize());
            assertEquals(2, doubleIslandGroup.getIslandGroupID());

//            for (int i = 0; i < 3; i++) {
//                assertEquals(i + 1, test.getIslands().get(i).getIslandID());
//            }
            for (Color color : Color.values()) {
                assertEquals(color.equals(Color.RED) ? 4 : 0, doubleIslandGroup.getStudentsNumber(color));
            }
//            for (int i = 0; i < 3; i++) {
//                assertEquals(i + 1, test.getIslandTilesID().get(i));
//            }
        } catch (NullIslandGroupException e) {
            System.err.println("NullIslandGroupException not excepted");
            fail();
        } catch (IllegalIslandGroupJoinException e) {
            System.err.println("IllegalIslandGroupJoinException not excepted");
            fail();
        }

        assertNull(doubleIslandGroup.getIslandTileByID(0));

        try {
            assertEquals(1, doubleIslandGroup.getIslandTileByID(1).getIslandID());
            assertEquals(2, doubleIslandGroup.getIslandTileByID(2).getIslandID());
            assertEquals(3, doubleIslandGroup.getIslandTileByID(3).getIslandID());
        } catch (IslandNotFoundException e) {
            System.err.println("IslandNotFoundException not expected");
            fail();
        }
    }

//    @Test
//    public void print() {
//        List<IslandGroup> islandGroupList = new ArrayList<>();
//        IslandGroup ig00 = new IslandGroup(0);
//        ig00.setTowersColor(TowerColor.WHITE);
//
//        IslandGroup ig11 = new IslandGroup(11);
//        ig11.setTowersColor(TowerColor.WHITE);
//
//        try {
//            ig00 = ig00.join(ig11);
//
//            islandGroupList.add(ig00);
//        } catch (IllegalIslandGroupJoinException | NullIslandGroupException e) {
//            fail();
//        }
//
//        String igs = IslandGroup.allToString(islandGroupList, 0);
//        System.out.println(igs);
//    }
//
//    @Test
//    public void printMultipleRow() {
//        List<IslandGroup> islandGroupList = new ArrayList<>();
//        for (int i = 0; i < 12; i++) {
//            IslandGroup ig = new IslandGroup(i);
//            islandGroupList.add(ig);
//        }
//
//        System.out.println(IslandGroup.allToString(islandGroupList, 0));
//    }
}
