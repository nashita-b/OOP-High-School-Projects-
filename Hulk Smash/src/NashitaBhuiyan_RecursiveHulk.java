/*Nashita Bhuiyan
 * Recursive Hulk
 * determine the path the Hulk should take in order to fight the most minions using a recursive function
 */
import java.io.*;
import java.util.*;
public class NashitaBhuiyan_RecursiveHulk {

	private int[][] minions;
	private int tempTotal = 0;
	private int maxTotal = -1;

	public NashitaBhuiyan_RecursiveHulk(String fName, int numRows) {

		Scanner fileIn = null;
		minions = new int[numRows][numRows];

		// open file
		try {
			fileIn = new Scanner(new File(fName));
		} catch (FileNotFoundException e) {
			System.exit(-1);
		}

		// transfer file information onto a matrix
		int minRow = 0;
		while (fileIn.hasNextInt()) {

			minions[minRow / numRows][minRow % numRows] = fileIn.nextInt();
			minRow++;
		}
	}

	//returns path with largest number of minions destroyed
	public int getTotal(int row, int col) {

		//return negative number if impassable sector
		if (minions[row][col] == -1) 
			return -1;

		tempTotal += minions[row][col];
		int currTotal = tempTotal;

		//return total minions defeated on a path when reach the end
		if (row == minions.length - 1 && col == minions.length - 1) 
			return tempTotal;

		int pathTotal = -1;
		if (row != minions.length - 1) 
			pathTotal = getTotal(row+1, col);
		
		//update most minions defeated on a single path, and reset minions destroyed to what it was before branching off
		if (pathTotal > maxTotal)
			maxTotal = pathTotal;
		tempTotal = currTotal;
		
		if (col != minions.length - 1) 
			pathTotal = getTotal(row, col+1);
			
		//update most minions defeated on a single path
		if (pathTotal > maxTotal)
			maxTotal = pathTotal;
		
		return maxTotal;
	}

	public static void main(String[] args) {

		NashitaBhuiyan_RecursiveHulk hulk = new NashitaBhuiyan_RecursiveHulk("CP74.txt", 12);
		System.out.println("MOST MINIONS IN A SINGLE PATH: " + hulk.getTotal(0, 0));
	}

	// contains Hulk's current position and how many minions were fought from beginning to this position
	public class Sector {

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
