/*Nashita Bhuiyan
 * Tic-Tac-Toe, with graphics
 */

//packages and toolkits
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphicTicTacToe extends JFrame{

	private int turn; //0 = player X; 1 = player O
	private JLabel currentPlayer;//display name of player whose turn it is, or out come of game
	
	private String[] players; //holds the names of the two players
	private PicPanel[][] gameBoard;
	private int numPlays;//records number of total turns made in the game
	
	public GraphicTicTacToe() {
		
		//prepare the model 
		setUpGame();
		
		//create frame
		setTitle("Tic Tac Toe");
		setSize(415, 540);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.black);
		setLayout(null);
		
		//create a panel to display player Turns
		JPanel turnBoard = new JPanel();
		turnBoard.setLayout(null);
		turnBoard.setBackground(new Color(181,217,247));
		turnBoard.setBounds(0,400,400,100);
		
		//add labels, indicating how each player will make a move and whose turn it is, to the turnBoard
		JLabel playerOne = new JLabel(players[0] + " (X)");
		playerOne.setBounds(20,10,200,20);
		JLabel playerTwo = new JLabel(players[1] + " (O)");
		playerTwo.setBounds(310,10,200,20);
		currentPlayer = new JLabel(players[0] + "'s Turn");
		currentPlayer.setBounds(160, 70, 200, 20);
		turnBoard.add(playerOne);
		turnBoard.add(playerTwo);
		turnBoard.add(currentPlayer);
		
		//create new panel to hold all the components of the tic-tac-toe board
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(3,3, 7, 7));
		grid.setBackground(Color.BLACK);
		grid.setBounds(0,0,400,410);
		
		//create and add the tic-tac-toe board to the GUI
		gameBoard = new PicPanel[3][3];
		for(int row=0; row<gameBoard.length; row++) {
			for(int col=0; col<gameBoard.length; col++) {
				
				gameBoard[row][col] = new PicPanel(row, col);
				grid.add(gameBoard[row][col]);
			}
		}

		//add all components to the frame and display the frame
		add(grid);
		add(turnBoard);
		setVisible(true);
	}
	
	//prepare gameBoard, first turn, and store the names of each player and how they will make a move (X or O)
	private void setUpGame() {
		
		//set the first player of the game to be X
		turn = 0;
		
		//Ask players for their names
		String firstEntry = JOptionPane.showInputDialog(null, "Enter a Player's Name");
		String secondEntry = JOptionPane.showInputDialog(null, "Enter a Player's Name");
	
		//randomly assign each player X or O (X goes first)
		int randomLoc = (int)(Math.random()*2);
		players = new String[2];
		players[randomLoc] = firstEntry;
		players[(randomLoc+1)%2] = secondEntry;
	}
	
	//update the game once a player makes a move
	private void updateGame(int panelRow, int panelCol) {
		
		//new text to be applied currentPlayer based on if the game was won, tied or ongoing
		String text;
		
		//Game has been won
		if(numPlays>=5 && checkWin(panelRow, panelCol)) {
			
			text = players[turn] + " Won";	
			
			//prevent players from making any more moves
			for(int r=0; r<gameBoard.length; r++) {
				for(int c=0; c<gameBoard.length; c++) {
					
					gameBoard[r][c].removeMouseListener(gameBoard[r][c]);
				}
			}
		}
		
		//Tie game
		else if(numPlays==9) {
			
			text = "Tie game.";
		}
		
		//Game is ongoing
		else {
			
			//update turn
			turn = (turn+1)%2;
			text = players[turn] + "'s Turn";
		}
		
		currentPlayer.setText(text);
	}
	
	//Check Who won the game if there is a winner
	private boolean checkWin(int currPanelRow, int currPanelCol) {
		
		//when times is two the game has been won
		int times = 0;
		
		//Row
		for(int col=0; col<gameBoard.length-1; col++) {
			if(gameBoard[currPanelRow][col].fName!=null && gameBoard[currPanelRow][col].fName==gameBoard[currPanelRow][col+1].fName)
				times++;
		}
		if(times==gameBoard.length-1) {
			return true;
		}

		//Column
		times = 0;
		for(int row=0; row<gameBoard.length-1; row++) {
			if(gameBoard[row][currPanelCol].fName!=null && gameBoard[row][currPanelCol].fName==gameBoard[row+1][currPanelCol].fName) 
				times++;
		}
		if(times==gameBoard.length-1) {
			return true;
		}
		
		//Backward Diagonal
		times = 0;
		for(int i=0; i<gameBoard.length-1; i++) {
			if(gameBoard[i][i].fName!=null  && gameBoard[i][i].fName==gameBoard[i+1][i+1].fName)
				times++;
		}
		if(times==gameBoard.length-1) {
			return true;
		}
		
		//Forward Diagonal
		times = 0;
		for(int i=0; i<gameBoard.length-1; i++) {
			if(gameBoard[i][gameBoard.length-1-i].fName!=null && gameBoard[i][gameBoard.length-1-i].fName==gameBoard[i+1][gameBoard.length-i-2].fName)
				times++;
		}
		if(times==gameBoard.length-1) {
			return true;
		}

		return false;
	}
	
	class PicPanel extends JPanel implements MouseListener{
		
		private int panelRow;//the row that the PicPanel occupies on the tic-tac-toe board 
		private int panelCol;//the column the PicPanel occupies on the tic-tac-toe board
		String fName;
		
		private BufferedImage image;

		public PicPanel(int r, int c){
			
			setBackground(Color.WHITE);
			this.addMouseListener(this);
			
			panelRow = r;
			panelCol = c;
		}
		
		//draw image
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			if(image!=null) {
				
				int x = (this.getWidth() - image.getWidth(null)) / 2;
				int y = (this.getHeight() - image.getHeight(null))/ 2;
				g.drawImage(image, x, y, null);
			}
		}

		//apply appropriate image to panel when a player clicks on it
		public void mouseClicked(MouseEvent e) {
		
			numPlays++;
			
			//apply appropriate image to panel
			if(turn==0) {
				fName = "x.png";
			}
			else {
				fName = "o.png";
			}
			
			try {
				image = ImageIO.read(new File(fName));
			}catch(IOException ioe) {
				image = null;
			}
			this.repaint();

			//make sure players can't reuse the same space
			this.removeMouseListener(this);
			
			//determine if move made has ended the game, or prepare the game for the next player
			updateGame(panelRow, panelCol);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
	
	public static void main(String[] args) {
		
		new GraphicTicTacToe();
	}

}
