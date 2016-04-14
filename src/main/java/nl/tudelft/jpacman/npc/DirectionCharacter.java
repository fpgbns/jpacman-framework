package nl.tudelft.jpacman.npc;

import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.sprite.Sprite;

public interface DirectionCharacter {
	public Map<Direction, Sprite> getSprites();
	public void setMobility(boolean newValue);
	public void setSprites(Map<Direction, Sprite> sprites);
	public boolean getMobility();
}
