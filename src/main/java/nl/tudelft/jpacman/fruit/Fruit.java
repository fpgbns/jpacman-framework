package nl.tudelft.jpacman.fruit;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;

public abstract class Fruit extends Unit {

	private final Sprite image;
	
	private final int lifetime;
	
	private final int effectDuration;
	
	protected Fruit(Sprite sprite, int lifetime, int effectDuration) {
		this.image = sprite;
		this.lifetime = lifetime;
		this.effectDuration = effectDuration;
	}
	
	public Sprite getSprite() {
		return image;
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public int getEffectDuration() {
		return effectDuration;
	}
	
	public abstract void fruitEffect(Player p);
}
