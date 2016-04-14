package nl.tudelft.jpacman.npc.ghost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.DirectionCharacter;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * An antagonist in the game of Pac-Man, a ghost.
 * 
 * @author Jeroen Roosen 
 */
public abstract class Ghost extends NPC implements DirectionCharacter {
	
	/**
	 * The sprite map, one sprite for each direction.
	 */
	private Map<Direction, Sprite> sprites;
	
	private final AnimatedSprite explodeSprite;
	
	private boolean exploded;
	
	/**
	 * Whether this unit can be moved or not.
	 */
	private boolean mobile = true;

	/**
	 * Creates a new ghost.
	 * 
	 * @param spriteMap
	 *            The sprites for every direction.
	 */
	protected Ghost(Map<Direction, Sprite> spriteMap, AnimatedSprite explodeAnimation) {
		this.explodeSprite = explodeAnimation;
		this.exploded = false;
		this.sprites = spriteMap;
	}

	@Override
	public Sprite getSprite() {
		if (!exploded) {
			return sprites.get(getDirection());
		}
		return explodeSprite;
	}
	
	public Map<Direction, Sprite> getSprites() {
		return sprites;
	}
	
	public void setSprites(Map<Direction, Sprite> sprites) {
		this.sprites = sprites;
	}

	/**
	 * Determines a possible move in a random direction.
	 * 
	 * @return A direction in which the ghost can move, or <code>null</code> if
	 *         the ghost is shut in by inaccessible squares.
	 */
	protected Direction randomMove() {
		Square square = getSquare();
		List<Direction> directions = new ArrayList<>();
		for (Direction d : Direction.values()) {
			if (square.getSquareAt(d).isAccessibleTo(this)) {
				directions.add(d);
			}
		}
		if (directions.isEmpty()) {
			return null;
		}
		int i = new Random().nextInt(directions.size());
		return directions.get(i);
	}
	
	public void temporaryAcceleration(int time)
	{
		Map<Direction, Sprite> oldSprites = sprites;
		setAcceleration(true);
		setSprites(new PacManSprites().getAngryGhostSprite());
		TimerTask timerTask = new TimerTask() {
		    public void run() {
		    	setAcceleration(false);
		        setSprites(oldSprites);
		    }
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, time * 1000);
	}

	public boolean hasExploded() {
		return exploded;
	}
	
	public void setExplode(boolean value) {
		if (!value) {
			explodeSprite.setAnimating(false);
		}
		if (value) {
			setMobility(false);
			explodeSprite.restart();
		}
		this.exploded = value;
	}
	
	/**
	 * Sets if this unit can be moved or not.
	 * @param newValue the new mobility state of this unit.
	 */
	public void setMobility(boolean newValue) {
		this.mobile = newValue;
	}
	
	/**
	 * Returns the mobility state of this unit.
	 * @return The current mobility state of this unit.
	 */
	public boolean getMobility() {
		return this.mobile;
	}
}
