/*Nashita Bhuiyan
 * Pacman 
 * foundation of the classic arcade game Pacman
 */
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;


public class NashitaBhuiyan_PacmanGame extends JFrame implements KeyListener {

	private enum STATUS {PACMAN,EMPTY,PELLET,BLOCKED, GHOST};
	private enum DIR {LEFT,RIGHT,UP,DOWN};
	
	private final int NUM_ROWS = 20;
	private final int NUM_COLS = 18;
	
	private  BufferedImage[] PAC_IMAGE;			//left, right, up, down
	private  BufferedImage PELLET_IMAGE=null;;
	private  BufferedImage[] GHOST;

	private PicPanel[][] allPanels;

	private int pacRow = 1;						//location of pacman
	private int pacCol = 1;
	
	private int numPellets = 0;//keeps track of all the pellets still visible, if all gone game ends
	
	private Timer timer;
	private int blueGhostRow = 1;
	private int blueGhostCol = 5;
	private int redGhostRow = 18;
	private int redGhostCol = 16;
	private int pinkGhostRow = 1;
	private int pinkGhostCol = 16;
	
	public NashitaBhuiyan_PacmanGame(){

		PAC_IMAGE = new BufferedImage[4];
		GHOST = new BufferedImage[3];
		
		try {

			PAC_IMAGE[0] = ImageIO.read(new File("pac_left.jpg"));
			PAC_IMAGE[1] = ImageIO.read(new File("pac_right.jpg"));
			PAC_IMAGE[2] = ImageIO.read(new File("pac_up.jpg"));
			PAC_IMAGE[3] = ImageIO.read(new File("pac_down.jpg"));
			PELLET_IMAGE = ImageIO.read(new File("pellet.png"));
			GHOST[0] = ImageIO.read(new File("blueGhost.jpg"));
			GHOST[1] = ImageIO.read(new File("redGhost.png"));
			GHOST[2] = ImageIO.read(new File("pinkGhost.png"));

		} catch (IOException ioe) {
			System.out.println("Could not read pacman pics");
			System.exit(0);
		}

		setSize(600,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Pacman");
		setLayout(new GridLayout(NUM_ROWS,NUM_COLS));
		allPanels = new PicPanel[NUM_ROWS][NUM_COLS];

		//create the maze based on information read in from a file
		Scanner fileIn = null;
		try {	
			fileIn = new Scanner(new File("maze.txt"));
		}catch (FileNotFoundException e) {
			System.out.println("File Not found");
		}
		
		for(int row=0; row<allPanels.length; row++) {
			String valRow = fileIn.nextLine();
			for(int col=0; col<allPanels[0].length; col++) {
				
					allPanels[row][col] = new PicPanel(valRow.substring(col,col+1));
					add(allPanels[row][col]);
			}
		}
		
		//place Pacman in the initial position ([1,1]) and direction (Right) in the maze
		allPanels[pacRow][pacCol].addPacman(DIR.RIGHT);
		
		allPanels[blueGhostRow][blueGhostCol].addGhost("blue");
		allPanels[redGhostRow][redGhostCol].addGhost("red");
		allPanels[pinkGhostRow][pinkGhostCol].addGhost("pink");
		
		//allow ghost to move on own
		timer = new Timer(200, new ActionListener(){
			
			private STATUS bluePrev = STATUS.PELLET;//holds whatever was on board before ghost took its place
			private int[] blueToAdd = new int[2];//stores what must be added to current ghost location according to current direction
			
			private STATUS redPrev = STATUS.PELLET;
			private int[] redToAdd = new int[2];
			
			private STATUS pinkPrev = STATUS.PELLET;
			private int[] pinkToAdd = new int[2];
			
			public void actionPerformed(ActionEvent ae) {
		
				//change/assign direction of ghost when newly created/hit a wall
				while(allPanels[blueGhostRow+blueToAdd[0]][blueGhostCol+blueToAdd[1]]==allPanels[blueGhostRow][blueGhostCol] || allPanels[blueGhostRow+blueToAdd[0]][blueGhostCol+blueToAdd[1]].panelStat==STATUS.BLOCKED) {
					
					blueToAdd = ghostDir(blueGhostRow, blueGhostCol);
				}
				while(allPanels[redGhostRow+redToAdd[0]][redGhostCol+redToAdd[1]]==allPanels[redGhostRow][redGhostCol] || allPanels[redGhostRow+redToAdd[0]][redGhostCol+redToAdd[1]].panelStat==STATUS.BLOCKED) {
					
					redToAdd = ghostDir(redGhostRow, redGhostCol);
				}
				while(allPanels[pinkGhostRow+pinkToAdd[0]][pinkGhostCol+pinkToAdd[1]]==allPanels[pinkGhostRow][pinkGhostCol] || allPanels[pinkGhostRow+pinkToAdd[0]][pinkGhostCol+pinkToAdd[1]].panelStat==STATUS.BLOCKED) {
					
					pinkToAdd = ghostDir(pinkGhostRow, pinkGhostCol);
				}

				//remove ghost from old position and replace with what was previously there
				clearGhost(blueGhostRow, blueGhostCol, bluePrev);
				clearGhost(redGhostRow, redGhostCol, redPrev);
				clearGhost(pinkGhostRow, pinkGhostCol, pinkPrev);

				//place ghost in new position and store what was there originally
				blueGhostRow+=blueToAdd[0];
				blueGhostCol+=blueToAdd[1];
				bluePrev = allPanels[blueGhostRow][blueGhostCol].panelStat;
				allPanels[blueGhostRow][blueGhostCol].addGhost("blue");
				
				redGhostRow+=redToAdd[0];
				redGhostCol+=redToAdd[1];
				redPrev = allPanels[redGhostRow][redGhostCol].panelStat;
				allPanels[redGhostRow][redGhostCol].addGhost("red");
				
				pinkGhostRow+=pinkToAdd[0];
				pinkGhostCol+=pinkToAdd[1];
				pinkPrev = allPanels[pinkGhostRow][pinkGhostCol].panelStat;
				allPanels[pinkGhostRow][pinkGhostCol].addGhost("pink");
			
				//end game when ghost eats pacman
				if(pinkPrev==STATUS.PACMAN || redPrev==STATUS.PACMAN || bluePrev==STATUS.PACMAN){
					
						timer.stop();
						endGame("You lose. Try again next time!");
				}
			}
		});
		
		this.addKeyListener(this);
		setVisible(true);
	}
	
	//help remove ghost from old position
	private void clearGhost(int ghostRow, int ghostCol, STATUS prev) {
		
		allPanels[ghostRow][ghostCol].clearPanel();
		if(prev==STATUS.PELLET) {
		
			allPanels[ghostRow][ghostCol].panelStat = STATUS.PELLET;
			allPanels[ghostRow][ghostCol].image = PELLET_IMAGE;;
		}
	}
	
	//help find new direction of movement for a ghost
	private int[] ghostDir(int ghostRow, int ghostCol) {
		
			int[] toAdd = new int[2];
			
			//randomly assign the ghost a direction to move in and store the direction
			int ghostDir = (int)(Math.random()*4);
			
			//left
			if(ghostDir==0) {
				
				toAdd[0] = 0;
				toAdd[1] = -1;
			}
			
			//right
			else if(ghostDir==1) {
				
				toAdd[0] = 0;
				toAdd[1] = 1;
			}
			
			//up
			else if(ghostDir==2) {
				
				toAdd[0] = -1;
				toAdd[1] = 0;
			}
			
			//down
			else {
				
				toAdd[0] = 1;
				toAdd[1] = 0;
			}
			
			return toAdd;
	}
	
	//message displayed to user after game has ended
	public void endGame(String message) {
		
		this.removeKeyListener(this);
		JOptionPane.showMessageDialog(null, message);//add more fluorish
	}

	//change Pacman's position and direction in the maze based on keys pressed
	public void keyPressed(KeyEvent ke) {
		
		timer.start();
		int keyval = ke.getKeyCode();
		int oldPacRow  = pacRow;
		int oldPacCol = pacCol;
		DIR pacmanDir;
		
		//update Pacman location and direction its facing
		if(keyval==KeyEvent.VK_DOWN && pacRow<NUM_ROWS-1 && allPanels[pacRow+1][pacCol].panelStat!=STATUS.BLOCKED) {
			pacRow++;
			pacmanDir=DIR.DOWN;
		}
		else if(keyval==KeyEvent.VK_UP && pacRow>0 && allPanels[pacRow-1][pacCol].panelStat!=STATUS.BLOCKED) {
			pacRow--;
			pacmanDir=DIR.UP;
		}
		else if(keyval==KeyEvent.VK_RIGHT && pacCol<NUM_COLS-1 && allPanels[pacRow][pacCol+1].panelStat!=STATUS.BLOCKED) {
			pacCol++;
			pacmanDir=DIR.RIGHT;
		}
		else if(keyval==KeyEvent.VK_LEFT && pacCol>0 && allPanels[pacRow][pacCol-1].panelStat!=STATUS.BLOCKED) {
			pacCol--;
			pacmanDir=DIR.LEFT;
		}
		else {
			return;
		}
		
		//remove Pacman from its old location and place it in new location
		allPanels[oldPacRow][oldPacCol].clearPanel();
		
		//keep track if pacman has eaten a pellet
		if(allPanels[pacRow][pacCol].panelStat!=STATUS.GHOST && allPanels[pacRow][pacCol].panelStat!=STATUS.EMPTY) {
			numPellets--;
			System.out.println(numPellets);
		}	
		allPanels[pacRow][pacCol].addPacman(pacmanDir);
		
		//check if there was a collision with a ghost(loss)
		if(allPanels[pacRow][pacCol].panelStat==STATUS.GHOST) {
			timer.stop();
			endGame("You lose. Try again next time!");
		}
		
		//win
		if(numPellets==1) {
			timer.stop();
			endGame("Winner!");
		}
			
	}

	public void keyReleased(KeyEvent ke) {
	}

	public void keyTyped(KeyEvent ke) {		
	}
	
	class PicPanel extends JPanel{

		private BufferedImage image;		
		private STATUS panelStat;
		
		//takes in a single val from the file (either "x" or "o")
		public PicPanel(String val){
			if(val.equals("o")) {
				panelStat = STATUS.PELLET;
				image = PELLET_IMAGE;
				
				//take note of all the pellets on the board
				numPellets++;
			}
			else
				panelStat = STATUS.BLOCKED;
		}

		public void clearPanel() {
			image = null;
			panelStat = STATUS.EMPTY;
			repaint();
		}
		
		public void addPacman(DIR direction) {
			image = PAC_IMAGE[direction.ordinal()];
			panelStat = STATUS.PACMAN;
			repaint();
		}
		
		public void addGhost(String color) {
			
			if(color.equals("blue")) {
				image = GHOST[0];
			}
			else if(color.equals("red")) {
				image = GHOST[1];
			}
			else {
				image = GHOST[2];
			}
			
			panelStat = STATUS.GHOST;
			repaint();
		}
		
		//this will draw the image
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			if(panelStat == STATUS.EMPTY)
				setBackground(Color.black);
			else if(panelStat == STATUS.BLOCKED)
				setBackground(Color.blue);
			else
				g.drawImage(image,0,0,this);			
		}
	}
	
	public static void main(String[] args){
		new NashitaBhuiyan_PacmanGame();
	}
}
