package nl.tudelft.jpacman.npc;

import java.util.Timer;
import java.util.TimerTask;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;

/**
 * A non-player unit.
 * 
 * @author Jeroen Roosen 
 */
public abstract class NPC extends Unit {
	
	private boolean AcceleratedInterval = false;

	/**
	 * The time that should be taken between moves.
	 * 
	 * @return The suggested delay between moves in milliseconds.
	 */
	public abstract long getInterval();

	/**
	 * Calculates the next move for this unit and returns the direction to move
	 * in.
	 * 
	 * @return The direction to move in, or <code>null</code> if no move could
	 *         be devised.
	 */
	public abstract Direction nextMove();
	
	public boolean isAccelerated() {
		return AcceleratedInterval;
	}
	
	public void setAcceleration(boolean value) {
		AcceleratedInterval = value;
	}
	
	/*
	public abstract void temporaryImmobility(int time);
	
	public abstract void temporaryAcceleration(int time);
	*/
}
