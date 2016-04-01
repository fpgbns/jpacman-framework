package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;
import nl.tudelft.jpacman.board.Square;

public class Teleport extends Unit {

	/**
	 * The sprite of this unit.
	 */
	private final Sprite image;
	private Square reference;
	
	/**
	 * Creates a new hole.
	 * @param sprite The sprite of this hole.
	 */
	public Teleport(Sprite sprite) {
		this.image = sprite;
	}
	
	@Override
	public Sprite getSprite() {
		return image;
	}
	
	public Square getReference(){
		return reference;
	}
	
	public void setReference(Square ref){
		reference = ref;
	}
	
}
