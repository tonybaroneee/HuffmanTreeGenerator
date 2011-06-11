package com;
/* Graphical user interface for the Huffman Tree Generator
 * 
 * Author: acb
 */

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;

@SuppressWarnings("serial")
public class MainInterface extends JFrame implements AWTEventListener {
	
	// ActionListener
	private class ProcessListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	if (inputString.getText().equals("")) {
        		clearPanels();
        		JOptionPane.showMessageDialog(null, "Word/phrase must " +
        				"be at least 1 character.");
        	} else {
        		HuffmanTreeGen.processString(inputString.getText());
        	}
        }
    }
	
	// KeyListener
	public class KeyProcessListener implements KeyListener {
	    public void keyPressed(KeyEvent e) {	 
	        switch (e.getKeyCode()) {
	        case KeyEvent.VK_ENTER:
	        	if (inputString.getText().equals("")) {
	        		clearPanels();
	        		JOptionPane.showMessageDialog(null, "Word/phrase must " +
	        				"be at least 1 character.");
	        	} else {
	        		HuffmanTreeGen.processString(inputString.getText());
	        	}
	        }
	    }
	    public void keyReleased(KeyEvent e) { }
	    public void keyTyped(KeyEvent e) { }
	}
	
	// Components
	
	// ***************************************** //
	ProcessListener listener = new ProcessListener();
	KeyProcessListener keyListen = new KeyProcessListener();
	Container container = getContentPane();
	
	JPanel mainPanel = new JPanel(new BorderLayout());
	JPanel userInputPanel = new JPanel(new BorderLayout());
	JPanel symbolPanel = new JPanel();
	JPanel resultsPanel = new JPanel(new GridLayout(1,2));
	JPanel totalResultsPanel = new JPanel(new BorderLayout());
	JPanel endResultsPanel = new JPanel(new GridLayout(0,2));
	JPanel treePanel = new JPanel();
	
	JLabel promptInput = new JLabel("Enter text to parse: ");
	JLabel totalSymbolsLabel = new JLabel("Total # of Symbols: ");
	JLabel avgEntropyLabel = new JLabel("Average Entropy: ");
	JLabel bitMinLabel = new JLabel("Bit Count - Theoretical Min: ");
	JLabel bitAsciiLabel = new JLabel("Bit Count - Fixed Length (ASCII):");
	
	JTextField inputString = new JTextField();
	JTextField totalSymbols = new JTextField();
	JTextField avgEntropy = new JTextField();
	JTextField bitMin = new JTextField();
	JTextField bitAscii = new JTextField();
	JTextArea bitValues = new JTextArea();
	
	JXTable symbolTable;
	DefaultTableModel symbolModel;
	JScrollPane scrollPane;
	
	JButton process = new JButton("Parse");
	// ***************************************** //
	
	
	/** 
	 * Constructor
	 */
	public MainInterface() {
		
		container.setLayout(new BorderLayout());
		addKeyListener(keyListen);
        getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
		process.addActionListener(listener);
		
		userInputPanel.add(promptInput, BorderLayout.WEST);
		userInputPanel.add(inputString, BorderLayout.CENTER);
		userInputPanel.add(process, BorderLayout.EAST);
		
		String[] names = {"Symbol", "Frequency", "Probability", "Info Content", "Entropy"};
		symbolModel = new DefaultTableModel(null, names);
		symbolTable = new JXTable(symbolModel);
		symbolTable.setEditable(false);
		symbolTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(symbolTable);
		symbolPanel.add(scrollPane);
		
		endResultsPanel.add(totalSymbolsLabel);
		endResultsPanel.add(totalSymbols);
		endResultsPanel.add(avgEntropyLabel);
		endResultsPanel.add(avgEntropy);
		endResultsPanel.add(bitMinLabel);
		endResultsPanel.add(bitMin);
		endResultsPanel.add(bitAsciiLabel);
		endResultsPanel.add(bitAscii);
		totalResultsPanel.add(endResultsPanel, BorderLayout.SOUTH);
		totalResultsPanel.add(symbolPanel, BorderLayout.CENTER);

		totalSymbols.setEditable(false);
		avgEntropy.setEditable(false);
		bitMin.setEditable(false);
		bitAscii.setEditable(false);
		
		resultsPanel.add(treePanel);
		resultsPanel.add(totalResultsPanel);
		
		mainPanel.add(userInputPanel, BorderLayout.NORTH);
		mainPanel.add(resultsPanel, BorderLayout.CENTER);
		container.add(mainPanel);
	}
	
	public void clearPanels() {
		int rowCount = symbolTable.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			symbolModel.removeRow(0);
		}
		bitValues.setText("");
		symbolTable.repaint();
		symbolTable.updateUI();
	}
	
	public void addSymbolStats(String symbol, Integer freq, double probability, 
															double infoContent,
															double entropy) {
		String[] row = new String[5];
		
		probability = formatDouble(probability);
		infoContent = formatDouble(infoContent);
		entropy = formatDouble(entropy);
		
		row[0] = symbol;
		row[1] = "" + freq;
		row[2] = "" + probability;
		row[3] = "" + infoContent;
		row[4] = "" + entropy;
		symbolModel.addRow(row);
	}
	
	public void addResults(int numSymbols, double avgEntrpy, double bMin, double bAscii) {
		avgEntrpy = formatDouble(avgEntrpy);
		bMin = formatDouble(bMin);
		bAscii = formatDouble(bAscii);
		
		totalSymbols.setText("" + numSymbols);
		avgEntropy.setText("" + avgEntrpy);
		bitMin.setText("" + bMin);
		bitAscii.setText("" + bAscii);
	}
	
	public void paintStats() {
		totalResultsPanel.add(symbolPanel, BorderLayout.CENTER);
		totalResultsPanel.repaint();
		totalResultsPanel.updateUI();
	}
	
	public void paintBitPatterns(HashSet<TreeNode> results) {
		List<TreeNode> resultsList = new LinkedList<TreeNode>();
		resultsList.addAll(results);
		Collections.sort(resultsList, new Comparator<TreeNode>() {
			public int compare(TreeNode o1, TreeNode o2)
			{
				return ((Integer)o1.getBit().length()).
				compareTo((Integer)o2.getBit().length());
			}
		});
		bitValues.setPreferredSize(new Dimension(380, 480));
		bitValues.append("Resulting Bit Values:\n" +
			"(Assuming left branches are 0 and right" +
			" branches are 1)\n-----\n\n");
		for (TreeNode element : resultsList) {
			bitValues.append(element + "\n");
		}
		treePanel.add(bitValues);
	}
	
	public double formatDouble(double value) {
		double result = value * 100;
		result = Math.round(result);
		result = result / 100;
		return result;
	}

	public void eventDispatched(AWTEvent event) {
		if ( event instanceof KeyEvent) {
            KeyEvent kEvent = (KeyEvent)event;
            // relay key event to all registered key listeners	         
            for (KeyListener keylist : this.getKeyListeners()) {
                if ( kEvent.getID() == KeyEvent.KEY_PRESSED) { 
                    keylist.keyPressed(kEvent); 
                } 
            }
        }
	}
}