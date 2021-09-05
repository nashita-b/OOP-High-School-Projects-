/*Nashita Bhuiyan
 * Flight Booking
 * maintain database of an airline at an airport, allow employees to view flights of the day and allow customers to book seats
 */
import java.util.*;
import java.io.*;
public class Client {
	public static void main(String[] args) {
		
		AllFlights list = new AllFlights("flights.txt");
		String choice;
		
		System.out.println(list);
		do{
			
			choice = menu();
			if(choice.equals("B")) {
				
				buyTicket(list);
			}
			else if(choice.equals("L")) {
				
				System.out.println(list);
			}
			else if(choice.equals("S")) {
				
				list.printManifest();
			}	
		}while(!choice.equals("Q"));
	}
	
	//buy a ticket
	public static void buyTicket(AllFlights a) {
		
		Scanner keyboard = new Scanner(System.in);
		
		//collect user input
		System.out.print("\nEnter Customer Name: ");
		String custName = keyboard.nextLine();
		System.out.print("Enter destination: ");
		String custDest = keyboard.nextLine();
		System.out.print("Enter arrival time (hour space minute): ");
		String timeTogether = keyboard.nextLine();
		
		//convert time data collected from user from String to Time
		String[] timeParts = timeTogether.split(" ");
		int custHour = Integer.parseInt(timeParts[0]);
		int custMinute = Integer.parseInt(timeParts[1]);
		Time custArrival = new Time(custHour, custMinute);
		
		Customer ticketPurch = new Customer(custName, custArrival, custDest);
		
		a.sellTicket(ticketPurch);
	}
	
	//menu
	public static String menu() {
		
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("\nMenu Options:");
		System.out.println("\nB) Buy a ticket\nL) List all flights\nS) Show manifest\nQ) Quit");
		System.out.print("\nEnter choice: ");
		String choice = keyboard.nextLine();
		
		return choice;
	}
}
