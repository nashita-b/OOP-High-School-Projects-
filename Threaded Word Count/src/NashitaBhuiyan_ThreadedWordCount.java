import java.io.*;
import java.util.*;
/*Nashita Bhuiyan
 * Threaded Word Count
 * given an array of text files examine and print the total number of words in all the files
 */
public class NashitaBhuiyan_ThreadedWordCount {
	
	private File[] files;
	private int numWords = 0; //critical section of data
	
	public NashitaBhuiyan_ThreadedWordCount(String[]fileNames) {
		
		//create array of files from the file names entered by the user
		files =  new File[fileNames.length];
		
		for(int i=0; i<fileNames.length; i++) {
			
			files[i] = new File(fileNames[i]);
		}
	}
	
	public void count() {
		
		WordCount words = new WordCount();
		ArrayList<Thread> allThreads = new ArrayList<Thread>();
				
		//create a thread for every file
		for(int i=0; i<files.length; i++) {
			
			Thread toAdd = new Thread(words);
			toAdd.setName(files[i].getName());
			allThreads.add(toAdd);
		}
		
		for(Thread nextThread: allThreads)
			nextThread.start();

		//make sure all threads are done adding to numWords before printing to the screen
		for(Thread nextThread: allThreads){
			try{
				nextThread.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		System.out.println("Total Words: " + numWords);
	}
	
	public class WordCount implements Runnable{

		private Object lock = numWords; 
		
		public void run() {		
			synchronized(lock) {
				
				//open file
				Scanner fileIn = null;
				try {
					fileIn = new Scanner(new File(Thread.currentThread().getName()));
				}
				catch(FileNotFoundException e){
					System.out.println("File not found.");
					System.exit(-1);
				}
				
				//add number of words found on one line to the total number of words found
				while(fileIn.hasNextLine()) {
					
					String line = fileIn.nextLine();
					String[]words = line.split("\\W+");
					numWords+=words.length;
				}
			}
		}
	}
}
