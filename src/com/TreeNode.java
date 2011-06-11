package com;
/* Helper class representing node for Huffman tree.
 * 
 * Author: acb
 */

public class TreeNode {
	
	// Variables
	private String symbol, bit;
	private int count;
	private TreeNode leftNode;
	private TreeNode rightNode;
	private boolean visited = false;
	
	// Methods
	public TreeNode(int count, String symbol) {
		this.count = count;
		this.symbol = symbol; 
		this.leftNode = null;
		this.rightNode = null;
		bit = "";
	}
	public TreeNode(int count, String symbol, TreeNode left, TreeNode right) {
		this.count = count;
		this.symbol = symbol; 
		this.leftNode = left;
		this.rightNode = right;
		bit = "";
	}
	public TreeNode(TreeNode node) {
		this.count = node.getCount();
		this.symbol = node.getSymbol(); 
		this.leftNode = node.getLeft();
		this.rightNode = node.getRight();
		bit = "";
	}
	public String getSymbol() {return symbol;}
	public int getCount() {return count;}
	public String getBit() {return bit;}
	public void setBit(String bit) {this.bit = bit;}
	public TreeNode getLeft() {return leftNode;}
	public TreeNode getRight() {return rightNode;}
	public void setVisited() { visited = true; }
	public boolean visited() { return visited; }
	public String toString() {
		return (symbol + "=" + bit);
	}

} // TreeNode
