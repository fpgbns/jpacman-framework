package nl.tudelft.jpacman.npc.ghost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * An antagonist in the game of Pac-Man, a ghost.
 * 
 * @author Jeroen Roosen 
 */
public abstract class Ghost extends NPC {
	
	/**
	 * The sprite map, one sprite for each direction.
	 */
	private Map<Direction, Sprite> sprites;
	
	private final AnimatedSprite explodeSprite;
	
	private boolean acceleration;
	
	private boolean exploded;

	/**
	 * Creates a new ghost.
	 * 
	 * @param spriteMap
	 *            The sprites for every direction.
	 */
	protected Ghost(Map<Direction, Sprite> spriteMap, AnimatedSprite explodeAnimation) {
		this.explodeSprite = explodeAnimation;
		this.exploded = false;
		this.acceleration = false;
		this.sprites = spriteMap;
	}

	@Override
	public Sprite getSprite() {
		if (!exploded) {
			return sprites.get(getDirection());
		}
		return explodeSprite;
	}
	
	public void setSprite(Map<Direction, Sprite> sprites) {
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
	
	public void setAcceleration(boolean value) {
		acceleration = value;
	}
	
	public boolean getAcceleration() {
		return acceleration;
	}
	
	public void temporaryImmobility(int time)
	{
		Map<Direction, Sprite> oldSprites = sprites;
		setMobility(false);
		setSprite(new PacManSprites().getParalizedGhostSprite());
		TimerTask timerTask = new TimerTask() {
		    public void run() {
		        setMobility(true);
		        setSprite(oldSprites);
		    }
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, time * 1000);
	}
	
	public void temporaryAcceleration(int time)
	{
		Map<Direction, Sprite> oldSprites = sprites;
		setAcceleration(true);
		setSprite(new PacManSprites().getAngryGhostSprite());
		TimerTask timerTask = new TimerTask() {
		    public void run() {
		    	setAcceleration(false);
		        setSprite(oldSprites);
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
}
