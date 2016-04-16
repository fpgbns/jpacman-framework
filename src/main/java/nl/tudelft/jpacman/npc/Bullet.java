package nl.tudelft.jpacman.npc;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Bridge;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;

public class Bullet extends NPC{
	
	/**
	 * The sprite map, one sprite for each direction.
	 */
	private Sprite sprite;
	
	/**
	 * The base movement interval.
	 */
	private static final int MOVE_INTERVAL = 100;
	
	private static final int BULLET_DELAY = 1;
	
	private boolean alive;
	
	private final Direction shootingDirection;
	
	public Bullet(Sprite sprite, Player p) {
		this.setAlive(true);
		this.sprite = sprite;
		shootingDirection = p.getDirection();
	}

	@Override
	public long getInterval() {
		return MOVE_INTERVAL;
	}

	@Override
	public Direction nextMove() {
		if(getSquare().getSquareAt(shootingDirection).isAccessibleTo(this) && !(Bridge.blockedBybridge(this, shootingDirection))) {
			return shootingDirection;
		}
		else {
			setAlive(false);
			return null;
		}
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public int getBulletDelay() {
		return BULLET_DELAY;
	}
	

}
