package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;

public class Hole extends Unit {

	/**
	 * The sprite of this unit.
	 */
	private final Sprite image;
	private int trapTime;
	
	/**
	 * Creates a new hole.
	 * @param sprite The sprite of this hole.
	 */
	public Hole(int time, Sprite sprite) {
		this.image = sprite;
		this.trapTime = time;
	}
	
	@Override
	public Sprite getSprite() {
		return image;
	}
	
	public int getTrapTime(){
		return trapTime;
	}
	
}
