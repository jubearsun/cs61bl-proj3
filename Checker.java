import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class Checker {
	
	private Board myBoard;
	private ArrayList<Block> myBlocks;
	private int currentBlock = 1;
	
	public static void main(String [] args) throws FileNotFoundException {
		if (args.length != 2) {
			System.out.println(2);
			System.exit(2);
		}
		Checker init = new Checker(args[0]);
		init.printBoard();
		ArrayList<Block> goalBlocks = init.makeGoalBlocks(args[1]);
		int[] oldSpot = new int[2];
		int[] newSpot = new int[2];
		Scanner s = new Scanner(System.in);
		while (s.hasNext()) {
			try {
				oldSpot[0] = s.nextInt();
				oldSpot[1] = s.nextInt();
				newSpot[0] = s.nextInt();
				newSpot[1] = s.nextInt();
				init.makeMove(oldSpot, newSpot);
				init.printBoard();
			}
			catch (NoSuchElementException e) {
				System.out.println(4);
				System.exit(4);
			}
		}
		init.CheckGoal(goalBlocks);
	}
	
	public Checker(String init) {
		try {
			File initFile = new File(init);
			myBlocks = new ArrayList<Block>();
			Scanner s = new Scanner(initFile);
			int i = 1;
			ArrayList<Integer> boardNBlockHelper = new ArrayList<Integer>();
			while (s.hasNext()) {
				boardNBlockHelper.add(s.nextInt());
				if (i == 2) {
					myBoard = new Board(boardNBlockHelper.get(0), boardNBlockHelper.get(1));
					boardNBlockHelper.clear();
				}
				else if ((i - 2) % 4 == 0) {
					Block blockToAdd = new Block(boardNBlockHelper);
					myBlocks.add(blockToAdd);
					boardNBlockHelper.clear();
				}
				i++;
			}
			if (boardNBlockHelper.size() != 0) {
				System.out.println(5);
				System.exit(5);
			}
			myBoard.createBoard();
		}
		catch (FileNotFoundException e) {
			System.out.println(3);
			System.exit(3);
		}
	}
	
	public Checker() {
		myBoard = null;
		myBlocks = null;
		currentBlock = 0;
	}

	public void makeMove(int[] oldSpot, int[] newSpot) {
		if (Math.abs(oldSpot[0] + oldSpot[1] - newSpot[0] - newSpot[1]) > 1) {
			System.out.println(6);
			System.exit(6);
		}
		myBoard.makeMove(oldSpot, newSpot);
	}
	
	public Block getBlock(int[] topLeft) {
		return myBoard.getBlock(topLeft);
	}
	
	public ArrayList<Block> makeGoalBlocks(String goalFile) {
		ArrayList<Block> rtnBlocks = new ArrayList<Block>();
		try {
			File goalBlocks = new File(goalFile);
			Scanner gBlocks = new Scanner(goalBlocks);
			try {
				ArrayList<Integer> myCoors = new ArrayList<Integer>();
				while (gBlocks.hasNext()) {
					myCoors.add(gBlocks.nextInt());
					myCoors.add(gBlocks.nextInt());
					myCoors.add(gBlocks.nextInt());
					myCoors.add(gBlocks.nextInt());
					rtnBlocks.add(new Block(myCoors));
					myCoors.clear();
				}
			} catch (NoSuchElementException e) {
				System.out.println(5);
				System.exit(5);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println(3);
			System.exit(3);
		}
		return rtnBlocks;
	}
	
	public void CheckGoal(ArrayList<Block> gBlocks) {
		int blocksMatchedSoFar = 0;
		ArrayList<Block> myBlocks = myBoard.blocks;
		for (Block b: gBlocks) {
			for (Block checker: myBlocks) {
				if (checker.equals(b)) {
					blocksMatchedSoFar++;
					break;
				}
			}
		}
		if (gBlocks.size() == blocksMatchedSoFar) {
			System.out.println(0);
			System.exit(0);
		} else {
			System.out.println(1);
			System.exit(1);
		}
	}
	
	public void printBoard() {
		myBoard.printBoard();
	}
	
	private class Board {
		int[][] board;
		int myHeight;
		int myWidth;
		ArrayList<Block> blocks;
		
		public Board(int height,int width) {
			blocks = new ArrayList<Block>();
			myHeight = height;
			myWidth = width;
			board = new int[height][width];
		}
		
		public Block getBlock(int[] topLeft) {
			for (int i = 0; i < blocks.size(); i++) {
				Block currBlock = blocks.get(i);
				if (topLeft[0] == currBlock.getTopLeftRow() && topLeft[1] == currBlock.getTopLeftCol()) {
					return currBlock;
				}
			}
			return null;
		}
		
		private void createBoard() {
			for (int i = 0; i < myBlocks.size(); i ++) {
				Block currBlock = myBlocks.get(i);
				if (isBlocked(currBlock)) {
					System.out.println(5);
					System.exit(5);
				} else {
					putBlock(currBlock);
				}
			}
		}
		
		private boolean isBlocked(Block block) {
			for (int a = block.getBottomRightRow(); a <= block.getTopLeftRow(); a++) {
				for (int i = block.getTopLeftCol(); i <= block.getBottomRightCol(); i++) {
					if (board[a][i] != 0) {
						return true;
					}
				}
			}
			return false;
		}
		
		private void putBlock(Block block) {
			if (isBlocked(block)) {
				System.out.println(6);
				System.exit(6);
			} else {
				for (int a = block.getTopLeftRow(); a <= block.getBottomRightRow(); a++) {
					for (int i = block.getTopLeftCol(); i <= block.getBottomRightCol(); i++) {
						if (a >= myHeight || i > myWidth) {
							System.exit(6);
						} else {
							board[a][i] = block.getBlock();
						}
					}
				}
			}
			blocks.add(block);
		}
		
		private void removeBlock(Block block) {
			int blockIndicator = block.getBlock();
			for (int i = 0; i < myHeight; i++) {
				for (int a = 0; a < myWidth; a++) {
					if (board[i][a] == blockIndicator) {
						board[i][a] = 0;
					}
				}
			}
			blocks.remove(block);
		}
		
		private void makeMove(int[] oldSpot, int[] newSpot) {
			if (oldSpot[0] > myHeight - 1 || newSpot[0] > myHeight - 1 ||
				oldSpot[1] > myWidth - 1 || newSpot[1] > myWidth - 1) {
				System.out.println(6);
				System.exit(6);
			}
			int blockIndicator = board[oldSpot[0]][oldSpot[1]];
			if (blockIndicator == 0) {
				System.out.println(6);
				System.exit(6);
			}
			Block toBeRemoved = null;
			Block toBeAdded;
			ArrayList<Integer> toBeAddedCoors = new ArrayList<Integer>();
			toBeAddedCoors.add(newSpot[0]);
			toBeAddedCoors.add(newSpot[1]);
			for (int i = 0; i < blocks.size(); i++) {
				Block currBlock = blocks.get(i);
				if (currBlock.getTopLeftRow() == oldSpot[0] && currBlock.getTopLeftCol() == oldSpot[1]) {
					toBeRemoved = currBlock;
				}
			}
			toBeAddedCoors.add(newSpot[0] + toBeRemoved.dimension[0] - 1);
			toBeAddedCoors.add(newSpot[1] +toBeRemoved.dimension[1] - 1);
			toBeAdded = new Block(toBeAddedCoors, blockIndicator);
			this.removeBlock(toBeRemoved);
			this.putBlock(toBeAdded);
		}
		
		
		public void printBoard() {
			for (int i = 0; i < myHeight; i++) {
				for (int a = 0; a < myWidth; a++) {
					int myBlock = board[i][a];
					System.out.print(myBlock + " ");
				}
				System.out.println();
			}
		}
	}
	
	public class Block {
		private int[] topLeftCoor = new int[2];
		private int[] bottomRightCoor = new int[2];
		private int blockIndicator; //number that helps distinct one block from another.
		private int[] dimension = new int[2];
		
		public Block (ArrayList<Integer> myCoors) {
			blockIndicator = currentBlock;
			currentBlock++;
			if (myCoors.size() != 4) {
				System.out.println(5);
				System.exit(5);
			} else {
				topLeftCoor[0] = myCoors.get(0);
				topLeftCoor[1] = myCoors.get(1);
				bottomRightCoor[0] = myCoors.get(2);
				bottomRightCoor[1] = myCoors.get(3);
				makeDimension(myCoors);
			}
		}
		
		public Block (ArrayList<Integer> myCoors, int myBlockInd) {
			System.out.println(myCoors.toString());
			blockIndicator = myBlockInd;
			if (myCoors.size() != 4) {
				System.out.println(5);
				System.exit(5);
			} else {
				topLeftCoor[0] = myCoors.get(0);
				topLeftCoor[1] = myCoors.get(1);
				bottomRightCoor[0] = myCoors.get(2);
				bottomRightCoor[1] = myCoors.get(3);
				makeDimension(myCoors);
			}
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
}
