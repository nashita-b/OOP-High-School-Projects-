/*Nashita Bhuiyan
 * Chat Room
 * an application that will allow clients to chat with each other
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
	private ArrayList<PrintWriter> clientOutputStreams;  
	private String secretKey;

	public  Server() {
		
		clientOutputStreams = new ArrayList<PrintWriter>();
		Integer key = (int)(Math.random()*((99999-1000)+1)+10000);
		secretKey = key.toString();

		try {
			
			ServerSocket server = new ServerSocket(4242);
			
			// listen for clients forever
			while(true) {
				
				Socket theSock = server.accept();

				// create new PrintWriter and add to ArrayList
				PrintWriter newOutput = new PrintWriter(theSock.getOutputStream());
				clientOutputStreams.add(newOutput);
				
				// create and start new thread
				Thread newClient = new Thread(new ClientHandler(theSock));
				newClient.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// allow all connected users to see the message
	public void tellEveryone(String message) {
				
		for(PrintWriter output: clientOutputStreams) {
			
			output.println(message);
			output.flush();
		}
	}
	
	public class ClientHandler implements Runnable {

		private Scanner reader;
		private Socket sock;
		private PrintWriter theWriter;

		public ClientHandler(Socket clientSocket) {

			sock = clientSocket;
			try {
				
				reader = new Scanner(sock.getInputStream());
				theWriter = new PrintWriter(sock.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			
			//send secretKey to client immediately after they connect to server
			theWriter.println(secretKey);
			theWriter.flush();
			
			String message = "";
			do {
				
				//send all clients a message if there is an available message from this client
				message = reader.nextLine();
				tellEveryone(message);
			}while(!message.contains(secretKey));
			
			//server no longer interacts with client if client logs off
			closeConnections();
		}

		private void closeConnections(){

			try{
				synchronized(clientOutputStreams){
					
					reader.close();
					theWriter.close();
					sock.close();
					clientOutputStreams.remove(theWriter);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}



	public static void main(String[] args) {
		new Server();
	}
}
