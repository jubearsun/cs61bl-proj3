

import java.util.ArrayList;

public class Block {
	
	private int[] topLeftCoor = new int[2];
	private int[] bottomRightCoor = new int[2];
	private int blockIndicator; //number that helps distinct one block from another.
	private int[] dimension = new int[2]; 
	private boolean equalsMark = false; // used in determining if two boards are equal
	
	public Block (ArrayList<Integer> myCoors, int myBlockInd) {
		blockIndicator = myBlockInd;
		topLeftCoor[0] = myCoors.get(0);
		topLeftCoor[1] = myCoors.get(1);
		bottomRightCoor[0] = myCoors.get(2);
		bottomRightCoor[1] = myCoors.get(3);
		makeDimension(myCoors);	
	}
	
	public void makeDimension(ArrayList<Integer> myCoors) {
		dimension[0] = myCoors.get(2) - myCoors.get(0) + 1;
		dimension[1] = myCoors.get(3) - myCoors.get(1) + 1;
	}
	
	public int getBlock() {
		return blockIndicator;
	}
	
	public int getTopLeftRow() {
		return topLeftCoor[0];
	}
	
	public int getTopLeftCol() {
		return topLeftCoor[1];
	}
	
	public int getBottomRightRow() {
		return bottomRightCoor[0];
	}
	
	public int getBottomRightCol() {
		return bottomRightCoor[1];
	}
	
	public int[] getDimension() {
		return dimension;
	}
	
	public int[] getTopLeftCoor() {
		return topLeftCoor;
	}
	
	public int getBlockIndicator() {
		return blockIndicator;
	}
	
	public boolean getEqualsMark() {
		return equalsMark;
	}
	
	public void setEqualsMark(boolean x) {
		this.equalsMark = x;
	}
	
	public boolean equals(Block other) {
		return (this.dimension[0] == other.dimension[0]) &&
			   (this.dimension[1] == other.dimension[1]) &&
			   (this.getTopLeftRow() == other.getTopLeftRow()) &&
			   (this.getTopLeftCol() == other.getTopLeftCol()) &&
			   (this.getBottomRightRow() == other.getBottomRightRow()) &&
			   (this.getBottomRightCol() == other.getBottomRightCol());		   
	}
	
	public String toString() {
		return topLeftCoor[0] + " " + topLeftCoor[1] + " " + bottomRightCoor[0] + " " + bottomRightCoor[1];
	}
}
