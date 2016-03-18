package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.level.Level;

/**
 * A simple implementation of a collision map for the JPacman player.
 * <p>
 * It uses a number of instanceof checks to implement the multiple dispatch for the 
 * collisionmap. For more realistic collision maps, this approach will not scale,
 * and the recommended approach is to use a {@link CollisionInteractionMap}.
 * 
 * @author Arie van Deursen, 2014
 *
 */

public class PlayerCollisions implements CollisionMap {

	public static Ghost ateGhost = null;

	@Override
	public void collide(Unit mover, Unit collidedOn) {
		
		if (mover instanceof Player) {
			playerColliding((Player) mover, collidedOn);
		}
		else if (mover instanceof Ghost) {
			ghostColliding((Ghost) mover, collidedOn);
		}
	}
	
	private void playerColliding(Player player, Unit collidedOn) {
		if (collidedOn instanceof Ghost) {
			if (((Ghost) collidedOn).getFearedMode()){
				playerVersusEatableGhost(player, (Ghost) collidedOn);
			}
			else {
				playerVersusGhost(player);
			}
		}
		if (collidedOn instanceof Pellet) {
			playerVersusPellet(player, (Pellet) collidedOn);
		}
	}
	
	private void ghostColliding(Ghost ghost, Unit collidedOn) {
		if (ghost.getFearedMode() && collidedOn instanceof Player) {
			playerVersusEatableGhost((Player) collidedOn, ghost);
		}
		else if (collidedOn instanceof Player) {
			playerVersusGhost((Player) collidedOn);
		}
	}
	
	
	/**
	 * Actual case of player bumping into ghost or vice versa.
     *
     * @param player The player involved in the collision.
	 */
	public void playerVersusGhost(Player player) {
		player.setAlive(false);
	}

	public void playerVersusEatableGhost(Player player, Ghost ghost)
	{
		PlayerCollisions.ateGhost = ghost;
		ghost.leaveSquare();
		Level.ghostLeft--;
		Level.ghostAte++;
		if(Level.ghostAte == 1){
			player.addPoints(200);
		}
		if(Level.ghostAte == 2) {
			player.addPoints(400);
		}
		if(Level.ghostAte == 3) {
			player.addPoints(800);
		}
		if(Level.ghostAte == 4) {
			player.addPoints(1600);
		}
	}
	
	/**
	 * Actual case of player consuming a pellet.
     *
     * @param player The player involved in the collision.
     * @param pellet The pellet involved in the collision.
	 */
	public void playerVersusPellet(Player player, Pellet pellet) {
		pellet.leaveSquare();
		player.addPoints(pellet.getValue());
		if(pellet.getValue() == 50) {
			player.setHunterMode(true);
		}
	}

}
