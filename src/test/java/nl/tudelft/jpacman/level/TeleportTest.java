package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;
import java.util.List;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Teleport;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;
import nl.tudelft.jpacman.sprite.SpriteStore;

import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Lists;


public class TeleportTest {
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/**
	 * Verifies that a Teleport object is initialized correctly. 
	 * 
	 * @throws IOException
	 */
	@Test
	public void initializationTest() throws IOException {
		SpriteStore store = new SpriteStore();
        Sprite sprite = store.loadSprite("/sprite/64x64white.png");
		Teleport testTeleport = new Teleport(sprite);
		assertEquals(testTeleport.getSprite(), sprite);
		assertNull(testTeleport.getReference());
	}
	
	/**
	 * Verifies that an 'T' character in a string representing a board produce a Square object only occupied by a Teleport object.
	 * 
	 * @throws IOException
	 */
	@Test
	public void BoardteleportTest() throws IOException {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
		Board b = parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "#T #", "####","----", "2 1 "))).getBoard();
		Square s1 = b.squareAt(1, 1);
		Square s2 = b.squareAt(2, 1);
		List<Unit> occupants =  s1.getOccupants();

		assertEquals(occupants.size(), 1);
		assertThat(occupants.get(0),  instanceOf(Teleport.class));
		Teleport t = (Teleport) occupants.get(0);
		assertEquals(t.getReference(), s2);
	}
	
	/**
	 * Verifies that a teleport reference can't be an integer negative number.
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest1() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "#T #", "####","----", "-1 1"))).getBoard();
	}
	
	/**
	 * Verifies that a teleport reference can't be another character
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest2() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "#T #", "####","----", "1 A "))).getBoard();
	}
	
	/**
	 * Verifies that a teleport reference can't contain only one element.
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest3() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "#T #", "####","----", "  2 "))).getBoard();
	}
	
	/**
	 * Verifies that a teleport reference can't point out of the board.
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest4() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "#TP#", "####","----", "3 3 "))).getBoard();
	}
	
	/**
	 * Verifies that all teleport reference cannot have no references in the case where the reference list is incomplete.
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest5() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("#####", "#TPT#", "#####","-----", "1 1  "))).getBoard();
	}
	
	/**
	 * Verifies that all teleport reference cannot have no references in the case where the reference list is empty.
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest6() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "#TP#", "####"))).getBoard();
	}
	
	/**
	 * Verifies that a teleport reference can't contain more than two element.
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest7() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "#T #", "####","----", " 2 3 2 "))).getBoard();
	}
	
	/**
	 * Verifies that there can't be more references than teleports.
	 * 
	 * @throws IOException
	 */
	@Test
	public void FomatFailTest8() {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        thrown.expect(PacmanConfigurationException.class);
		parser.parseMap(Lists.newArrayList(Lists.newArrayList("####", "# P#", "####","----", "2 3 "))).getBoard();
	}
}
