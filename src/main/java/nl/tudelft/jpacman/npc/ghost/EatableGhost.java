package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.Map;
import java.util.Random;



/**
 * Created by Yarol Timur on 15/03/2016.
 */
public class EatableGhost extends Ghost {

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval. 50% of the original speed.
     */
    private static final int MOVE_INTERVAL = 500;

    /**
     * Creates the news eatables Ghosts when the player eat a Super Pellet.
     *
     * @param spriteMap
     *            The sprites for these ghosts.
     */
    public EatableGhost(Map<Direction, Sprite> spriteMap) {
        super(spriteMap);
    }



    @Override
    public long getInterval() {
        return MOVE_INTERVAL + new Random().nextInt(INTERVAL_VARIATION);
    }


    @Override
    public Direction nextMove() {
        Direction d = randomMove();
        return d;
    }

}
