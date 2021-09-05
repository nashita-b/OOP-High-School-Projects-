/*Nashita Bhuiyan
 * Totoro Solver
 * an algorithm that solves the totoro puzzle from the initial board
 */
import java.util.*;
public class NashitaBhuiyan_TotoroSolver {
	
	private Board firstBoard;
	private static final int[] HORZ_DISP = {-1, 1, 0, 0};
	private static final int[] VERT_DISP = {0, 0, -1, 1};
	
	public NashitaBhuiyan_TotoroSolver(int[][] initial) {
		
		firstBoard = new Board(initial, 0, null);
	}
	
	public void search() {
		
		//create a queue containing the first version of the board
		PriorityQueue boards = new PriorityQueue<LinkedList>();
		boards.add(firstBoard);
		
		//store the row and column lengths
		int rowlength = firstBoard.curBoard.length;
		int collength = firstBoard.curBoard[0].length;
		
		//store the locations totoro has been previously
		int[][] totVisited = new int[rowlength][collength];

		//continue moving totoro as long as totoro does not move to a location it already visited or the game is over
		while(totVisited[((Board)boards.peek()).totLoc[0]][((Board)boards.peek()).totLoc[1]]!=1 && !((Board) boards.peek()).gameOver()) {
			
			//remove and print the board with highest priority
			Board prevBoard = (Board) boards.remove();
			prevBoard.print();
			
			//update totVisited (1 means visited, 0 means never visited)
			totVisited[prevBoard.totLoc[0]][prevBoard.totLoc[1]] = 1;
		
			//examine all the places totoro can move to 
			for(int variation=0; variation<HORZ_DISP.length; variation++) {
			
				int row = prevBoard.totLoc[0] +	HORZ_DISP[variation];
				int col = prevBoard.totLoc[1] +	VERT_DISP[variation];
				Board toAdd = null;
				
				//switch totoro's location 
				if(row>=0 && col>=0 && row<rowlength && col<collength) 
					toAdd = prevBoard.swap(row, col);
				
				if(toAdd!=null && toAdd!=prevBoard.previousBoard) 
					boards.add(toAdd);
			}
		}
		
		//print total number of moves required if winning state reached
		if(((Board) boards.peek()).gameOver()) {
			
			((Board)boards.remove()).print();
			System.out.println("Moves Required: " + ((Board) boards.peek()).movesMade);
		}
		
		//print first board if unsolvable
		else {
			
			firstBoard.print();
		}
	}

	public class Board implements Comparable<Board>{

		private int[][] curBoard;	//current puzzle configuration
		private int movesMade;		//number of moves made so far
		private Board previousBoard;	
		private int[] totLoc;		//Array of size 2. Location of totoro within the board
		private int manhattan;		//how far current state is from the final state
		
		public Board(int[][] board, int moves, Board prev) {
			
			//initialize matrix and totoro location array
			curBoard = new int[board.length][board[0].length];
			totLoc = new int[2];
			
			//load values from board array into curBoard
			for(int row=0; row<board.length; row++) {
				for(int col=0; col<board[0].length; col++) { 
					
					curBoard[row][col] = board[row][col];
					
					//find and store totoro's location within the matrix
					if(curBoard[row][col]==0) {
						
						totLoc[0] = row;
						totLoc[1] = col;
					}
				}
			}
						
			movesMade = moves;
			previousBoard = prev;
			
			//update manhattan value
			manhattan = 0;

			for(int row=0; row<board.length; row++) {
				for(int col=0; col<board[0].length; col++) {
					
					int value = curBoard[row][col]-1;
					
					int rowVal = Math.abs((value/curBoard.length) - row);
					int colVal= Math.abs((value%curBoard.length) - col);
					
					//do not add value to manhattan if currently on totoro's location
					if(totLoc[0]!=row || totLoc[1]!=col) 
						manhattan+=rowVal+colVal;
			
				}
			}
		}
		
		
		//returns new board with totoro's location swapped
		public Board swap(int row, int col) {
								
			//make deep copy of the curBoard
			int[][] swapped = new int[this.curBoard.length][this.curBoard[0].length];
			
			for(int r=0; r<swapped.length; r++) {
				for(int c=0; c<swapped[0].length; c++) {
					
					swapped[r][c] = this.curBoard[r][c];
				}
			}
			
			//swap totoro's location with another number
			swapped[this.totLoc[0]][this.totLoc[1]] = swapped[row][col];
			swapped[row][col] = 0;
			
			//create new board from newly swapped matrix
			Board swapBoard = new Board(swapped, movesMade+1, this);			
			return swapBoard;
		}
		
		//prints the content of the board 
		public void print() {
			
			for(int row=0; row<this.curBoard.length; row++) {
				for(int col=0; col<this.curBoard[row].length; col++) {
					
					System.out.print(this.curBoard[row][col] + " | ");
				}
				
				System.out.println();
			}
			
			System.out.println();
		}
		
		//returns true if all board pieces are in the correct order
		public boolean gameOver() {
			
			return manhattan==0;
		}
		
		public int compareTo(Board other) {

			return (this.manhattan+this.movesMade) - (other.manhattan+other.movesMade);
		}

		public boolean equals(Object other){

			if(!(other instanceof Board))
				return false;

			
			Board otherBoard = (Board)other;		
			return totLoc[0]==otherBoard.totLoc[0] && totLoc[1]==otherBoard.totLoc[1];
		}
	}
}
