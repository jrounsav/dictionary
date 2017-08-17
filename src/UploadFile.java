import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class UploadFile {
	
	public UploadFile(){
		
	}
	/**
    * Upload Contents of Dictionary file to a dictionary
    */
   public void uploadDictionary(File selectedFile, HashMapify map)
   {
	   int count = 0;
	   try
	   {
		   Scanner s1 = new Scanner(selectedFile);
		   while(s1.hasNextLine())
		   {
			   count = count + 1;
			   s1.nextLine();
		   }
		   String[] word = new String[count];
		   
		   Scanner s2 = new Scanner(selectedFile);
		   for(int index = 0; index < count; index++)
		   {
			   word[index] = s2.next();
		   }
		   
		   for(int j = 0; j < word.length; j++)
		   {
			   map.addWord(word[j], false);
		   }
	   }
	   catch(FileNotFoundException ex)
	   {
		   JOptionPane.showMessageDialog(null, "File not found");
	   }
	   catch(IOException ex)
	   {
		   JOptionPane.showMessageDialog(null, "Error reading file");
	   }
	  
   }
   /**
    * Upload contents of Input file to the Input object
    * 
    * @param selectedFile
    * @param inFile
    */
   public static void uploadInput(File selectedFile, Input inFile)
   {
	   int count = 0;
	   try
	   {
		   Scanner s1 = new Scanner(selectedFile);
		   while(s1.hasNextLine())
		   {
			   count = count + 1;
			   s1.nextLine();
		   }		  
		   String[] line = new String[count];
		   
		   Scanner s2 = new Scanner(selectedFile);
		   for(int index = 0; index < count; index++)
		   {
			   line[index] = s2.nextLine();
		   }
		   
		   for(int j = 0; j < line.length; j++)
		   {
			   inFile.loadLine(line[j], false);
		   }
		   
	   }
	   catch(FileNotFoundException ex)
	   {
		   JOptionPane.showMessageDialog(null, "File not found");
//		   System.out.println("File not found");
	   }
	   catch(IOException ex)
	   {
		   JOptionPane.showMessageDialog(null, "Error reading file");
//		   System.out.println("Error reading file");
	   }
	  
   }
}