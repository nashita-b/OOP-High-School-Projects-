/*Nashita Bhuiyan
 * SAT Prep
 * a GUI that will help students review classic SAT words
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class SATPrep extends JFrame implements ActionListener, ListSelectionListener {

	private Dictionary full;  //contains list of 5000 words and definitions read in from a file
	private Dictionary partial;	//dictionary of words that contain whatever user enters
	private DefaultListModel<String> partWord;	//list of words to be shown on scrollable list
	private JTextArea actualDef; // show definition of given word
	private JTextField searchField; // where user enters word to search
	private JList<String> keyWordList;	//scrollable list

	public SATPrep() {

		// form dictionary containing all words and their definitions from file
		full = new Dictionary("wordlist.txt");
		
		//prepare GUI
		new View();
		
		//add functionality to search and scrollable list components
		searchField.addActionListener(this);
		keyWordList.addListSelectionListener(this);
	}

	//populate list with all words containing whatever the user typed into the search bar
	public void actionPerformed(ActionEvent ae) {
		
		//clear definition and list from previous searches
		actualDef.setText("");
		partWord.clear();
		
		//create a new list of words 
		partial = full.getHits(searchField.getText());
		for (Definition p : partial) {

			partWord.addElement(p.getWord());
		}
	}

	//show definition of selected word in actualDef
	public void valueChanged(ListSelectionEvent le) {

		int index = keyWordList.getSelectedIndex();
		if (index != -1) {

			actualDef.setText(partial.get(index).getDef());
		}
	}

	//assemble visual components
	public class View {

		public View() {
			
			// create frame
			setTitle("SAT Review");
			setSize(450, 650);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//create a panel with a background image
			BackgroundPic mainPanel = new BackgroundPic("clouds-3_5.jpg");
			mainPanel.setLayout(null);
			
			// create search bar
			searchField = new JTextField(20);
			JLabel search = new JLabel("Search:");
			search.setBounds(50, 50, 50, 30);
			searchField.setBounds(130, 55, 200, 20);
			
			// create scrollable list
			partial = new Dictionary();
			partWord = new DefaultListModel<String>();
			keyWordList = new JList<String>(partWord);
			keyWordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JScrollPane jscp = new JScrollPane(keyWordList);
			jscp.setBounds(50, 100, 280, 300);
			
			//create area where word definitions are shown
			JLabel def = new JLabel("Def:");
			def.setBounds(50, 420, 30, 30);
			actualDef = new JTextArea("");
			actualDef.setEditable(false);
			actualDef.setLineWrap(true);
			actualDef.setWrapStyleWord(true);
			actualDef.setOpaque(false);
			actualDef.setBounds(130, 420, 200, 50);
	
			//add widgets to image panel
			mainPanel.add(search);
			mainPanel.add(searchField);
			mainPanel.add(jscp);
			mainPanel.add(def);
			mainPanel.add(actualDef);
			
			add(mainPanel);
			setVisible(true);
		}
	}

	//apply background image to a panel
	public class BackgroundPic extends JPanel {
		
		private BufferedImage bkgrndImg;

		//read image in from a file
		public BackgroundPic(String fname){

			try {
				
				bkgrndImg = ImageIO.read(new File(fname));
			} catch (IOException ioe) {
				
				System.out.println("Could not read in the pic");
				System.exit(0);
			}
		}

		//apply image to panel
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			g.drawImage(bkgrndImg, 0, 0, this);
		}
	}
	

	public static void main(String[] args) {

		new SATPrep();
	}

}
