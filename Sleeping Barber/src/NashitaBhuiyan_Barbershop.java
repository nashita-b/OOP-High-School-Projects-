import java.util.*;
/*Nashita Bhuiyan
 * Sleeping Barber
 * keep track of the barber cutting hair and customers in a barbershop waiting to get their heir cut and getting their heir cut
 */
public class NashitaBhuiyan_Barbershop{

	private final int MAXCUSTOMERS = (int) (Math.random()*5+1);//maximum number of customers that can be in the store at a time
	private final int CUTTIME = (int) (Math.random()*30000+1);//the amount of time it takes to cut a person's hair
	private static int ID = 0;
	
	private ArrayList<Customer> customers = new ArrayList<Customer>();//maintains a list of customers waiting to ge their hair cut
	
	public NashitaBhuiyan_Barbershop() {
				
		Barber barber = new Barber();
		Customer customer = new Customer();
		customers.add(customer);
		
		Thread barb = new Thread(barber);
		Thread cust = new Thread(customer);
		
		barb.start();
		cust.start();
	}
	
	public class Barber implements Runnable{
			
		//continuously output whether the Barber is awake or not and cut any waiting customers' hair
		public void run() {
			while(true) {
			
				asleep();
				cutHair();
			}
		}
		
		//print to the screen whether the barber is awake or not
		public void asleep() {
			
			synchronized(customers) {
				
				//make the barber fall asleep if there are no customers in the store
				if(customers.isEmpty()) {
					
					System.out.println("The Barber is asleep.");
					
					//leave barber thread in the wait state until a customer arrives
					try {
						customers.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else{
					
					System.out.println("The Barber is awake.");
				}
			}
		}
		
		//cut a cusotmer's hair
		public void cutHair() {
			
			//simulates the duration of time it takes to cut a customer's hair
			try {
				Thread.sleep(CUTTIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//make record of the haircut
			synchronized(customers) {
				
				customers.get(0).hairIsCut = true;
			}
		}
	}
	
	public class Customer implements Runnable{
		
		private int id;//create unique id for each customer
		private boolean hairIsCut;//records whether a customer has had their hair cut or not
		
		public Customer(){
			
			ID++;
			id = ID;
			hairIsCut = false;
		}
		
		//continuously add new customers to the waiting room at random intervals and remove them from the store if max capacity is reached or they recieved a hair cut
		public void run() {
			while(true) {
				
				customerEntry();
				wakeBarber();
				customerExitCut();	
			}
		}
		
		//add new customers to the waiting room at random intervals
		public void customerEntry() {
			
			//have thread sleep for a random amount of time before adding a new customer to the room
			try {
				Thread.sleep((int)(Math.random()*3000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			synchronized(customers) {
				
				//add a customer to the room
				customers.add(new Customer());
				
				//remove customer from room if the total number of customers in the room exceeds the store's maximum capacity
				if(customers.size()>MAXCUSTOMERS) {
					
					int removeID = customers.remove(customers.size()-1).id;
					System.out.println("Customer " + removeID + " left because the shop is full.");
				}
			}
		}
		
		//wake the barber from sleep
		public void wakeBarber() {
			
			//take the barber off the wait state if there is a customer in the store
			if(!customers.isEmpty()) {
				
				synchronized(customers) {
					customers.notify();
				}
			}
		}
		
		//remove from the list the customer who just got their hair cut
		public void customerExitCut() {
			
			if(customers.get(0).hairIsCut) {
				
				synchronized(customers) {
					
					int cutID = customers.remove(0).id;
					System.out.println("Customer " + cutID + " got their hair cut and left.");
				}
			}
		}
	}
	
	public static void main(String[]args) {
	
		NashitaBhuiyan_Barbershop ad = new NashitaBhuiyan_Barbershop();
	}
}
