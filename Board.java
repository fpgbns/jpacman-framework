package nl.tudelft.jpacman.board;

import java.util.List;
import java.util.Random;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.level.Level;

/**
 * A top-down view of a matrix of {@link Square}s.
 *
 * @author Jeroen Roosen
 */
public class Board {

	/**
	 * The grid of squares with board[x][y] being the square at column x, row y.
	 */
	private Square[][] board;

	private int sectionSizeX;
	private int sectionSizeY;

	/**
	 * Creates a new board.
	 *
	 * @param grid The grid of squares with grid[x][y] being the square at column
	 *             x, row y.
	 */
	Board(Square[][] grid) {
		assert grid != null;
		this.board = grid;
		this.sectionSizeX = grid.length;
		this.sectionSizeY = grid[0].length;
		assert invariant() : "Initial grid cannot contain null squares";
	}

	/**
	 * Whatever happens, the squares on the board can't be null.
	 *
	 * @return false if any square on the board is null.
	 */
	public boolean invariant() {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				if (board[x][y] == null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns the width of this board, i.e. the amount of columns.
	 *
	 * @return The width of this board.
	 */
	public int getWidth() {
		return board.length;
	}

	/**
	 * Returns the height of this board, i.e. the amount of rows.
	 *
	 * @return The height of this board.
	 */
	public int getHeight() {
		return board[0].length;
	}

	/**
	 * Returns the square at the given <code>x,y</code> position.
	 *
	 * @param x The <code>x</code> position (column) of the requested square.
	 * @param y The <code>y</code> position (row) of the requested square.
	 * @return The square at the given <code>x,y</code> position (never null).
	 */
	public Square squareAt(int x, int y) {
		assert withinBorders(x, y);
		Square result = board[x][y];
		assert result != null : "Follows from invariant.";
		return result;
	}

	/**
	 * Determines whether the given <code>x,y</code> position is on this board.
	 *
	 * @param x The <code>x</code> position (row) to test.
	 * @param y The <code>y</code> position (column) to test.
	 * @return <code>true</code> iff the position is on this board.
	 */
	public boolean withinBorders(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}

	public void extend(Direction direction) {
		Square[][] grid = null;
		int expandSize = 5;
		if (direction == Direction.EAST || direction == Direction.WEST)
			expandSize = sectionSizeX;
		else
			expandSize = sectionSizeY;
		grid = new Square[this.getWidth() + Math.abs(direction.getDeltaX() * expandSize)][this.getHeight() + Math.abs(direction.getDeltaY() * expandSize)];
		if (direction == Direction.EAST) {
			this.boardCopy(board, grid, 0, 0);
			this.createSquare(grid, board.length, 0, board.length + expandSize, board[0].length);
			this.setLink(grid, board.length - 1, 0, expandSize, getHeight());
		}else if (direction == Direction.NORTH) {
			this.boardCopy(board, grid, 0, expandSize);
			this.createSquare(grid, 0, 0, board.length, expandSize);
			this.setLink(grid, 0, 0, getWidth(), expandSize + 1);
		}else if (direction == Direction.SOUTH) {
			this.boardCopy(board, grid, 0, 0);
			this.createSquare(grid, 0, board[0].length, board.length, board[0].length + expandSize);
			this.setLink(grid, 0, board[0].length - 1, getWidth(), expandSize);

		} else{
			this.boardCopy(board, grid, expandSize, 0);
			this.createSquare(grid, 0, 0, expandSize, board[0].length);
			this.setLink(grid, 0, 0, expandSize + 1, getHeight());
		}
		this.setPositions(grid);
		this.board = grid;

	}

	private void boardCopy(Square[][] originalBoard, Square[][] newBoard, int width, int height)
	{
		for (int i = 0; i < originalBoard.length; i++)
		{
			for (int j = 0; j < originalBoard[0].length; j++)
			{
				newBoard[i + width][j + height] = originalBoard[i][j];
			}
		}
	}

	private void setPositions(Square[][] grid)
	{
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[0].length; j++)
			{
				grid[i][j].setCoord(i, j);
			}
		}
	}

	private void createSquare(Square[][] grid, int _x, int _y, int dx, int dy) {
		int nbx = (dx-_x)/sectionSizeX;
		int nby = (dy-_y)/sectionSizeY;
		for(int nx=0; nx<nbx; nx++) {
			for(int ny=0; ny<nby; ny++) {
				Level l = Launcher.getLauncher().makeLevel();
				//Level.getInstance().addNPCs(l);
				//System.out.println("Nb Ghosts : " + Level.getInstance().getNpcs().size());
				Square[][] newMap = l.getBoard().board;
				for(int x=0; x<newMap.length; x++) {
					for(int y=0; y<newMap[0].length; y++) {
						grid[_x+(nx*sectionSizeX)+x][_y+(ny*sectionSizeY)+y] = newMap[x][y];
					}
				}
			}
		}
	}

	private void setLink(Square[][] grid, int startX, int startY, int endX, int endY)
	{
		for (int i = startX; i < startX + endX; i++)
		{
			for (int j = startY; j < startY + endY; j++)
			{
				Square sq1 = grid[i][j];
				for (Direction direction : Direction.values())
				{
					int x = (grid.length + i + direction.getDeltaX()) % grid.length;
					int y = (grid[0].length + j + direction.getDeltaY()) % grid[0].length;
					Square sq2 = grid[x][y];
					sq1.link(sq2, direction);
				}
			}
		}
	}
}
