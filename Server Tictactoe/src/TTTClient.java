/*Nashita Bhuiyan
 * Tic-Tac-Toe
 * program that facilitates tic-tac-toe across a network
 */
import java.awt.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.net.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;

//Handles Client input (hoping for single-threaded)

public class TTTClient extends JFrame {


	private boolean myTurn;				// is it the user's turn
	private JLabel label;				// displays status info
	private Panel[][] panels;			// stores the x and o pics

	private String oppName;				// used for output purposes
	private String myPiece;				// either "x.jpg" or "o.jpg"
	private String oppPiece;
	private String myName;				// for debugging purposes

	private PrintWriter writer;			// used to write out to the server
	private Scanner reader;			//used to read from the server
	private Socket theSock;	
	private STATUS stat;				//WIN,LOSS,DRAW,GO

	public TTTClient(String ip, String name, int port){


		myName = name;
		this.setLayout(null);

		//create the board
		setSize(600,600);
		setTitle("Tic Tac Toe - "+name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creates grid of tic tac toe board
		JPanel grid= new JPanel();
		grid.setBounds(0, 0, 600, 500);
		grid.setLayout(new GridLayout(3,3,5,5));
		grid.setBackground(Color.black);
		add(grid);

		//creates panel for label to be in
		JPanel labelPanel=new JPanel();
		labelPanel.setBounds(0, 500, 600, 100);
		labelPanel.setBackground(new Color(83,199,237));
		add(labelPanel);

		//creates label to output whos turn it is and who won
		label=new JLabel();
		labelPanel.add(label);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("Comic Sans", Font.ITALIC, 18));
		label.setBounds(250, 570, 100, 30);


		panels=new Panel[3][3];

		//creates panels in a matrix to represent tic tac toe board
		for(int i = 0; i < 3; i ++){

			for(int k = 0; k < 3; k ++){

				panels[i][k] = new Panel(i,k);
				panels[i][k].setBackground(Color.white);
				grid.add(panels[i][k]);
			}

		}

		try{

			theSock = new Socket(ip,port);
			writer = new PrintWriter(theSock.getOutputStream());
			reader = new Scanner(new InputStreamReader(theSock.getInputStream()));
		}catch(IOException e){

			System.out.println("Problem setting up client socket");
			System.exit(-1);
		}

		//sends the server the player's name
		writeMessage(name);
	
		String[] message = reader.nextLine().split(",");

		//gets Player's Name, receives from server
		// if they go first and if they are x or o
		STATUS stat = getStatus(message[0]);
		oppName = message[1];

		if(stat==STATUS.GO){
			label.setText("It's your turn");
			myTurn = true;
			myPiece = "X.jpg";
			oppPiece = "O.jpg";
		}
		else{
			label.setText("It's "+ oppName+"'s turn");
			myTurn = false;
			myPiece = "O.jpg";
			oppPiece = "X.jpg";
		}

		Thread t = new Thread(new ReadHandler());
		t.start();
		setVisible(true);
	}

	// converts a string value to it's enum counterpart
	private STATUS getStatus(String value){
		return STATUS.values()[Integer.parseInt(value)];
	}

	//sends a message go the server
	private void writeMessage(String message){
		writer.println(message);
		writer.flush();
	}

	//updates the JLabel with the provided message
	private void updateLabel( String message){
		
		try {
			SwingUtilities.invokeAndWait(
					new Runnable(){

						public void run(){
							label.setText(message);
						}
					}
				);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
	}


	class Panel extends JPanel implements MouseListener{

		private BufferedImage image;
		private int w,h;		//used for drawing the pic
		private boolean empty;

		private int row;		// the row of the TTT matrix element
		private int col;		// the col of the TTT matrix element

		public Panel(int r, int c){
			row = r;
			col = c;

			empty = true;
			this.addMouseListener(this);
		}


		//changes the panel to a given file pic
		private void changePic(String fname){

			try {

				image = ImageIO.read(new File(fname));
				w = image.getWidth();
				h = image.getHeight();

			} catch (IOException ioe) {
				System.out.println(ioe);
				System.exit(0);
			}


		}

		//only handle an event when clicked
		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		//updates the pic and label if cell is free and it is the user's turn
		public void mouseClicked(MouseEvent e) {

			if(empty && myTurn) {
				
				//update pic and label
				changePic(myPiece);
				this.repaint();
				removeMouseListener(this);
				empty = false;
				myTurn = false;
				
				//send selection to server
				label.setText("It's "+ oppName+"'s turn");
				writeMessage(row + "," + col);
			}
		}

		public Dimension getPreferredSize() {

			return new Dimension(w,h);
		}
		
		//update board to include the opponents moves
		public void includeOppMove() {
			
			panels[row][col].changePic(oppPiece);
			panels[row][col].repaint();
			removeMouseListener(panels[row][col]);
			panels[row][col].empty = false;
		}

		//this will draw the image
		//repaint calls this method (when it's ready)
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			if(!empty)
				g.drawImage(image,0,0,this);
		}

	}

	//Handles reading messages from the server
	public class ReadHandler implements Runnable{

		public void run(){

			String message = "";
			stat = STATUS.GO;
		
			while(stat==STATUS.GO){
				
				//gets a message from the server
				message = reader.nextLine();
				String[] movePosition = message.split(",");
				myTurn = true;
				stat = getStatus(movePosition[0]);
				
				//if the player being evaluated has not won
				if(stat!=STATUS.WON) {
					//updates the panel that the opponent selected
					panels[Integer.parseInt(movePosition[1])][Integer.parseInt(movePosition[2])].includeOppMove();
					updateLabel("It's your turn");
				}
			}

			//update label for win and loss
			if(stat==STATUS.WON) {
				updateLabel("You won!");
			}
			else if(stat==STATUS.LOST)
				updateLabel("Sorry you lost. " + oppName + " won.");
			else
				updateLabel("It's a draw.");

			try{
				reader.close();
				theSock.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			return;
		}

	}

	public static void main(String[] args){
		String name = JOptionPane.showInputDialog(null,"What is your name?");
		new TTTClient("0.00.000.0000",  name, 4242);
	}
}
