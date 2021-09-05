/*Nashita Bhuiyan
 * Tic-Tac-Toe
 * allows players to play game of tic tac toe
 */

import java.util.*;
public class TicTacToe {
	public static void main(String[] args) {
		Random rand = new Random();
		
		
		/*VARIABLES*/
		String[][] playerNames;
		int turn = rand.nextInt(2);
		boolean win = false;
		
		
		/*CREATE BOARD MATRIX*/
		String[][] board = {
							{"", "", ""},
							{"", "", ""},
							{"", "", ""},
							};
		
		
		/*ASSIGN A LETTER TO EACH PLAYER*/
		playerNames = PlayerNames();
		
		
		/*START GAME*/
		System.out.println("*NEW GAME*");
		do {
			//determine whose turn it is
			turn = Turn(turn);
			
			//display important information to players befor they start their turn
			System.out.println(playerNames[turn][1] + "'s Turn: ");
			Display(board);
			
			//allow player to make their move
			move(board, playerNames[turn][0]);
		
		}while(!checkWin(board));
		
		
		/*LET PLAYERS KNOW WHO WON THE GAME*/
		System.out.println();
		System.out.println(playerNames[turn][1] + " WON!");
	}
	
	
	
	/*DISPLAY BOARD*/
	public static void Display(String[][] board) {
		for(int i=0; i<board.length; i++) {
			System.out.print("| ");
			for(int j=0; j<board[0].length; j++) {
				
				//print what is in each element in the matrix
				System.out.print(board[i][j] + " | ");
			}
			
			System.out.println();
			System.out.println("----------");
		}
	}
	
	
	
	/*ASK USER FOR THIER NAME AND ASSIGN THEM THE LETTER THEY WILL USE*/
	public static String[][] PlayerNames() {
		String[][] playerNames = new String[2][2];
		Scanner scan = new Scanner(System.in);
		
		
		//Player X
		playerNames[0][0] = "X";
		System.out.print("Player X: ");
		playerNames[0][1] = scan.nextLine();
		
		//Player O
		playerNames[1][0] = "O";
		System.out.print("Player O: ");
		playerNames[1][1] = scan.nextLine();
	
		
		return playerNames;
	}
	
	
	
	/*ALLOW PLAYER TO MAKE A MOVE*/
	public static void move(String[][] board, String letter) {
		Scanner scan = new Scanner(System.in);
		
		/*VARIABLES*/
		int row;
		int col;
		
		
		/*PROMPT USER TO SELECT THE ELEMENT THEY WANT TO TAKE*/
		//row
		System.out.print("Row: ");
		row = scan.nextInt();
		
		//column
		System.out.println("Column: ");
		col = scan.nextInt();
		
		
		/*UPDATE BOARD BASED ON MOVE*/
		board[row][col] = letter;
	}
	
	
	
	/*DETERMINE IF SOMEONE WON*/
	public static boolean checkWin(String[][] board) {
		/*EACH ROW AND COLUMN*/
		for(int i=0; i<3; i++) {
			//every row
			if(board[i][0] != "") {
				if(board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
					return true;
				}
			}
			
			//every column
			else if(board[0][i] != "") {
				if(board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
					return true;
				}
			}
		}
		
		
		/*CHECK DIAGONALS*/
		if(board[1][1] != "") {
			if(board[0][0] == board[1][1] && board[2][2] == board[1][1]) {
				return true;
			}
			
			else if(board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
				return true;
			}
		}
		
		
		return false;
	}
	
	
	
	/*DETERMINE WHOSE TURN IS NEXT*/
	public static int Turn(int turn) {
		int t = turn;
		
		
		if(turn == 0) {
			t++;
		}
		
		else if(turn == 1) {
			t--;
		}
		
		
		return t;
	}
}
