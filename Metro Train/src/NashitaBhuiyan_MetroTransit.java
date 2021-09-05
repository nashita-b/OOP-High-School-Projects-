/*Nashita Bhuiyan
 * Metro Trains
 * a simulation that models multiple metro trains travelling to different stations 
 * as well as customers trying to ride a train to get to a final destination.
 */

import java.io.*;
import java.util.*;

public class NashitaBhuiyan_MetroTransit implements Runnable{

	private static int PASSENGERID = 0;
	private ArrayList<String> stations;
	private Map<String, Train> trainsAtStation = new TreeMap<String, Train>();
	private Map<String, Queue<Passenger>> passengersInLine = new TreeMap<String, Queue<Passenger>>();
	
	public NashitaBhuiyan_MetroTransit(int numTrains){
		
		stations = new ArrayList<String>();
		
		//open file containing names and order of all stations
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(new File("stations.txt"));
		}catch(FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(-1);
		}
		
		while(fileIn.hasNextLine()) {
			
			String stationName = fileIn.nextLine();
			
			//read names of stations from file and store in list
			stations.add(stationName);
						
			//create map of trains at stations
			trainsAtStation.put(stationName, null);
			
			//create map that takes record of the passengers waiting to board a train
			Queue<Passenger> passengersToBoard = new LinkedList<Passenger>();
			passengersInLine.put(stationName, passengersToBoard);
		}
		
		//create the total number trains asked by the user and assign a thread to each train
		ArrayList<Train> firstTrains = new ArrayList<Train>();
		ArrayList<Thread> allthreads = new ArrayList<Thread>();
		for(int i=0; i<numTrains; i++) {
			
			Train toAdd = new Train();
			Thread t = new Thread(toAdd);
			allthreads.add(t);
			firstTrains.add(toAdd);
		}
		
		for(Thread nextThread: allthreads)
			nextThread.start();
		
		Thread travellers = new Thread(this);
		travellers.start();
	}
	
	//Continuously add new passengers to the line to board a train
	public void run() {
		
		while(true) {
			
			Passenger toAdd = new Passenger();
			Queue<Passenger> line = (Queue<Passenger>) passengersInLine.get(toAdd.arrival);
			line.add(toAdd);
			
			System.out.println("Passenger " + toAdd.id + " is waiting in " + toAdd.arrival + " station and is headed towards " + toAdd.destination + " station");//delete
		}
	}
	
	public class Train implements Runnable{
		
		private int capacity = (int)(Math.random()*10+1);
		private ArrayList<Passenger> passengers = new ArrayList<Passenger>();
		
		public void run() {
						
			int moveDirection = 1;
			
			//trains will run forever forwards and backwards through all stations
			for(int i=0; i>=0 && i<stations.size(); i+=moveDirection) {
				
				String stationName = stations.get(i);
						
				//have train wait before entering station if the station is currently occupied
				synchronized(trainsAtStation) {
					if(trainsAtStation.get(stationName)!=null) {
							
						try {
							trainsAtStation.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				//have one train arrive at a station and unload passengers at a time
				synchronized(trainsAtStation){
									
					trainsAtStation.put(stationName, this);
					System.out.println("A train has arrived at " + stationName);
					unload(stationName);
				}
				
				//allow new passengers to board the train
				board(stationName);
				
				synchronized(trainsAtStation) {
					
					//train departs from station
					trainsAtStation.put(stationName, null);
					System.out.println("A train has departed from station " + stationName);
					
					//notify any trains waiting to enter the station that the station is now clear
					trainsAtStation.notify();
				}					
				
				//change the direction of train if it has reached the end of the line of stations
				if(i==0)
					moveDirection = 1;
				if(i==stations.size()-1)
					moveDirection = -1;
			}
		}
		
		//unload passengers from train who have arrived at their destination
		public void unload(String stationName) {
			
			Iterator<Passenger> toUnload = passengers.iterator();
			while(toUnload.hasNext()){
			    
				Passenger unloading = toUnload.next();
			    
			    if(unloading.destination.equals(stationName)){
			    	toUnload.remove();
			    }
			}
		}
		
		//pick up passengers who are waiting in line at the station
		public void board(String stationName) {
			
			//continue to allow passengers if there are people in line and the train has not reached carrying capacity
			while(passengers.size()<capacity && !passengersInLine.get(stationName).isEmpty()) {
				
				Passenger toBoard = passengersInLine.get(stationName).remove();
				passengers.add(toBoard);
				System.out.println("Passenger " + toBoard.id + " has boarded a train");
			}
		}
	}
	
	public class Passenger{
		
		private int id;
		private String arrival;
		private String destination;
		
		public Passenger() {
			
			//assign passenger with a unique id
			PASSENGERID++;
			id = PASSENGERID;
			
			//randomly assign passenger with arrival station
			arrival = stations.get((int)(Math.random()*stations.size()));
			
			//randomly assign passenger with destination station that is different than the arrival station
			do {
				
				destination = stations.get((int)(Math.random()*stations.size()));
			}while(arrival.equals(destination));
		}
	}
	
	public static void main(String[]args) {
		
		//ask user how many trains they would like ot make
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Number of trains to create: ");
		int numTrains = keyboard.nextInt();
		
		NashitaBhuiyan_MetroTransit metro = new NashitaBhuiyan_MetroTransit(numTrains);
	}
}
