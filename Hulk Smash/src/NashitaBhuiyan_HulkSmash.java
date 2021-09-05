/*Nashita Bhuiyan
 * Hulk Smash
 * determine the path the Hulk should take in order to fight the most minions
 */
import java.io.*;
import java.util.*;

public class NashitaBhuiyan_HulkSmash {
	
	private int[][] minions;
	private Stack<Sector> paths = new Stack<Sector>();
	
	public NashitaBhuiyan_HulkSmash(String fName, int numRows) {
		
		Scanner fileIn = null;
		minions = new int[numRows][numRows];
		
		//open file
		try {
			fileIn = new Scanner(new File(fName));
		}
		catch(FileNotFoundException e){
			System.exit(-1);
		}
				
		//transfer file information onto a matrix
		int minRow = 0;
		while(fileIn.hasNextInt()) {
			
			minions[minRow/numRows][minRow%numRows] = fileIn.nextInt();
			minRow++;
		}		
	}
	
	//returns path with largest number of minions destroyed
	public int maxMinionsPath() {
		
		paths.push(new Sector(0, 0, minions[0][0]));
		Sector toMove;
		int mostMinions = -1;
		
		//evaluate all possible paths the Hulk can go down
		do {
			//get the Hulk to get to the end of a single path
			while(!paths.isEmpty() && (paths.peek().row!=minions.length-1 || paths.peek().col!=minions.length-1)) {
				
				toMove = paths.pop();
				moveRight(toMove);
				moveDown(toMove);
			}
		
			//replace mostMinions if a higher number of minions is fought through the new path
			if(!paths.isEmpty()) {
				
				toMove = paths.pop();
				
				if(toMove.totalMinFought > mostMinions)
					mostMinions = toMove.totalMinFought;
			}
			
		}while(!paths.isEmpty());
		
		return mostMinions;
	}
	
	//move the Hulk to the right of its previous location
	private void moveRight(Sector prevTile) {
		
		//check if Hulk can move any more right and if the tile to the right is impassable
		if(prevTile.col+1 == minions.length || minions[prevTile.row][prevTile.col+1]==-1)
			return;
		
		paths.push(new Sector(prevTile.row, prevTile.col+1, prevTile.totalMinFought+minions[prevTile.row][prevTile.col+1]));
	}
	
	//move the Hulk one row below its previous location
	private void moveDown(Sector prevTile) {
		
		//check if Hulk can move any more down and if the tile below is impassable
		if(prevTile.row+1 == minions.length || minions[prevTile.row+1][prevTile.col]==-1)
			return;
		
		paths.push(new Sector(prevTile.row+1, prevTile.col, prevTile.totalMinFought+minions[prevTile.row+1][prevTile.col]));
	}

	public static void main(String[]args) {
		
		NashitaBhuiyan_HulkSmash hulk = new NashitaBhuiyan_HulkSmash("CP74.txt", 12);
		System.out.println("MOST MINIONS IN A SINGLE PATH: " + hulk.maxMinionsPath());
	}
	
	//contains Hulk's current position and how many minions were fought from beginning to this position
	public class Sector{
		
		private int row; 
		private int col;
		private int totalMinFought;

		public Sector(int r, int c, int t) {
			
			row = r;
			col = c;
			totalMinFought = t;
		}	
	}
}
