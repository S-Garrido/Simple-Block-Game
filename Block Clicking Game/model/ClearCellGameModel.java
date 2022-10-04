package model;

import java.util.Random;

/**
 * This class extends GameModel and implements the logic of the clear cell game,
 * specifically.
 * 
 * @author Dept of Computer Science, UMCP
 */

public class ClearCellGameModel extends GameModel { //Handles game rules
	
	private int score = 0;
	private Random randomNum;
	private int emptyCells = 0;	//counts empty cells in a row
	
	/**
	 * Defines a board with empty cells.  It relies on the
	 * super class constructor to define the board.
	 * 
	 * @param rows number of rows in board
	 * @param cols number of columns in board
	 * @param random random number generator to be used during game when
	 * rows are randomly created
	 */
	public ClearCellGameModel(int rows, int cols, Random random) { //Defines empty board 
		super(rows, cols);
		randomNum = random;
	}

	/**
	 * The game is over when the last row (the one with index equal
	 * to board.length-1) contains at least one cell that is not empty.
	 */
	public boolean isGameOver() { //logic to see if game is over
		for(int col = 0; col < board[0].length ; col++) {
			if(board[board.length - 1][col] != BoardCell.EMPTY) { //If the one cell isn't empty, game over
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the player's score.  The player should be awarded one point
	 * for each cell that is cleared.
	 * 
	 * @return player's score
	 */
	public int getScore() { //returns score
		return score;
	}

	
	/**
	 * This method must do nothing in the case where the game is over.
	 * 
	 * As long as the game is not over yet, this method will do 
	 * the following:
	 * 
	 * 1. Shift the existing rows down by one position.
	 * 2. Insert a row of random BoardCell objects at the top
	 * of the board. The row will be filled from left to right with cells 
	 * obtained by calling BoardCell.getNonEmptyRandomBoardCell().  (The Random
	 * number generator passed to the constructor of this class should be
	 * passed as the argument to this method call.)
	 */
	public void nextAnimationStep() { //While game isn't over, this continues the specified animation
		if(!isGameOver()) {
			BoardCell[][] newBoard;
			newBoard = new BoardCell[board.length][board[0].length]; //Deep Copy
			for(int row = 0; row < board.length; row ++) {
				for(int col = 0; col < board[row].length; col ++) {
					if(row == 0 ) {
						newBoard[row][col] = BoardCell.getNonEmptyRandomBoardCell(randomNum) ;
					} else {
						newBoard[row][col] = board [row - 1][col];
					}
				}
			}
			board = newBoard;
		}
	}

	/**
	 * This method is called when the user clicks a cell on the board.
	 * If the selected cell is not empty, it will be set to BoardCell.EMPTY, 
	 * along with any adjacent cells that are the same color as this one.  
	 * (This includes the cells above, below, to the left, to the right, and 
	 * all in all four diagonal directions.)
	 * 
	 * If any rows on the board become empty as a result of the removal of 
	 * cells then those rows will "collapse", meaning that all non-empty 
	 * rows beneath the collapsing row will shift upward. 
	 * 
	 * @throws IllegalArgumentException with message "Invalid row index" for 
	 * invalid row or "Invalid column index" for invalid column.  We check 
	 * for row validity first.
	 */
	public void processCell(int rowIndex, int colIndex) { //Processes the cells when player clicks on one
	if(rowIndex < 0 || rowIndex > board.length ) { //Row out of bounds
		throw new IllegalArgumentException("Invalid row index");
	}
	if(colIndex < 0 || colIndex > board[0].length ) { //Column out of bounds
		throw new IllegalArgumentException("Invalid column index");
	}
	if(board[rowIndex][colIndex] != BoardCell.EMPTY) {
		BoardCell[][] newBoard;
		newBoard = new BoardCell[board.length][board[0].length]; //Deep Copy
			
		for(int row = 0; row < newBoard.length; row ++) { //Go through grid to empty cells
			for(int col = 0; col < newBoard[row].length; col ++) {
				if((row - rowIndex) > (-2) && (row - rowIndex) < 2 && (col - colIndex) > (-2) && (col - colIndex) < 2 && board[row][col].getColor() == board[rowIndex][colIndex].getColor()) {
					newBoard[row][col] = BoardCell.EMPTY; //Empties selected and adjacent squares of same color
					score += 1;
				} else {
					newBoard[row][col] = board[row][col];
				}
				}
			}	
		for(int k = 0; k < 3; k++) { //Check the entire board 3 times to handle a triple collapse (maximum collapse)
		for(int row = 0; row < newBoard.length; row ++) { //Go through grid to find empty rows	
			for(int col = 0; col < newBoard[row].length; col ++) {
				if(newBoard[row][col] == BoardCell.EMPTY) {
					emptyCells += 1;
					}
				if(emptyCells == newBoard[row].length) {
						for(int i = row; i < newBoard.length; i ++) { //Collapse	
							for(int j = 0; j < board[i].length; j ++) {
								if(i == (newBoard.length - 1)) {
									newBoard[i][j] = BoardCell.EMPTY; //Avoiding out of bounds error
								} else {
								newBoard[i][j] = newBoard[i+1][j];
								}
							}
							}
					}
			}
			emptyCells = 0;
		}
		}
			board = newBoard;
		}
	}

}