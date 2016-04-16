package nl.tudelft.jpacman.board;

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

	private int widthOfOneMap;
	private int heightOfOneMap;

	/**
	 * Creates a new board.
	 *
	 * @param grid The grid of squares with grid[x][y] being the square at column
	 *             x, row y.
	 */
	Board(Square[][] grid) {
		assert grid != null;
		this.board = grid;
		this.widthOfOneMap = grid.length;
		this.heightOfOneMap = grid[0].length;
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

	public void extend(Direction direction)
	{
		Square[][] grid = null;
		switch(direction) {
			case EAST:
				grid = new Square[this.getWidth() + Math.abs(direction.getDeltaX() * this.widthOfOneMap)][this.getHeight() + Math.abs(direction.getDeltaY() * this.widthOfOneMap)];
				this.boardCopy(board, grid, 0, 0);
				this.createSquare(grid, this.getWidth(), 0, this.getWidth() + this.widthOfOneMap, this.getHeight());
				this.setLink(grid, this.getWidth() - 1, 0, this.widthOfOneMap, this.getHeight());
				break;
			case NORTH:
				grid = new Square[this.getWidth() + Math.abs(direction.getDeltaX() * this.heightOfOneMap)][this.getHeight() + Math.abs(direction.getDeltaY() * this.heightOfOneMap)];
				this.boardCopy(board, grid, 0, this.heightOfOneMap);
				this.createSquare(grid, 0, 0, this.getWidth(), this.heightOfOneMap);
				this.setLink(grid, 0, 0, this.getWidth(), this.heightOfOneMap + 1);
				break;
			case SOUTH:
				grid = new Square[this.getWidth() + Math.abs(direction.getDeltaX() * this.heightOfOneMap)][this.getHeight() + Math.abs(direction.getDeltaY() * this.heightOfOneMap)];
				this.boardCopy(board, grid, 0, 0);
				this.createSquare(grid, 0, this.getHeight(), this.getWidth(), this.getHeight() + this.heightOfOneMap);
				this.setLink(grid, 0, this.getHeight() - 1, this.getWidth(), this.heightOfOneMap);
				break;
			case WEST:
				grid = new Square[this.getWidth() + Math.abs(direction.getDeltaX() * this.widthOfOneMap)][this.getHeight() + Math.abs(direction.getDeltaY() * this.widthOfOneMap)];
				this.boardCopy(board, grid, this.widthOfOneMap, 0);
				this.createSquare(grid, 0, 0, this.widthOfOneMap, this.getHeight());
				this.setLink(grid, 0, 0, this.widthOfOneMap + 1, this.getHeight());
				break;
			default:
				break;
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

	public void setPositions(Square[][] grid)
	{
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[0].length; j++)
			{
				grid[i][j].setCoord(i, j);
			}
		}
	}

	private void createSquare(Square[][] grid, int startX, int startY, int endX, int endY)
	{
		for(int i = 0; i < (endX - startX) / this.widthOfOneMap; i++)
		{
			for(int j = 0; j < (endY - startY) / this.heightOfOneMap; j++)
			{
				Launcher launcher = Launcher.getLauncher();
				launcher.setBoardToUse("/boardExtendedAdd.txt");
				Level lev = launcher.makeLevel();
				Level game = Level.getLevel();
				game.addGhost(lev);
				this.setSquare(grid, lev.getBoard().board, startX, startY, i, j);
			}
		}
	}

	private void setSquare(Square[][] grid, Square[][] newGrid, int startX, int startY, int x, int y)
	{
		for(int i = 0; i < newGrid.length; i++)
		{
			for(int j = 0; j < newGrid[0].length; j++)
			{
				grid[startX + (x*this.widthOfOneMap) + i][startY + (y*this.heightOfOneMap) + j] = newGrid[i][j];
			}
		}
	}

	private void setLink(Square[][] grid, int startX, int startY, int endX, int endY)
	{
		Square sq1, sq2;
		int x, y;
		for (int i = startX; i < startX + endX; i++)
		{
			for (int j = startY; j < startY + endY; j++)
			{
				sq1 = grid[i][j];
				for (Direction direction : Direction.values())
				{
					x = (grid.length + i + direction.getDeltaX()) % grid.length;
					y = (grid[0].length + j + direction.getDeltaY()) % grid[0].length;
					sq2 = grid[x][y];
					sq1.link(sq2, direction);
				}
			}
		}
	}

	public Square[][] getBoard() {
		return board;
	}
}