/*Nashita Bhuiyan
 * Tic-Tac-Toe
 * program that facilitates tic-tac-toe across a network
 */
import java.net.*;
import java.util.*;
import java.io.*;

public class Server {

	private char[][] board;					// the board
	private int turns;						// the total number of turns taken
	private char[] marks ={'X','O'};		// X is always the first player
	private Scanner[] readers;
	private PrintWriter[] writers;
	
	public Server(){
		board = new char[3][3];
		readers = new Scanner[2];
		writers = new PrintWriter[2];
	}

	public void go(){
		
			//Set up the server socket, print IP and port info 
			ServerSocket server;
			Socket player1;
			Socket player2;
			try {
				server = new ServerSocket(4242);
				System.out.println(InetAddress.getLocalHost().getHostAddress());
				
				//Accept two players and set up the readers and writers
				player1 = server.accept();
				player2 = server.accept();
				
				readers = new Scanner[2];
				readers[0] = new Scanner(new InputStreamReader(player1.getInputStream()));
				readers[1] = new Scanner(new InputStreamReader(player2.getInputStream()));

				writers = new PrintWriter[2];
				writers[0] = new PrintWriter(player1.getOutputStream());
				writers[1] = new PrintWriter(player2.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			//Randomly determine who goes first
			if((int)(Math.random()*2) ==1){
				PrintWriter temp = writers[0];
				writers[0] = writers[1];
				writers[1] = temp;
				Scanner hold = readers[0];
				readers[0]=readers[1];
				readers[1]=hold;
			}

			//Get the names of the two players
			String moving = readers[0].nextLine();
			String waiting = readers[1].nextLine();
			
			//Send the names to opposing player and indicate who goes first
			writeMessage(writers[0], STATUS.GO, waiting);
			writeMessage(writers[1], STATUS.DRAW, moving);
			
			String move="";
			STATUS stat = STATUS.GO;
			do{
				
				turns++;
				
				//Get move from next player and mark the players move on the board
				move = readers[(turns+1)%2].nextLine();
				String[] position = move.split(",");
				board[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = marks[(turns+1)%2];
				
				//figure out if the game should still go on
				stat = gameOver(marks[(turns+1)%2]);
				
				//send STATUS and opposing players move
				writeMessage(writers[turns%2], stat, move);
			}while(stat == STATUS.GO);

			//send final message to player
			writeMessage(writers[(turns+1)%2], stat, "You won!");
	}

	//writes a socket with status, followed by a message. All seperated by commas.
	private void writeMessage(PrintWriter writer,STATUS stat, String message){
		writer.println(stat.ordinal()+","+message);
		writer.flush();
	}

	//determines if the game is over
	//sends back Lose or Draw if it is over
	//sends back GO if there is no winner
	private STATUS gameOver(char mark){
		
		int total =0;
		int diag = 0;
		int backDiag = 0;
		boolean[] skipCol = new boolean[board.length];
		
		//check rows and diagonals
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board.length; c++) {
				
				total++;
				
				if(board[r][c]!=mark) {
					
					total = 0;
					skipCol[r] = true;
					c=board.length;
				}
				else if(r==c) {
					diag++;
				}
				else if(r+c==(board.length-1) && r*c==0) {
					backDiag++;
				}
				else if(total==board.length || diag==board.length || backDiag==board.length){
					return STATUS.WON;
				}
			}
		}
		
		//check columns
		for(int c=0; c<board.length; c++) {
			for(int r=0; r<board.length; r++) {
				
				total++;
				if(skipCol[c]==true) {
					r=board.length;
					total = 0;
				}
				else if(total==3){
					return STATUS.WON;
				}
			}
		}
		
		//if all cells are filled and there is no winner
		if(turns==9)
			return STATUS.DRAW;
		
		return STATUS.GO;
	}
	public static void main(String[] args){
		new Server().go();
	}
}
