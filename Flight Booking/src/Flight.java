/*Nashita Bhuiyan
 * Flight Booking: Flight
 * maintains name of flight, destination of flight, time of flight, maximum occupancy and how many booked already
 */
public class Flight {
	
	private String flightName;
	private String destination;
	private Time departTime;
	private int maxOccup;
	private Customer[] seatsBooked;
	
	public Flight(String name, String dest, Time leave) {
		
		flightName = name;
		//consider if dest is #
		destination = dest;
		departTime = leave;
		maxOccup = (int)(Math.random()*81+30);
		seatsBooked = new Customer[0];
	}
	
	public String getDest() {
		
		return destination;
	}
	
	public Time getDeptTime() {
		
		return departTime;
	}
	
	public String getFlightName() {
		
		return flightName;
	}
	
	//returns true if flight is at max capacity
	public boolean isFull() {
		
		if(seatsBooked.length == maxOccup) {
			
			return true;
		}
		return false;
	}
	
	public String toString() {
		
		String toReturn = flightName + ", " + destination + ", " + departTime;
		return toReturn;
	}
	
	//verifies if a customer can book a ticket on the flight
	public int canBook(Customer latest) {
		
		//return large positive number if flight cannot be booked
		if(this.isFull() || !latest.getDest().equals(this.destination)) {
			
			return Integer.MAX_VALUE;
		}
		
		//return difference in time between customer arrival and plane departure
		return (latest.getArrived().compareTo(this.departTime));
	}
	
	//finalizes the booking process
	public void bookFlight(Customer latest) {
		
		Customer[] temp = this.seatsBooked;
		this.seatsBooked = new Customer[seatsBooked.length+1];
		
		for(int i=0; i<temp.length; i++) {
			
			this.seatsBooked[i] = temp[i];
		}
		
		this.seatsBooked[seatsBooked.length-1] = latest;
	}
	
	//prints to screen flight name, departure time, current occupancy, and name of every customer and their arrival time
	public void printPassengerList() {
		
		for(int i=0; i<seatsBooked.length; i++) {
			
			String myString = String.format("Flight: %30s %40s %20s", this.flightName, "Departure Time:",  this.departTime);
			myString += "\n" + String.format("Max Occupancy: %20s %41s %18d", this.maxOccup, "Seats Filled:", this.seatsBooked.length);
			myString += "\n" + String.format("%1s %20s", seatsBooked[i].getName(), seatsBooked[i].getArrived());
			System.out.println(myString);
			System.out.println();
		}		
	}
}


