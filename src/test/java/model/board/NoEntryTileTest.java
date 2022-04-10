package model.board;

import exceptions.EmptyNoEntryListException;
import model.GameModel;
import model.Player;
import model.expert.CharacterCard;
import model.expert.NoEntryTile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.CharacterType;
import util.GameMode;
import util.TowerColor;
import util.Wizard;
import static org.junit.jupiter.api.Assertions.*;




public class NoEntryTileTest {
    private static GameModel game;
    private static CharacterCard grannyHerbs;

    @BeforeAll
    public static void setUp() {
        game = new GameModel(3, GameMode.EXPERT);
        game.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK));
        grannyHerbs = new CharacterCard(CharacterType.GRANNY_HERBS);
        for (int i = 0; i < 4; i++) grannyHerbs.addNoEntryTile(new NoEntryTile());
        try {
            game.getIslandGroupByID(0).addNoEntry(grannyHerbs.removeNoEntryTile());
        } catch (EmptyNoEntryListException e) {
            fail();
        }
    }
    //setPosition is tested when added/removed from islandGroup

    @Test
    public void getPositionOnIsland(){
        try {
            int print = game.getIslandGroupByID(0).removeNoEntry().getPosition();
            assertTrue(print==-1);
        } catch (EmptyNoEntryListException e) {
            fail();
        }
    }

    @Test
    public void getPositionOnGranny(){
        try {
            assertTrue(grannyHerbs.removeNoEntryTile().getPosition()==-1);
        } catch (EmptyNoEntryListException e) {
            fail();
        }
    }

}
