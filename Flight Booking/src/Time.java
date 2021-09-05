/*Nashita Bhuiyan
 * Flight Booking: Time
 * maintains hour and minute of the day in military time
 */
public class Time {
	
	private int hour;
	private int minute;
	
	public Time(int h, int m) {
		
		//hour range must be between [0, 24], minute range must be between [0, 60]
		if(m<0 || m>60 || h*60+m>24*60 || h*60+m<0) {
			
			throw new IllegalArgumentException("Invalid entry.");
		}
	
		hour = h;
		minute = m;
		
	}
	
	public String toString() {
		
		String toReturn = "";
		                                                               
		//add hour
		if(hour < 10) {
			
			toReturn += "0";
		}
		toReturn += hour+":";
		
		//add minute
		if(minute < 10) {
			
			toReturn += "0";
		}
		toReturn += minute;
		
		return toReturn;
	}
	
	//return negative if calling object time comes before the parameter's time
	public int compareTo(Time other) {
		
		//compute difference between total number of minutes in calling object & parameter
		int diff = ((this.hour*60) + this.minute) - ((other.hour*60) + other.minute);
		return diff;
	}
}
