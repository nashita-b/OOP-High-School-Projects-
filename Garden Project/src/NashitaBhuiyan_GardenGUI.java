/*Nashita Bhuiyan
 * Garden 
 * updates and displays a grid containing locations of flowers in a garden every time a button is pressed
 */
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


public class NashitaBhuiyan_GardenGUI extends JFrame implements ActionListener{

	private BufferedImage image;
	
	private PicPanel[][] allPanels;
	
	public  final int[] HORZDISP = {1,1,0,-1,-1,-1,1,0}; //neighborhoods
	public  final int[] VERTDISP = {0,1,1,1,-1,0,-1,-1};
	
	public NashitaBhuiyan_GardenGUI(){

		setSize(1000,850);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("Garden");
		getContentPane().setBackground(new Color(18,145,15));
		
		try {
			image = ImageIO.read(new File("fireflower.jpg"));
			
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Could not read in the pic");
			System.exit(0);
		}
		
		//create grid to add picPanels to 
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(8,8,5,5));
		grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		grid.setBackground(Color.BLACK);
		grid.setBounds(100,50,700,700);
		
		//add picPanels to grid
		createGarden(grid);
		
		//create button
		JButton step = new JButton("Step");
		step.setBounds(850, 350, 100, 100);
		step.addActionListener(this);
		
		//add components to frame and display
		add(grid);
		add(step);
		setVisible(true);
	}
	
	//create an 8x8 garden filled flowers at locations specified by a text file
	private void createGarden(JPanel grid) {
			
		Scanner fileIn = null;
			
		//open file
		try {
				
			fileIn = new Scanner(new File("flowerlocs.txt"));
		}catch(FileNotFoundException e){
				
			System.out.println("File not found.");
			System.exit(-1);
		}
			
		//create and add each PicPanel to the garden
		allPanels = new PicPanel[8][8];
		for(int row=0; row<allPanels.length; row++) {
			for(int col=0; col<allPanels.length; col++) {
				
				allPanels[row][col]=new PicPanel();
				grid.add(allPanels[row][col]);
			}
		}
		
		//read in data from file and load information onto matrix
		while(fileIn.hasNextInt()) {
			
			int row = fileIn.nextInt();
			int col = fileIn.nextInt();
			allPanels[row][col].addFlower();
		}
	}
	
	//examine all eight neighboring cells to determine the total number of flowers present
	public int countNeighbors(boolean[][] grid, int currentRow, int currentCol) {
			
		int numNeighbors=0;
			
		for(int i=0; i<HORZDISP.length; i++) {
					
			int rowEx = currentRow + HORZDISP[i];
			int colEx = currentCol + VERTDISP[i];
			
			if(rowEx>=0 && colEx >=0 && colEx<grid.length && rowEx<grid.length && grid[rowEx][colEx]) 
				numNeighbors++;
		}
		
		return numNeighbors;
	}
	
	//change locations of flowers in garden based on the following rules:
	//1)remove flowers with no neighbors 2)remove flowers with 4+ neighbors 3)grow a flower in an empty location with 3 neighbors
	public void actionPerformed(ActionEvent ae) {
		
		//create a temporary matrix to hold all the current locations of flowers in the garden
		boolean[][]temp = new boolean[allPanels.length][allPanels.length];
		for(int row=0; row<temp.length; row++) {
			for(int col=0; col<temp.length; col++) {
					
				//temp[row][col]=new PicPanel();
				if(allPanels[row][col].hasFlower) {
					temp[row][col]=true;
				}
			}
		}
		
		//update garden based on number of neighbors surrounding a given location in the garden
		for(int row=0; row<allPanels.length; row++) {
			for(int col=0; col<allPanels.length; col++) {
				
				int numNeighbors = countNeighbors(temp, row, col);
				
				//remove border from panels that have had actions performed on them in previous steps
				allPanels[row][col].setBorder(null);
				
				//add or remove a flower and apply red border if any action is performed on one of the panels
				if(allPanels[row][col].hasFlower && (numNeighbors<=1 || numNeighbors>=4)) {
							
					allPanels[row][col].removeFlower();
					allPanels[row][col].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f),Color.red));
				}
				else if(!allPanels[row][col].hasFlower && numNeighbors==3) {
		
					allPanels[row][col].addFlower();
					allPanels[row][col].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f),Color.red));
				}
			}
		}
	}

	class PicPanel extends JPanel{
		
		private boolean hasFlower = false;

		public PicPanel(){
			setBackground(new Color(121,30,3));
		}

		public void addFlower(){
			hasFlower = true;

			repaint();
		}
		
		public void removeFlower(){
			hasFlower = false;
			repaint();
		}

		//this will draw the image
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			if(hasFlower)
				g.drawImage(image,0,0,this);
		}
	}
	
	public static void main(String[] args){
		new NashitaBhuiyan_GardenGUI();
	}
}
