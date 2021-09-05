/*Nashita Bhuiyan
 * Password Verifier
 * a server that will allow a client three chances to enter a password
 */
import java.net.*;
import java.util.*;
import java.io.*;

public class NashitaBhuiyan_PasswordServer {

	private static final String password = "password";
	private ArrayList<String> rejectedIPs = new ArrayList<String>();
	
	public NashitaBhuiyan_PasswordServer(){
		
		try{

			ServerSocket server = new ServerSocket(4242);
			
			//display port number and ip adress
			System.out.println("Port Number: " + server.getLocalPort());
			System.out.println("IP Adress: " + InetAddress.getLocalHost().getHostAddress());
			
			//accept connections forever
			while(true) {
				
				Socket theSock = server.accept();

				//new thread created to interact with client
				Thread newClient = new Thread(new maintainClients(theSock));
				newClient.run();
			}
		}catch(IOException e){
			e.printStackTrace();

		}
	}
	
	public class maintainClients implements Runnable{

		private Socket sock;
		private String ip;
		private int attempts;
		
		public maintainClients(Socket theSock) {
			
			sock = theSock;
			ip = sock.getRemoteSocketAddress().toString(); 
			ip = ip.substring(ip.indexOf(":")+1);
			attempts = 0;
			
			try {
				
				PrintWriter out = new PrintWriter(sock.getOutputStream());
				
				//immediately reject client if they have already been rejected
				for(String ips: rejectedIPs) {
					
					if(ips.equals(ip)) {
						
						out.println("NO");
						out.flush();
						return;
					}
						
				}
				
				//if client is not on the rejected list
				out.println("ACCEPTED");
				out.flush();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			
			try {
				
				PrintWriter out = new PrintWriter(sock.getOutputStream());
				Scanner in = null;					
				in = new Scanner(sock.getInputStream());
				
				//have client make 3 attempts to guess the password correclty before rejecting them
				do {
					
					String passAttempt = in.nextLine();
					
					//if client guesses correctly 
					if(passAttempt.equals(password)) {
							
						out.println("ACCEPTED");
						out.flush();
						out.close();
						in.close();
						sock.close();
						return;
					}
					
					//if client used up all guesses
					else if(attempts==3) {
							
						rejectedIPs.add(ip);
						out.println("NO");
						out.flush();
					}
					
					//show client how many more attempts they have left
					else {
						
						attempts++;
						out.println("That is the incorrect password. You have " + (3-attempts) + " attempts remaining. ");
						out.flush();
					}
				}while(attempts<3);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[]args){

		new NashitaBhuiyan_PasswordServer();
	}
}
