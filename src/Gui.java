
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Gui extends JFrame {

	/**
	 * Setting variables at start
	 */
	private JLabel item1;
	private JButton open;
	private JButton open2;
	private JLabel item2;
	private JButton start;
	private JButton ignore;
	private JButton add;
	private JButton replace;
	private JButton stats;
	private JButton newDictionary;
	private JButton saveUploads;
	private JButton helpScreen;
	private JFileChooser fc;
	private JTextArea output;
	private HashMapify map;
	private Input inFile;
	private UploadFile up;
	private comparisonHandler handler;
	private InputHandler handler1;
	private DictionaryHandler handler2;
	private replaceHandler handler5;
	private statsHandler handler6;
	private newFileHandler handler7;
	private saveHandler handler8;
	private helpHandler handler9;
	private Integer dcount = 0;

	/**
	 * Constructor
	 */
	public Gui() {

		super("Spell Check");

		// Instantiating custom classes
		this.map = new HashMapify();
		this.inFile = new Input();
		this.up = new UploadFile();

		// Configuring layout
		setLayout(new FlowLayout());
		this.item1 = new JLabel("add Input text file");
		this.item1.setToolTipText("add file");
		add(this.item1);
		this.open = new JButton("Input");
		add(this.open);
		this.item2 = new JLabel("Choose Dictionary file");
		this.item2.setToolTipText("add file");
		add(this.item2);
		this.open2 = new JButton("Dictionary");
		add(this.open2);
		this.start = new JButton("Start Comparison");
		add(this.start);
		this.output = new JTextArea("The sentence from the inputFile", 5, 33);
		this.output.setEditable(false);
		add(this.output);
		this.replace = new JButton("Replace the Word");
		add(this.replace);
		this.stats = new JButton("Print stats");
		add(this.stats);
		this.newDictionary = new JButton("Clear Uploads");
		add(this.newDictionary);
		this.saveUploads = new JButton("Save Uploads");
		add(this.saveUploads);
		this.helpScreen = new JButton("Help");
		add(this.helpScreen);

		// Adding handlers to elements
		this.handler = new comparisonHandler(this.inFile, this.map);
		this.start.addActionListener(this.handler);
		this.handler1 = new InputHandler(this.inFile, this.up);
		this.handler2 = new DictionaryHandler(this.map, this.up);
		this.open.addActionListener(this.handler1);
		this.open2.addActionListener(this.handler2);
		this.handler5 = new replaceHandler(this.map, this.inFile, this.output);
		this.replace.addActionListener(this.handler5);
		this.handler6 = new statsHandler(this.inFile, this.up);
		this.stats.addActionListener(this.handler6);
		this.handler7 = new newFileHandler(this);
		this.newDictionary.addActionListener(this.handler7);
		this.handler8 = new saveHandler(this);
		this.saveUploads.addActionListener(this.handler8);
		this.handler9 = new helpHandler();
		this.helpScreen.addActionListener(this.handler9);
	}

	/**
	 * Custom handler to handle comparing Input & Dictionary files
	 */
	private class comparisonHandler implements ActionListener {
		public Input inFile;
		public HashMapify map;

		/**
		 * Constructor
		 * Contains references to necessary classes in order to access their functions
		 * 
		 * @param inFile Reference to Input class
		 * @param map Reference to HashMapify
		 */
		public comparisonHandler(Input inFile, HashMapify map) {
			this.inFile = inFile;
			this.map = map;
		}

		/**
		 * Action: Compare all words in the input file with the words stored in
		 * the dictionary and give the user the options of Add or Ignore
		 */
		public void actionPerformed(ActionEvent event) {

			ArrayList<String> missing = new ArrayList<String>();

			boolean flag = true;
			// Add all missing words to an array
			for (String s : inFile.allInput) {
				flag = map.checkforWord(s, map.map);
				if (!flag) {
					// Verify that there are no duplicates in the array of missing words
					if (!missing.contains(s)) {
						missing.add(s);
					}					
				} 
			}
			
			// For every missing word, give the option to add to dictionary
			// via a Dialog Box
			for (String s : missing) {
				
				Object[] options1 = { "Add to Dictionary", "Ignore the Word" };

				JPanel panel = new JPanel();
				panel.add(new JLabel(s + " is missing from the dictionary"));

				int addIgnoreDialog = JOptionPane.showOptionDialog(null, panel, "Error: missing",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);
				if (addIgnoreDialog == JOptionPane.YES_OPTION) {
					map.addWord(s, true);
				} else if(addIgnoreDialog == JOptionPane.NO_OPTION) {
					this.inFile.totalIgnored = this.inFile.totalIgnored += 1;
				}

			}
		}
	}

	/**
	 * Custom Handler for Input file uploads
	 */
	private class InputHandler implements ActionListener {
		public UploadFile up;
		public Input inFile;

		/**
		 * Constructor
		 * Contains references to necessary classes in order to access their functions
		 * 
		 * @param inFile Reference to Input class
		 * @param map Reference to HashMapify
		 */
		public InputHandler(Input inFile, UploadFile up) {
			this.inFile = inFile;
			this.up = up;
		}

		/**
		 * Action: Add all words in an input file to the program.
		 * Add all text to the output text area.
		 */
		public void actionPerformed(ActionEvent event) {
			fc = new JFileChooser();
			fc.setCurrentDirectory(new java.io.File(""));
			fc.setDialogTitle("Please Choose File");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();

				this.up.uploadInput(selectedFile, this.inFile);

				try {
					FileReader r = new FileReader(selectedFile);
					output.read(r, "");
				} catch (Exception e) {

				}
			} else {
				JOptionPane.showMessageDialog(null, "The user did not choose any file");
			}
		}
	}

	/**
	 * Custom handler for dictionary uploads. Assumes each word is on a new line.
	 */
	private class DictionaryHandler implements ActionListener {
		public HashMapify map;
		public UploadFile up;

		public DictionaryHandler(HashMapify map, UploadFile up) {
			this.map = map;
			this.up = up;
		}

		/**
		 * Action: Basic file upload with call to HashMapify class for uplaod
		 */
		public void actionPerformed(ActionEvent event) {

			fc = new JFileChooser();
			fc.setCurrentDirectory(new java.io.File(""));
			fc.setDialogTitle("Please Choose File");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {

				File selectedFile2 = fc.getSelectedFile();
				this.up.uploadDictionary(selectedFile2, this.map);
				this.map.printValues();
			} else {
				JOptionPane.showMessageDialog(null, "The user did not choose any file");
			}

		}
	}

	/**
	 * Custom handler to deal with the replacement of Input words via the output textArea
	 */
	private class replaceHandler implements ActionListener {
		public HashMapify map;
		public Input inFile;
		public JTextArea output;

		public replaceHandler(HashMapify map, Input inFile, JTextArea output) {
			this.map = map;
			this.inFile = inFile;
			this.output = output;
		}

		/**
		 * Action: Create popup to collect replacement text, and replace the
		 * selected output text
		 */
		public void actionPerformed(ActionEvent event) {
			JTextArea ta = new JTextArea(5, 5);
			JOptionPane.showMessageDialog(null, new JScrollPane(ta));
			String s = ta.getText();
			
			// Try splitting the input text on ever space & replacing the selection
			try {
				String[] lineArray = this.output.getSelectedText().split("\\s+");

				inFile.totalReplaced += lineArray.length;
				
				this.output.replaceSelection(s);
			} catch(NullPointerException n){
				
			}

			// Clear the Input file's ArrayList and refresh it with 
			// the contents of the output textarea
			this.inFile.clearList();

			for (String line : this.output.getText().split("\\n")) {
				this.inFile.loadLine(line, true);
			}
		}
	}

	/**
	 * Custom Handler for the Statistics button. Grabs and displays usage data.
	 */
	private class statsHandler implements ActionListener {
		public UploadFile up;
		public Input inFile;

		public statsHandler(Input inFile, UploadFile up) {
			this.inFile = inFile;
			this.up = up;
		}

		public void actionPerformed(ActionEvent event) {

			try (PrintWriter out = new PrintWriter("filename.txt")) {
				out.println("Words in Input: " + inFile.totalWords);
				out.println("Lines read: " + inFile.linesRead);
				out.println("Input Words replaced: " + inFile.totalReplaced);
				out.println("Words Added to Dictionary: " + map.dirtyCount);
				out.println("Input words ignored: " + inFile.totalIgnored);

				String str = "";
				
				str += "Words in Input: " + inFile.totalWords + "\n";
				str += "Lines read: " + inFile.linesRead + "\n";
				str += "Input Words replaced: " + inFile.totalReplaced + "\n";
				str += "Words Added to Dictionary: " + map.dirtyCount + "\n";
				str += "Input words ignored: " + inFile.totalIgnored + "\n";
				
				JOptionPane.showMessageDialog(null, str);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Custom handler that deals with clearing out all of the currently
	 * loaded files and data.
	 */
	private class newFileHandler implements ActionListener {
		public Gui gui;

		public newFileHandler(Gui g) {
			this.gui = g;
		}

		/**
		 * Action: Call clearAll function
		 */
		public void actionPerformed(ActionEvent event) {
			clearAll();
		}
	}

	/**
	 * Custom Handler that deals with saving the currently loaded
	 * Input and Dictionary to the "saved" directory at the root
	 * of the project.
	 */
	private class saveHandler implements ActionListener {
		public Gui gui;

		public saveHandler(Gui g) {
			this.gui = g;
		}

		/**
		 * Call the saveFiles function
		 */
		public void actionPerformed(ActionEvent event) {
			gui.saveFiles();
		}
	}
	
	/**
	 * Basic help modal
	 */
	private class helpHandler implements ActionListener {

		public helpHandler() {

		}

		public void actionPerformed(ActionEvent event) {
				String str = "";
				
				str += "How to Use\n";
				str += "\n";
				str += "You must upload a dictionary and input file for this ";
				str += "to work. Please remember to do so every time you start the ";
				str += "app or Clear the uploads." + "\n";
				str += "" + "\n";
				str += "Once files have been uploaded you may update the Input ";
				str += "by highlighting words in the textarea and clicking the Replace ";
				str += "button" + "\n";
				str += "" + "\n";
				str += "If you are happy with the input please hit Start Comparison ";
				str += "and you will be given the option to add words from the Input ";
				str += "file to the Dictionary" + "\n";
				str += "" + "\n";
				str += "When you are happy with the Input & Dictionary states, save them ";
				str += "by hitting the save button. You can access them again from the root ";
				str += "of this project." + "\n";
				str += "" + "\n";
				str += "After saving feel free to hit the clear button and then upload ";
				str += "a new Input/Dictionary combination." + "\n";
				str += "" + "\n";
				str += "Also, you may get usage stats by hitting the statistics button anytime." + "\n";
				
				JOptionPane.showMessageDialog(null, str);
		}
	}

	/**
	 * Removes all uploaded file data from the GUI so that
	 * new files can be uploaded.
	 */
	public void clearAll() {
		// Clear textarea
		this.output.setText("");
		this.map.printValues();
		// Reset Dictionary
		this.map = null;
		this.map = new HashMapify();
		this.map.printValues();
		
		// Reset Input
		this.inFile = null;
		this.inFile = new Input();

		// Reset all actionListeners to refresh internal object data
		this.start.removeActionListener(this.handler);
		this.handler = null;
		this.handler = new comparisonHandler(this.inFile, this.map);

		this.open.removeActionListener(this.handler1);
		this.open2.removeActionListener(this.handler2);
		this.handler1 = null;
		this.handler2 = null;
		this.handler1 = new InputHandler(this.inFile, this.up);
		this.handler2 = new DictionaryHandler(this.map, this.up);

		this.replace.removeActionListener(this.handler5);
		this.handler5 = null;
		this.handler5 = new replaceHandler(this.map, this.inFile, this.output);

		this.stats.removeActionListener(this.handler6);
		this.handler6 = null;
		this.handler6 = new statsHandler(this.inFile, this.up);

		this.newDictionary.removeActionListener(this.handler7);
		this.handler7 = null;
		this.handler7 = new newFileHandler(this);

		this.start.addActionListener(this.handler);
		this.open.addActionListener(this.handler1);
		this.open2.addActionListener(this.handler2);

		this.replace.addActionListener(this.handler5);
		this.stats.addActionListener(this.handler6);
		this.newDictionary.addActionListener(this.handler7);

	}

	/**
	 * Call the external object save functions and increment
	 * the number of Input/Dictionary combinations for the file save
	 */
	public void saveFiles() {
		dcount++;
		this.map.saveValues(this.dcount);
		this.inFile.saveList(this.dcount);
		JOptionPane.showMessageDialog(null, "Saved Files!");
	}

}
