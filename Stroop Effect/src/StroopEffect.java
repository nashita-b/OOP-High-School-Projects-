/*Nashita Bhuiyan
 * Stroop Effect
 * demonstration of cognitive interference where user is challenged to match the color of a block with its associated text
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class StroopEffect extends JFrame implements ActionListener{
	
	// text value and color value for the label and panel
	private final String[] names = { "Red", "Orange", "Yellow", "Green", "Blue", "Magenta" };
	private final Color[] colors = { Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta };
	
	private JPanel colorPanel;
	private JLabel display;
	private JLabel guessMessage;
	
	private int colorsIndex; //store index location of color chosen for the JPanel from the colors array
	private int namesIndex; //store index location of color name chosen for the JLablel from the names array
	private int totalGuesses;
	private int correctGuesses;
	
	public StroopEffect() {
		
		setTitle("Stroop Effect");
		setSize(175, 120);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton guess = new JButton("Guess");
		guess.addActionListener(this);
		
		JButton cycle = new JButton("Cycle");
		cycle.addActionListener(this);
		
		display = new JLabel();
		
		colorPanel = new JPanel();
		colorPanel.setPreferredSize(new Dimension(30, 30));
		
		//assign initial colors and names to display and colorPanel
		setGame();
		
		//guess system
		guessMessage = new JLabel();
		correctGuesses = 0;
		totalGuesses = 0;
		guessMessage.setText(String.valueOf(correctGuesses) + "/" +String.valueOf(totalGuesses));
		
		setLayout(new FlowLayout());
		add(guess);
		add(cycle);
		add(display);
		add(colorPanel);
		add(guessMessage);
		setVisible(true);
	}

	//assign new color and name values to display and colorPanel
	private void setGame() {
		
		colorsIndex = (int)(Math.random()*colors.length);
		colorPanel.setBackground(colors[colorsIndex]);
		
		namesIndex = (int)(Math.random()*names.length);
		int dispColorsIndex; //temporarily hold index from color array for display's foreground
		//continue to find new index values for displColorsIndex until display has a different index for text and foreground color
		do {
			
			dispColorsIndex = (int)(Math.random()*colors.length);
		}while(namesIndex==dispColorsIndex);
		
		display.setText(names[namesIndex]);
		display.setForeground(colors[dispColorsIndex]);
	}
	
	// pops up a separate box containing the message parameter
	private void displayMessage(String message) {
		
		JOptionPane.showMessageDialog(null, message);
	}
	
	//check what actions have been performed and adjust accordingly
	public void actionPerformed(ActionEvent ae) {

		//cycle through the colors in the colors array and change the color of the colorPanel
		if(ae.getActionCommand().equals("Cycle")) {
			
			colorsIndex++;			
			colorPanel.setBackground(colors[colorsIndex%colors.length]);
		}
		else if(ae.getActionCommand().equals("Guess") && colorsIndex!=namesIndex) {
			
			totalGuesses++;
			guessMessage.setText(String.valueOf(correctGuesses) + "/" +String.valueOf(totalGuesses));
			displayMessage("Sorry, try again.");
		}
		else{
			
			correctGuesses++;
			totalGuesses++;
			guessMessage.setText(String.valueOf(correctGuesses) + "/" +String.valueOf(totalGuesses));
			displayMessage("Correct!");
			//reset game so player can play another round
			setGame();
		}
		System.out.println(LocalDateTime.now().getHour() + ":" +LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond());
	}
	
	public static void main(String[] args){
		
		new StroopEffect();
	}
}