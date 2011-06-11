package com;
/* Main file for generating Huffman coding tree and statistics.
 * 
 * Author: acb
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JFrame;

class HuffmanTreeGen {
	
	static MainInterface huffmanGUI;
	
	public static void main(String args[]) {
		huffmanGUI = new MainInterface();
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		Dimension dim = toolkit.getScreenSize();
		huffmanGUI.setTitle("Huffman Coding Statistics");
		huffmanGUI.setLocation((dim.width/2)-450, (dim.height/2)-300);
		huffmanGUI.setSize(900, 600);
		huffmanGUI.setVisible(true);
		huffmanGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	static public void processString(String input) {
		
		// ***************************************** //
		
		SortedMap<String, Integer> elements = new TreeMap<String, Integer>();
		ArrayList<Map.Entry<String, Integer>> tempList;
		ArrayList<TreeNode> huffmanTree;
		TreeNode parentNode, left, right;
		
		// ***************************************** //
		
		
		// remove leading/trailing whitespaces
		input.trim();
		
		// fill in the map with characters and occurance numbers
		for ( int i = 0; i < input.length(); i++ ) {
			String tempSymbol = input.substring(i, i+1);
			if (tempSymbol.equals(" ")) {
				tempSymbol = "<ws>";
			}
			if ( elements.containsKey( tempSymbol ) ) {
				int tempCount = elements.get(tempSymbol);
				elements.put(tempSymbol, tempCount + 1);
			} else {
				elements.put(tempSymbol, 1);
			}
		}
		
		// sort the generated map by frequency value
		tempList = new ArrayList<Map.Entry<String, Integer>>(elements.entrySet());
		Collections.sort(tempList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, java.lang.Integer> o1, Map.Entry<String, Integer> o2)
			{
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		// instantiate huffman tree to store TreeNodes
		huffmanTree = new ArrayList<TreeNode>(tempList.size());
		
		// while also creating TreeNodes and adding to tree
		int i = 0;
		for (Entry<String,Integer> e : tempList) {
			TreeNode node = new TreeNode(e.getValue(), e.getKey());
			huffmanTree.add(i++, node);
		}

		getHuffmanData(tempList, input.length());
		
		// reverse the array for tree construction purposes
		Collections.reverse(huffmanTree);
		
		// perform the joining of the 2 lowest ndoes until list only has one node (this is head node)
		while (huffmanTree.size() != 1) {
			left = new TreeNode(huffmanTree.remove(0));
			right = new TreeNode(huffmanTree.remove(0));
			String parentName = left.getSymbol() + right.getSymbol();
			int parentCount = left.getCount() + right.getCount();
			left.setBit("0");
			right.setBit("1");
			
			parentNode = new TreeNode(parentCount, parentName, left, right);
			huffmanTree.add(parentNode);
			
			// sort list again by values to ensure lowest counts are first
			Collections.sort(huffmanTree, new Comparator<TreeNode>() {
				public int compare(TreeNode o1, TreeNode o2) {
					return ((Integer)o1.getCount()).compareTo((Integer)o2.getCount());
				}
			});
		}
		
		TreeNode itrNode = new TreeNode(huffmanTree.get(0));		
		getBitValues(itrNode);
	}
	
	
	static void getHuffmanData(ArrayList<Map.Entry<String, Integer>> elements, int totalLength) {
		double avgEntropy = 0.0;
		double probability;
		double infoContent;
		double entropy;
		
		huffmanGUI.clearPanels();
		
		for (Map.Entry<String, Integer> e : elements) {
			
			// calculate probability, information content, and entropy
			probability = (double)e.getValue() / (double)totalLength;
			infoContent = -1.0 * (Math.log10(probability) / Math.log10(2));
			entropy = probability * infoContent;
			avgEntropy += entropy;
			
			// pass information to GUI for display purposes
			huffmanGUI.addSymbolStats(e.getKey(), e.getValue(), probability, infoContent, entropy);
		}
		huffmanGUI.paintStats();
		huffmanGUI.addResults(totalLength, avgEntropy, avgEntropy * (double)totalLength, 
				totalLength * 8);
	}
	
	
	static void getBitValues(TreeNode head) {
		ArrayList<TreeNode> stack = new ArrayList<TreeNode>();
		HashSet<TreeNode> results = new HashSet<TreeNode>();
		TreeNode tempHead, tempLeft, tempRight;
		
		// sift through tree, assigning bit values along the way
		stack.add(0, head);
		while (stack.size() != 0) {
			tempHead = stack.remove(0);
			
			if (tempHead.getRight() != null) {
				tempRight = tempHead.getRight();
				tempRight.setBit(tempHead.getBit() + "1");
				stack.add(0, tempRight);
			} else if (!tempHead.visited()) {
				results.add(tempHead);
				tempHead.setVisited();
			}
			if (tempHead.getLeft() != null) {
				tempLeft = tempHead.getLeft();
				tempLeft.setBit(tempHead.getBit() + "0");
				stack.add(0, tempLeft);
			} else if (!tempHead.visited()){
				results.add(tempHead);
				tempHead.setVisited();
			}
		}
		huffmanGUI.paintBitPatterns(results);
	}
	
} // HuffmanTreeGen