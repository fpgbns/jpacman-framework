package nl.tudelft.jpacman.game;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.fruit.Fruit;
import nl.tudelft.jpacman.fruit.FruitFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Bullet;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.sprite.PacManSprites;

import com.google.common.collect.ImmutableList;

/**
 * A game with one player and a single level.
 * 
 * @author Jeroen Roosen 
 */
public class SinglePlayerGame extends Game {

	/**
	 * The player of this game.
	 */
	private final Player player;

	/**
	 * The level of this game.
	 */
	private final Level level;
	
	private boolean fruitLock = true;

	private boolean shootLock = true;
	/**
	 * Create a new single player game for the provided level and player.
	 * 
	 * @param p
	 *            The player.
	 * @param l
	 *            The level.
	 */
	protected SinglePlayerGame(Player p, Level l) {
		assert p != null;
		assert l != null;

		this.player = p;
		this.level = l;
		level.registerPlayer(p);
	}

	@Override
	public List<Player> getPlayers() {
		return ImmutableList.of(player);
	}

	@Override
	public Level getLevel() {
		return level;
	}

	/**
	 * Moves the player one square to the north if possible.
	 */
	public void moveUp() {
		move(player, Direction.NORTH);
	}

	/**
	 * Moves the player one square to the south if possible.
	 */
	public void moveDown() {
		move(player, Direction.SOUTH);
	}

	/**
	 * Moves the player one square to the west if possible.
	 */
	public void moveLeft() {
		move(player, Direction.WEST);
	}

	/**
	 * Moves the player one square to the east if possible.
	 */
	public void moveRight() {
		move(player, Direction.EAST);
	}

	@Override
	public void fruitEvent() {
		if(fruitLock){
			fruitLock = false;
			FruitFactory fruitFactory = level.getFruitFactory();
			Fruit fruit = fruitFactory.getRandomFruit();
			Square postion = fruitFactory.getRandomFruitPosition();
			fruit.occupy(postion);
			TimerTask timerTask = new TimerTask() {
		        public void run() {
		        	for(Unit occupant : postion.getOccupants()){
		        		if(occupant instanceof Fruit){
		        			fruit.leaveSquare();
		        		}
		        	}
		        	fruitLock = true;
		        }
		    };
		    Timer timer = new Timer();
		    timer.schedule(timerTask, fruit.getLifetime() * 1000);
		}
	}

	@Override
	public void ShootingEvent() {
		if(shootLock){
			shootLock = false;
			Bullet b = new Bullet(new PacManSprites().getPelletSprite(), player);
			b.occupy(player.getSquare());
			level.animateBullet(b);
			TimerTask timerTask = new TimerTask() {
		        public void run() {
		        	shootLock = true;
		        }
		    };
		    Timer timer = new Timer();
		    timer.schedule(timerTask, b.getBulletDelay() * 1000);
		}
	}

	@Override
	public void bulletCleanEvent(List<Bullet> bullets, Map<NPC, ScheduledExecutorService> npcs) {
		for(Bullet bullet : bullets) {
			bullet.leaveSquare();
			npcs.remove(bullet);
		}
	}

}
