package nl.tudelft.jpacman.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.fruit.FruitFactory;
import nl.tudelft.jpacman.level.Bridge;
import nl.tudelft.jpacman.npc.Bullet;
import nl.tudelft.jpacman.npc.DirectionCharacter;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.sprite.PacManSprites;

/**
 * A level of Pac-Man. A level consists of the board with the players and the
 * AIs on it.
 * 
 * @author Jeroen Roosen 
 */
public class Level {

	/**
	 * The board of this level.
	 */
	private final Board board;

	/**
	 * The lock that ensures moves are executed sequential.
	 */
	private final Object moveLock = new Object();

	/**
	 * The lock that ensures starting and stopping can't interfere with each
	 * other.
	 */
	private final Object startStopLock = new Object();

	/**
	 * The NPCs of this level and, if they are running, their schedules.
	 */
	private final Map<NPC, ScheduledExecutorService> npcs;

	/**
	 * <code>true</code> iff this level is currently in progress, i.e. players
	 * and NPCs can move.
	 */
	private boolean inProgress;

	/**
	 * The squares from which players can start this game.
	 */
	private final List<Square> startSquares;

	/**
	 * The start current selected starting square.
	 */
	private int startSquareIndex;
	
	/**
	 * The Fruit factory for this level.
	 */
	private FruitFactory fruitFactory;

	/**
	 * The players on this level.
	 */
	private final List<Player> players;

	/**
	 * The table of possible collisions between units.
	 */
	private final CollisionMap collisions;

	/**
	 * The objects observing this level.
	 */
	private final List<LevelObserver> observers;

	/**
	 * Creates a new level for the board.
	 * 
	 * @param b
	 *            The board for the level.
	 * @param ghosts
	 *            The ghosts on the board.
	 * @param startPositions
	 *            The squares on which players start on this board.
	 * @param collisionMap
	 *            The collection of collisions that should be handled.
	 */
	public Level(Board b, List<NPC> ghosts, List<Square> startPositions,
			CollisionMap collisionMap) {
		assert b != null;
		assert ghosts != null;
		assert startPositions != null;

		this.board = b;
		this.inProgress = false;
		this.npcs = new HashMap<>();
		for (NPC g : ghosts) {
			npcs.put(g, null);
		}
		this.startSquares = startPositions;
		this.startSquareIndex = 0;
		this.players = new ArrayList<>();
		this.collisions = collisionMap;
		this.observers = new ArrayList<>();
	}
	
	/**
	 * Setup the fruit for this level if the board specified that some square may contain a fruit.
	 * @param fruitpositions the list of the squares where a fruit may appear
	 * @param npcs the list of the NPC registered on this level at the time of setting-up the fruits. 
	 */
	public void setupFruits(List<Square> fruitpositions, List<NPC> npcs) {
		if(fruitpositions.size() > 0)
			fruitFactory = new FruitFactory(new PacManSprites(), fruitpositions, npcs);
	}
	

	/**
	 * Adds an observer that will be notified when the level is won or lost.
	 * 
	 * @param observer
	 *            The observer that will be notified.
	 */
	public void addObserver(LevelObserver observer) {
		if (observers.contains(observer)) {
			return;
		}
		observers.add(observer);
	}

	/**
	 * Removes an observer if it was listed.
	 * 
	 * @param observer
	 *            The observer to be removed.
	 */
	public void removeObserver(LevelObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Registers a player on this level, assigning him to a starting position. A
	 * player can only be registered once, registering a player again will have
	 * no effect.
	 * 
	 * @param p
	 *            The player to register.
	 */
	public void registerPlayer(Player p) {
		assert p != null;
		assert !startSquares.isEmpty();

		if (players.contains(p)) {
			return;
		}
		players.add(p);
		npcs.put(p, null);
		Square square = startSquares.get(startSquareIndex);
		p.occupy(square);
		startSquareIndex++;
		startSquareIndex %= startSquares.size();
	}

	/**
	 * Returns the board of this level.
	 * 
	 * @return The board of this level.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Moves the unit into the given direction if possible and handles all
	 * collisions.
	 * 
	 * @param unit
	 *            The unit to move.
	 * @param direction
	 *            The direction to move the unit in.
	 */
	public void move(Unit unit, Direction direction) {
		assert unit != null;
		assert direction != null;

		if (!isInProgress() || (unit instanceof DirectionCharacter && !((DirectionCharacter) unit).getMobility())) {
			return;
		}

		synchronized (moveLock) {
			unit.setDirection(direction);
			Square location = unit.getSquare();
			Square destination = location.getSquareAt(direction);
			
			if (destination.isAccessibleTo(unit) && !(Bridge.blockedBybridge(unit, direction))) {
				unit.setOnBridge(false);
				List<Unit> occupants = destination.getOccupants();
				unit.occupy(destination);
				for (Unit occupant : occupants) {
					collisions.collide(unit, occupant);
				}
			}
			updateObservers();
		}
	}

	/**
	 * Starts or resumes this level, allowing movement and (re)starting the
	 * NPCs.
	 */
	public void start() {
		synchronized (startStopLock) {
			if (isInProgress()) {
				return;
			}
			startNPCs();
			inProgress = true;
			updateObservers();
		}
	}

	/**
	 * Stops or pauses this level, no longer allowing any movement on the board
	 * and stopping all NPCs.
	 */
	public void stop() {
		synchronized (startStopLock) {
			if (!isInProgress()) {
				return;
			}
			stopNPCs();
			inProgress = false;
		}
	}

	/**
	 * Starts all NPC movement scheduling.
	 */
	private void startNPCs() {
		for (final NPC npc : npcs.keySet()) {
			ScheduledExecutorService service = Executors
					.newSingleThreadScheduledExecutor();
			service.schedule(new NpcMoveTask(service, npc),
					npc.getInterval() / 2, TimeUnit.MILLISECONDS);
			npcs.put(npc, service);
		}
	}

	/**
	 * Stops all NPC movement scheduling and interrupts any movements being
	 * executed.
	 */
	private void stopNPCs() {
		for (Entry<NPC, ScheduledExecutorService> e : npcs.entrySet()) {
			e.getValue().shutdownNow();
		}
	}

	/**
	 * Returns whether this level is in progress, i.e. whether moves can be made
	 * on the board.
	 * 
	 * @return <code>true</code> iff this level is in progress.
	 */
	public boolean isInProgress() {
		return inProgress;
	}

	/**
	 * Updates the observers about the state of this level.
	 */
	private void updateObservers() {
		if (!isAnyPlayerAlive()) {
			for (LevelObserver o : observers) {
				o.levelLost();
			}
		}
		if (remainingPellets() == 0) {
			for (LevelObserver o : observers) {
				o.levelWon();
			}
		}
		if (anyPlayerDesserveFruits()) {
			for (LevelObserver o : observers) {
					o.fruitEvent();
			}
		}
		if (isAnyPlayerShooting()) {
			for (LevelObserver o : observers) {
					o.ShootingEvent();
			}
		}
		List<NPC> deadNPCs = NPCToClean() ;
		if(deadNPCs.size() > 0) {
			for (LevelObserver o : observers) {
				o.NPCCleanEvent(deadNPCs, npcs);
			}
		}
	}

	/**
	 * Returns <code>true</code> iff at least one of the players in this level
	 * is alive.
	 * 
	 * @return <code>true</code> if at least one of the registered players is
	 *         alive.
	 */
	public boolean isAnyPlayerAlive() {
		for (Player p : players) {
			if (p.isAlive()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns <code>true</code> if at lest one of the player meet the criteria for making a fruit appear.
	 * 
	 * @return <code>true</code> if at lest one of the player has 500 or 1500 points.
	 */
	public boolean anyPlayerDesserveFruits() {
		for (Player p : players) {
			if (fruitFactory != null && (p.getScore() == 500 || p.getScore() == 1500)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns <code>true</code> if at lest one of the player can shoot bullets.
	 * 
	 * @return <code>true</code> if at lest one of the player can shoot bullets.
	 */
	public boolean isAnyPlayerShooting() {
		for (Player p : players) {
			if (p.isShooting()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns <code>true</code> if at least one NPC is dead and need to be cleaned from the board.
	 * 
	 * @return <code>true</code> if at least one NPC is dead and need to be cleaned from the board.
	 */
	private List<NPC> NPCToClean() {
		List<NPC> deadNPCs = new ArrayList<>();
		for (NPC npc : npcs.keySet()) {
			if (((npc instanceof Bullet) && !((Bullet) npc).isAlive())
					|| ((npc instanceof Ghost) && ((Ghost) npc).hasExploded())) {
				deadNPCs.add(npc);
			}
		}
		return deadNPCs;
	}

	/**
	 * Counts the pellets remaining on the board.
	 * 
	 * @return The amount of pellets remaining on the board.
	 */
	public int remainingPellets() {
		Board b = getBoard();
		int pellets = 0;
		for (int x = 0; x < b.getWidth(); x++) {
			for (int y = 0; y < b.getHeight(); y++) {
				for (Unit u : b.squareAt(x, y).getOccupants()) {
					if (u instanceof Pellet) {
						pellets++;
					}
				}
			}
		}
		return pellets;
	}
	
	/**
	 * returns the fruit factory for this level.
	 * @return the fruit factory for this level.
	 */
	public FruitFactory getFruitFactory(){
		return fruitFactory;
	}

	/**
	 * A task that moves an NPC and reschedules itself after it finished.
	 * 
	 * @author Jeroen Roosen 
	 */
	private final class NpcMoveTask implements Runnable {

		/**
		 * The service executing the task.
		 */
		private final ScheduledExecutorService service;

		/**
		 * The NPC to move.
		 */
		private final NPC npc;

		/**
		 * Creates a new task.
		 * 
		 * @param s
		 *            The service that executes the task.
		 * @param n
		 *            The NPC to move.
		 */
		private NpcMoveTask(ScheduledExecutorService s, NPC n) {
			this.service = s;
			this.npc = n;
		}

		@Override
		public void run() {
			Direction nextMove = npc.nextMove();
			if (nextMove != null) {
				move(npc, nextMove);
			}
			long interval = npc.getInterval();
			service.schedule(this, interval, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * An observer that will be notified when the level is won or lost.
	 * 
	 * @author Jeroen Roosen 
	 */
	public interface LevelObserver {

		/**
		 * The level has been won. Typically the level should be stopped when
		 * this event is received.
		 */
		void levelWon();

		/**
		 * The level has been lost. Typically the level should be stopped when
		 * this event is received.
		 */
		void levelLost();
		
		/**
		 * A Fruit can appear.
		 */
		void fruitEvent();
		
		/**
		 * A Player can shoot bullets
		 */
		void ShootingEvent();
		
		/**
		 * A NPC is dead and need to be cleared from the board
		 * @param List<NPC> deadNPC the list of the NPCs that are dead
		 * @param Map<NPC, ScheduledExecutorService> npcs the npcs that are still in the game.
		 */
		void NPCCleanEvent(List<NPC> deadNPCs, Map<NPC, ScheduledExecutorService> npcs);
	}

	/**
	 * enable the movment of a bullet
	 * @param b the bullet that have to be moved.
	 */
	public void animateBullet(Bullet b) {
			ScheduledExecutorService service = Executors
					.newSingleThreadScheduledExecutor();
			service.schedule(new NpcMoveTask(service, b),
					b.getInterval() / 2, TimeUnit.MILLISECONDS);
			npcs.put(b, service);
	}
}
