package nl.tudelft.jpacman.level;

import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;

public class Bridge extends Unit{
	private Map<Direction, Sprite> sprites;

	public Bridge(Map<Direction, Sprite> spriteMap){
		sprites = spriteMap;
	}
	
	public Sprite getSprite(){
		return sprites.get(getDirection());
	}
	
	public boolean parralelTo(Direction dir){
		Direction bDir = getDirection();
		if(((dir == Direction.NORTH || dir == Direction.SOUTH) && bDir == Direction.NORTH)
		   || ((dir == Direction.EAST || dir == Direction.WEST) && bDir == Direction.EAST)){
				return true;
			}
			else{
				return false;
			}
	}
	
	public void effect(Unit unit) {
		Direction uDir = unit.getDirection();
		if(parralelTo(uDir)){
			unit.setOnBridge(true);
		}
		else{
			unit.setOnBridge(false);
		}
	}
}
