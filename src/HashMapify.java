import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList; import java.util.Collection; import java.util.HashMap; import java.util.Iterator; import java.util.List; import java.util.Map.Entry;

import javax.swing.JOptionPane;

import java.util.Set;



public class HashMapify{
	
	public HashMap<String, String> map;
	public Integer cleanCount = 0;
	public Integer dirtyCount;
	
	/**
	 * Constructor to generate a base map
	 */
	public HashMapify(){
		this.map = new HashMap();
		this.dirtyCount = 0;
	}
	
	/**
	 * Clean print of all dictionary files
	 */
	public void printValues(){
		ArrayList<String> list = new ArrayList<String>();
		
		mapToArray((HashMap)this.map, "", list);
		System.out.println(list);
	}
	
	/**
	 * Save dictionary values to a file in dictionary consumable format
	 * @param i Integer An index to prevent file overrides
	 */
	public void saveValues(Integer i){
		ArrayList<String> list = new ArrayList<String>();
		
		// Un-HashMap the map and send to list for printing
		mapToArray((HashMap)this.map, "", list);
		
		// Create the dictionary file in the "saved" folder at root of project
		try{
		    PrintWriter writer = new PrintWriter("saved/dictionary-" + i + ".txt", "UTF-8");		    
		    for(String str : list) {
		    	writer.println(str);
			}
		    writer.close();
		} catch (IOException e) {
//		   System.out.println(e);
			JOptionPane.showMessageDialog(null, e);
		}
		
		
	}
	
	/**
	 * Recursively parse all words in dictionary so that they are human-readable
	 * and add them to an ArrayList
	 * 
	 * @param source HashMap to parse
	 * @param s String to pass down for Array
	 * @param target ArrayList where everything should end up
	 */
	public void mapToArray(HashMap source, String s, ArrayList target){
		
		// Dictionaries are based on a key system.
		// Grab each key
		for (Object key: source.keySet()) {
			String append = s;
			append += key.toString(); 
			
			// If the key is a terminator, add the string to the array
			if(key.toString().equals("0")){
				target.add(s); // If the
			}
			// If the key is not a terminator and the corresponds to data
			// recursively call self with the data, appended string, and target array
			if(!key.toString().equals("0") && source.get(key) != null){
				mapToArray((HashMap) source.get(key), append, target);
			}
		}		
	}
	
	/**
	 * Simple function to return this object's HashMap
	 * @return
	 */
	public HashMap getMap(){
		return this.map;
	}
	
	/**
	 * Begins the process of mapping the words by checking if 
	 * they exist and then running mapWord.
	 * @param word The word to be added
	 * @param dirty Flag passed to determine if this is the original file upload or a new addition
	 */
	public void addWord(String word, boolean dirty){
		boolean wordExists = this.checkforWord(word, this.map);
		if(!wordExists){
			String sterile = this.steralize(word);
			mapWord(sterile, this.map);
			if(!dirty){
				cleanCount = cleanCount + 1;
			} 
			else{
				dirtyCount = dirtyCount +1;
			}
		}
		
	}	
	
	/**
	 * Fix the input to avoid special characters case.
	 * @param word
	 * @return
	 */
	public String steralize(String word){
		
		String steralized = word.replaceAll("[^a-zA-Z]+","");
		steralized = steralized.toLowerCase();
		
		return steralized;
		
	}
	
	/**
	 * Adds the first character as a key and a new HashMap as the
	 * value. Passes the rest of the word recursively to be mapped until there
	 * are no more characters.
	 * @param word
	 */
	public void mapWord(String word, HashMap map){
		Integer len = word.length();		
		if(len > 0){				
			String first = word.substring(0,1);
			String rest = word.substring(1, len);
			
			if(map.containsKey(first)){
				mapWord(rest, (HashMap) map.get(first));
			}
			else {
				HashMap submap = newMap();
				map.put(first, submap);
				mapWord(rest, submap);				
			}				
		} else {
			HashMap submap = newMap();
			map.put(0, submap);
		}	
	}

	/**
	 * Checks to see if a word exists in the dictionary
	 * @param word
	 * @param map
	 * @return boolean 
	 */
	public boolean checkforWord(String word, HashMap map){

		String sterile = steralize(word);
		int len = sterile.length();
		
		if(len == 0){
			return true;	
		}
		
		String first = sterile.substring(0,1);
		String rest = sterile.substring(1, len);
		if(map.containsKey(first) && len > 0){
			if(len == 1){
				HashMap m1 = (HashMap)map.get(first);
				Boolean endkey = m1.containsKey(0);
				if(endkey == true){
					return true;
				} else {
					return false;
				}				
			}				
			return checkforWord(rest, (HashMap)map.get(first));
		}
		return false;
	}
	
	/**
	 * Generate a new HashMap
	 * @return HashMap A new map
	 */
	public HashMap newMap(){
		HashMap newmap = new HashMap();
		return newmap;
	}
	
	
	
}