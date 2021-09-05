/*Nahsita Bhuiyan
 *Resident Assistant: CollegeStudent 
 */

public class CollegeStudent {
	
	private String name;
	private String major;
	private String dorm;
	private double board;
	static int ID;
	
	public CollegeStudent(String n, String m, String d, double b) {
		
		name = n;
		major = m;
		dorm = d;
		board = b;
	}

	public double getBoard() {
		
		return board;
	}
	
	public String getDorm() {
		
		return dorm;
	}
	
	public int getID() {
		
		return ID;
	}
	
	//raises the student's room and board cost by 1.5%
	public void raiseBoard() {
		
		board *= 1.015;
	}
	
	public String toString() {
		
		return "Name: " + name + "\nID: " + ID + "\nMajor: " + major + "\nDorm: " + dorm + "\nBoard: " + board;
	}
}
