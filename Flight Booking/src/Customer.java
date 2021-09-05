/*Nashita Bhuiyan
 * Flight Booking: Customer
 * Maintains the cutomer's name, time of arrival, and destination
 */
public class Customer {
	
	private String name;
	private Time arrived;
	private String destination;
	
	public Customer(String n, Time a, String d) {
		
		name = n;
		arrived = a;
		//consider if destination is a number?
		destination = d;
	}
	
	public String getDest() {
		
		return destination;
	}
	
	public Time getArrived() {
		
		return arrived;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String toString() {
		
		String toReturn = name + ", " + destination + ", " + arrived;
		return toReturn;
	}
}
