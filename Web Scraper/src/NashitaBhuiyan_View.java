import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.imageio.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
//import java.awt.event.*;
import javax.swing.event.*;
/*Nashita Bhuiyan
 * Web Scraper
 * allow the user to view the top ranked schools according to US News and World Report based on a given category
 */

//gives the user a look and feel to navigate all categories
public class NashitaBhuiyan_View extends JFrame implements ListSelectionListener{

	private JList<String> categories;
	private DefaultListModel<String> catModel;

	private JList<String> schools;
	private DefaultListModel<String> schoolsModel;

	private Map<String,ArrayList<String>> directory;

	public NashitaBhuiyan_View(){

		setSize(600,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("US News and World Report Top Schools");
		setLayout(null);

		getContentPane().setBackground(Color.white);

		//create the list for category names
		catModel = new DefaultListModel<String>();
		categories = new JList<String>(catModel);
		categories.addListSelectionListener(this);
		JScrollPane departJSP = new JScrollPane(categories);
		departJSP.setBounds(50,40,200,200);
		departJSP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Categories"));
		departJSP.setOpaque(false);
		add(departJSP);

		//create the list for universities in the categories
		schoolsModel = new DefaultListModel<String>();
		schools = new JList<String>(schoolsModel);
		schools.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane schoolsJSP = new JScrollPane(schools);
		schoolsJSP.setBounds(300,40,250,200);
		schoolsJSP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Schools"));
		schoolsJSP.setOpaque(false);
		add(schoolsJSP);

		
		//category is the key and it maps to all universities found in that category
		directory = new HashMap<String,ArrayList<String>>();

		loadDir();
		
		setVisible(true);
	}

	private void loadDir(){

		Scanner catIn = null;
		Scanner schoolsIn = null;
		//reading category names and urls
		//from two seperate files
		try{
			catIn = new Scanner(new File("cats.txt"));
			schoolsIn = new Scanner(new File("sites.txt"));
		}catch(FileNotFoundException e){
			System.out.println("At least one text file not found");
			System.exit(-1);
		}

		//put categories from file onto a list and create new threads for each page listed on the file
		ArrayList<Thread> threads = new ArrayList<Thread>();
		while(schoolsIn.hasNextLine() && catIn.hasNextLine()) {
			
			String category = catIn.nextLine();
			threads.add(new Thread(new Scrape(category, schoolsIn.nextLine())));
			catModel.addElement(category);
		}
		
		//launch all threads
		for(Thread nextThread: threads)
			nextThread.start();

		//join threads so all threads run to completion before the GUI is shown
		for(Thread nextThread: threads){
			try{
				nextThread.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public void valueChanged(ListSelectionEvent le){

		//clear out all schools each time the user selects
		//a new category
		schoolsModel.removeAllElements();

		int location = categories.getSelectedIndex();
		if(location == -1)
			return;

		//update the schools list based on selected categories
		String cat = (String)catModel.get(location);
		ArrayList<String> allSchools = directory.get(cat);
		if(allSchools == null)
			return;
		for(String school: allSchools){
			schoolsModel.addElement(school);
		}
	}

	public class Scrape implements Runnable{

		private String site;
		private String category;

		public Scrape(String d,String s){
			site = s;
			category = d;
		}
		
		public void run(){
			
			Scanner reader = getConnection(site);
			
			//put all html file information onto a single string
			String file ="";
			while (reader.hasNextLine()){
				
				file+= reader.nextLine().trim()+" ";
			}
					
			//skip over first DCC			
			file = file.substring(file.indexOf("DetailCardColleges__CardContainer") + "DetailCardColleges__CardContainer".length()-1);

			//find and store school names in directory under the proper category
			ArrayList<String> schoolNames = new ArrayList<String>();
			synchronized(directory) {
				directory.put(category, schoolNames);
			}
						
			while(file.indexOf("DetailCardColleges__CardContainer")!=-1) {
				
				//find the location of the next school in file
				file = file.substring(file.indexOf("DetailCardColleges__CardContainer") + "DetailCardColleges__CardContainer".length()-1);
				file = file.substring(file.indexOf("name=\"") + "name=\\\"".length()-1);
				
				//make sure no other threads are updating the directory before adding the school to the list
				schoolNames.add(file.substring(0, file.indexOf("\">")));
				System.out.println(category);
			}
		}
		
		//this method is already called on your behalf
		//disables certificate validation for https connection
		//creates a Scanner object that will read the html code off the provided site webpage
		private  Scanner getConnection(String site) {

			TrustManager[] trustAllCerts = new TrustManager[] { 
					new X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() { 
							return new X509Certificate[0]; 
						}
						public void checkClientTrusted(X509Certificate[] certs, String authType) {}
						public void checkServerTrusted(X509Certificate[] certs, String authType) {}
					}};

			// Ignore differences between given hostname and certificate hostname
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) { return true; }
			};

			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
			} catch (Exception e) {
				System.exit(-2);
			}

			try {
				URL url = new URL(site);
				HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
				if(connection == null){
					System.out.println("No connection found");
					return null;
				}

				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; Macintosh)");
				connection.connect();
				return new Scanner(connection.getInputStream());
			} catch (IOException e) {
				return null;
			}			
		}
	}


	public static void main(String[] args){
		new NashitaBhuiyan_View();
	}
}
