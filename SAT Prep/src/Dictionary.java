/*Nashita Bhuiyan
 * Dictionary 
 * Builds a list of words and their definitions, also provides a function that returns a list of partial matches to a given word
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dictionary extends ArrayList<Definition> {
	
	public Dictionary() {
		
		super();
	}
	
	//create a dictionary by reading in from a file
	public Dictionary(String fname) {
		
		Scanner fileIn = null;
		try {
		
			fileIn = new Scanner (new File(fname));
		} catch(FileNotFoundException e) {
		
			System.out.println("File not a file.");
			System.exit(-1);
		}
		
		//read from file and add to dictionary
		while(fileIn.hasNextLine()) {
			
			String line = fileIn.nextLine();
			int endOfWord = line.indexOf(" ");
			
			String word = line.substring(0, endOfWord);
			String definition = line.substring(endOfWord+1);
			
			add(new Definition(word, definition));
		}
	}
	
	//return a list of type Definition whose words contain the parameter text entered
	public Dictionary getHits(String text){
		
		Dictionary hits = new Dictionary();
		
		//look through the entire dictionary for any matches
		for(Definition nxtDef: this) {
			
			//add matches to a new list
			if((nxtDef.getWord().indexOf(text)) != -1)
				hits.add(nxtDef);
		}
		
		return hits;
	}

}
