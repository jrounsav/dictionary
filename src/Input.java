import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Input {
	public ArrayList<String> allInput;
	public Integer linesRead;
	public Integer totalWords;
	public Integer totalReplaced;
	public Integer totalAdded;
	public Integer totalIgnored;
	
	public Input(){
		this.allInput = new ArrayList<String>();
		this.linesRead = 0;
		this.totalWords = 0;
		this.totalReplaced = 0;
		this.totalAdded=0;
		this.totalIgnored=0;
	}
	
	/**
	 * Clear out the ArrayList
	 */
	public void clearList(){
		this.allInput = null;
		this.allInput = new ArrayList<String>();
	}
	
	/**
	 * Add every word in a line from an input source to the ArrayList
	 * 
	 * @param line
	 * @param dirty
	 */
	public void loadLine(String line, boolean dirty){
		String[] lineArray = line.split("\\s+");
		if(!dirty){
			this.linesRead = this.linesRead + 1;
		}
		
		for(String word : lineArray){
			this.allInput.add(word);
			if(!dirty){
				this.totalWords = this.totalWords + 1;
			}
				
		}
		this.allInput.add("\n");
	}
	
	/**
	 * Remove a word from the ArrayList
	 * 
	 * @param line
	 * @param dirty
	 */
	public void removeWord(String line, boolean dirty){
		String[] lineArray = line.split("\\s+");

		for(String word : lineArray){
			this.allInput.remove(word);
			if(!dirty){
				this.totalReplaced=this.totalReplaced + 1;
			}
		}
	}
	
	/**
	 * Print Input contents
	 */
	public void printList(){
		String listVal = this.allInput.toString();
		System.out.println(listVal);
	}
	
	/**
	 * Save the Input ArrayList to a local file in the "saved" directory.
	 * Contains any changes made during replacements.
	 * @param i
	 */
	public void saveList(Integer i){
		String listVal = this.allInput.toString();
		try{
		    PrintWriter writer = new PrintWriter("saved/input-" + i + ".txt", "UTF-8");		    
		    
		    for(String s: this.allInput){
		    	if(!s.equals("\n")){
		    		writer.print(s + " ");
		    	} else {
		    		writer.print(s);
		    	}
		    			    	
		    }
		    
		    writer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
}