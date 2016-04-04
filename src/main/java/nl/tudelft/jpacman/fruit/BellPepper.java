package nl.tudelft.jpacman.fruit;

import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;

public class BellPepper extends Fruit{
	
	protected BellPepper(Sprite sprite, int lifetime, int effectDuration) {
		super(sprite, lifetime, effectDuration);
	}

	@Override
	public void fruitEffect(Player p) {
		p.temporaryAcceleration(getEffectDuration());
	}
}
