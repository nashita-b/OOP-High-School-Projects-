/*Nashita Bhuiyan
 * Pacman 
 * foundation of the classic arcade game Pacman
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;


public class PacMan extends JFrame implements KeyListener {

	private enum STATUS {PACMAN,EMPTY,PELLET,BLOCKED};
	private enum DIR {LEFT,RIGHT,UP,DOWN};
	
	private final int NUM_ROWS = 20;
	private final int NUM_COLS = 18;
	
	private  BufferedImage[] PAC_IMAGE;			//left, right, up, down
	private  BufferedImage PELLET_IMAGE=null;;

	private PicPanel[][] allPanels;

	private int pacRow =1;						//location of pacman
	private int pacCol =1;
	DIR pacmanDir;
	
	public PacMan(){

		PAC_IMAGE = new BufferedImage[4];
		
		try {

			PAC_IMAGE[0] = ImageIO.read(new File("pac_left.jpg"));
			PAC_IMAGE[1] = ImageIO.read(new File("pac_right.jpg"));
			PAC_IMAGE[2] = ImageIO.read(new File("pac_up.jpg"));
			PAC_IMAGE[3] = ImageIO.read(new File("pac_down.jpg"));
			PELLET_IMAGE = ImageIO.read(new File("pellet.png"));

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
		pacmanDir = DIR.RIGHT;
		allPanels[pacRow][pacCol].addPacman(pacmanDir);
		
		this.addKeyListener(this);
		setVisible(true);
	}

	//change Pacman's position and direction in the maze based on keys pressed
	public void keyPressed(KeyEvent ke) {
		
		int keyval = ke.getKeyCode();
		allPanels[pacRow][pacCol].clearPanel();
		
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
		
		//remove Pacman from its old location and place it in new location
		allPanels[pacRow][pacCol].addPacman(pacmanDir);
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
		new PacMan();
	}
}
