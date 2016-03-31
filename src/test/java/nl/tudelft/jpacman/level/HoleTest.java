package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;
import java.util.List;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Hole;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;
import nl.tudelft.jpacman.sprite.SpriteStore;

import org.junit.Test;

import com.google.common.collect.Lists;

public class HoleTest {
	
	public static final int HOLE_TIME_TEST = 2;
	
	/**
	 * Verifies that a Hole object is initialized correctly. 
	 * 
	 * @throws IOException
	 */
	@Test
	public void initializationTest() throws IOException {
		SpriteStore store = new SpriteStore();
        Sprite sprite = store.loadSprite("/64x64white.png");
		Hole testHole = new Hole(HOLE_TIME_TEST, sprite);
		assertEquals(testHole.getTrapTime(), HOLE_TIME_TEST);
		assertEquals(testHole.getSprite(), sprite);
	}
	
	/**
	 * Verifies that an 'H' character in a string representing a board produce a Square object only occupied by a Hole object.
	 * 
	 * @throws IOException
	 */
	@Test
	public void BoardHoleTest() throws IOException {
		PacManSprites sprites = new PacManSprites();
		MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
		Board b = parser.parseMap(Lists.newArrayList("H")).getBoard();
		Square s1 = b.squareAt(0, 0);
		List<Unit> occupants =  s1.getOccupants();

		assertEquals(occupants.size(), 1);
		assertThat(occupants.get(0),  instanceOf(Hole.class));
	}
}
