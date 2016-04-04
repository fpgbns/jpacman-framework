package nl.tudelft.jpacman.fruit;

import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;

public class KidneyBean extends Fruit {
	
	protected KidneyBean(Sprite sprite, int lifetime, int effectDuration) {
		super(sprite, lifetime, effectDuration);
	}

	@Override
	public void fruitEffect(Player p) {
		p.temporaryShooting(getEffectDuration());
	}
}
