/*Nahsita Bhuiyan
 *Resident Assistant: ResidentAssistant 
 *1) 
 */

import java.util.*;
public class ResidentAssistant extends CollegeStudent{

	private CollegeStudent[] list;
	
	public ResidentAssistant(String n, String m, String d, double b) {
		
		super(n, m, d, b);
		list = new CollegeStudent[0];
	}
	
	//adds a new student to the RA's list
	public void addStudent(CollegeStudent newcomer) {
		
		//check if new student is in the same dorm as RA
		if(!newcomer.getDorm().equals(this.getDorm())) {
			
			throw new IllegalArgumentException();
		}
		
		CollegeStudent[] temp = list;
		list = new CollegeStudent[list.length+1];
		int listIndex = 0;
		
		//load new student into list in order based on ID number
		for(int i=0; i<temp.length; i++) {
			
			if(newcomer.getID() < temp[i].getID()) {
				
				list[listIndex] = newcomer;
				listIndex++;
			}
			
			list[listIndex] = temp[i];
			listIndex++;
		}
		
		if(list[list.length-1] == null) {
		
			list[list.length-1] = newcomer;
		}
	}
	
	//removes a student from the RA's list based on their given id
	public void moveOut(int oldStudentID) {
	
		int removalLoc = studentSearch(oldStudentID);
		
		if(removalLoc == -1) {
			
			throw new NoSuchElementException();
		}
		
		//remove student from RA list if they are on the list
		CollegeStudent[] temp = list;
		list = new CollegeStudent[list.length-1];
		
		for(int i=0; i<removalLoc; i++) {
			
			list[i] = temp[i];
		}
		for(int i=removalLoc+1; i<list.length; i++) {
			
			list[i] = temp[i];
		}
	}
	
	//finds location of a student on an RA's list given an ID parameter, returns -1 if student not found
	public int studentSearch(int oldStudentID) {
		
		int startLoc = 0;
		int endLoc = list.length;
		
		while(startLoc <= endLoc) {
			
			int midpoint = (startLoc + endLoc)/2;
			
			if(list[midpoint].getID() == oldStudentID) {
				
				return midpoint;
			}
			
			else if(list[midpoint].getID() > oldStudentID) {
				
				endLoc = midpoint - 1;
			}
			else {
				
				startLoc = midpoint + 1;
			}
		}
		
		return -1;
	}
	
	/*edit?*/
	public String toString() {
		
		String toReturn = super.toString();
		
		for(int i=0; i<list.length; i++) {
		
			toReturn += super.toString();
		}
		
		return toReturn;
	}
}
