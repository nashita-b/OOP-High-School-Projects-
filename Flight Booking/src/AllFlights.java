/*Nashita Bhuiyan
 * Flight Booking:AllFlights
 * tracks all flights for all airlines across one day
 */
import java.util.*;
import java.io.*;
public class AllFlights {
	
	Flight[] flightsList;
	
	public AllFlights(String fName) {
		
		Scanner fileIn = null;
		try {
			
			fileIn = new Scanner(new File(fName));
		}catch(FileNotFoundException e) {
			
			System.out.println("File not found.");
			System.exit(-1);
		}
		
		flightsList = new Flight[0];
		
		//read data in from file and store flights into array
		 while(fileIn.hasNextLine()) {
			 
			 String flightName = fileIn.nextLine();
			 String destination = fileIn.nextLine();
			 int hour = fileIn.nextInt();
			 int minute = fileIn.nextInt();
			 fileIn.nextLine();
			 
			 Time flyTime = new Time(hour, minute);
			 Flight newestFlight = new Flight(flightName, destination, flyTime);
			 
			 //enter new flight into flightList
			 Flight[] temp = flightsList;
			 flightsList = new Flight[flightsList.length+1];
			 
			 for(int i=0; i<temp.length; i++) {
				 
				 flightsList[i] = temp[i];
			 }
			 flightsList[flightsList.length-1] = newestFlight;
		 }
	}
	
	//takes in a customer and finds an available flight that matches their destination
	public void sellTicket(Customer cust) {
		
		Flight toBook =  null;
		for(int i=0; i<flightsList.length; i++) {
			
			//if customer and plane destination match
			if(flightsList[i].canBook(cust) < 0) {
				
				if(toBook == null) {
					
					toBook = flightsList[i];
				}
				
				//determine the earliest of two flights
				toBook = earliestFlight(flightsList[i], toBook);
			}
		}
		
		if(toBook != null) {
			
			toBook.bookFlight(cust);
			System.out.println("Booked on " + toBook.getFlightName() +"!");
		}
		else {
			
			System.out.println("Booking unsuccessful.");
		}
	}
	
	//returns formatted 3-columnlist of flight names
	public String toString() {
		
		String myString = String.format("%-40s %-40s %-40s", "Flight Name", "Destination", "Departure Time");
		myString += "\n";
		
		for(int i=0; i<flightsList.length; i++) {
			
			myString += "\n";
			myString += String.format("%-40s %-40s %-40s", flightsList[i].getFlightName(), flightsList[i].getDest(), flightsList[i].getDeptTime());
		}
		
		return myString;
	}
	
	//prints out passenger list of each flight
	public void printManifest() {
		
		for(int i=0; i<flightsList.length; i++) {
			
			flightsList[i].printPassengerList();
		}
	}
	
	//returns flight that comes earlier of two
	private static Flight earliestFlight(Flight comparing, Flight latest) {
		
		if(comparing.getDeptTime().compareTo(latest.getDeptTime()) < 0) {
			
			return comparing;
		}
		
		return latest;
	}
}
