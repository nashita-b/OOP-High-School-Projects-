/*Nashita Bhuiyan
 * Chat Room
 * an application that will allow clients to chat with each other
 */

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Client extends JFrame{
	
	private JTextArea incoming;			
	private JTextField outgoing;			
	private String name;				
	private DefaultListModel<String> friendModel;	
	private String secretKey;			
	
	private Scanner reader;				
	private PrintWriter writer;			
	private Socket sock;				

	private final String SERVER_IP = "192.168.200.3";
	private final int SERVER_PORT = 4242;

	public Client() {


		setLayout(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(900, 525);

		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);

		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		qScroller.setBounds(65,30,500,250);
		qScroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Chat"));
		qScroller.setOpaque(false);
		add(qScroller);

		JLabel outMessage = new JLabel("Message: ");
		outMessage.setFont(new Font("Times New Roman",Font.PLAIN,14));
		outMessage.setBounds(65,300,70,25);
		add(outMessage);

		outgoing = new JTextField(20);
		outgoing.setBounds(130,300,360,20);
		outgoing.addActionListener(new SendButtonListener());
		add(outgoing);

		friendModel = new DefaultListModel<String>();
		JList<String> friendList = new JList<String>(friendModel);
		JScrollPane friendScroll = new JScrollPane(friendList);
		friendList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Friends"));
		friendScroll.setBounds(675,30,150,250);
		add(friendScroll);

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		sendButton.setBounds(500,300,65,18);
		add(sendButton);

		JButton logOff = new JButton("Log Off");
		logOff.addActionListener(new LogOffButtonListener());
		logOff.setBounds(775,440,80,18);
		add(logOff);
		Random r = new Random();
		this.getContentPane().setBackground(new Color(r.nextInt(256),r.nextInt(256),r.nextInt(256)));

		setUpNetworking();

		Thread clientThread = new Thread(new IncomingReader());
		clientThread.start();

		setVisible(true);
	}

	private void setUpNetworking() {

		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter your name: ");
		name = keyboard.nextLine();
		
		setTitle("Chat Client - " +name);

		//set up socket, printWriter, and scanner
		try {
			
			sock = new Socket(SERVER_IP, SERVER_PORT);
			writer = new PrintWriter(sock.getOutputStream());
			reader = new Scanner(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class SendButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent ev) {

			//get text from textField and send to server
			writer.println(name + ": " + outgoing.getText());
			writer.flush();

			// clear textField and reset cursor
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}

	public class LogOffButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent ev){

			try{
				writer.println(name+":logoff:"+secretKey);
				writer.flush();
				
				sock.close();				

			}catch(IOException e){
				e.printStackTrace();
			}

			System.exit(0);
		}
	}

	class IncomingReader implements Runnable {

		public void run() {

			//store secretKey sent by server
			secretKey = reader.nextLine();
			
			//read incoming message and place into text area
			while(true) {
				
				String newMessage = reader.nextLine();
				
				//remove friend from list if they have logged off
				if(newMessage.contains(secretKey)) {
					friendModel.removeElement(newMessage.substring(0, newMessage.indexOf(":")));
				}
				
				//add message to textarea if new message sent
				else {
				
				incoming.append(newMessage+"\n");
				
				//add new person to friends list if a new client as sent a message
				String username = newMessage.substring(0, newMessage.indexOf(":"));
				if(!friendModel.contains(username) && !username.equals(name)) 
					friendModel.addElement(username);
				}
			}
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}

