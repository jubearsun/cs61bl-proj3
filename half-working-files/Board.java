import java.util.ArrayList;
import java.util.HashSet;

public class Board {
	private int[][] board;
	private int myHeight;
	private int myWidth;
	private ArrayList<Block> blocks;
	private static boolean debugging = false;
	
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
	
	public void createBoard(ArrayList<Block> myBlocks, int print) { 
		for (int i = 0; i < myBlocks.size(); i ++) {
			Block currBlock = myBlocks.get(i);
			if (isBlocked(currBlock, currBlock.getTopLeftCoor())) {
				System.out.println("unable to make board due to invalid format");
				System.exit(print);
			} else {
				putBlock(currBlock);
			}
		}
	}
//	
//	private boolean isBlocked(Block block) {
//		for (int a = block.getBottomRightRow(); a <= block.getTopLeftRow(); a++) {
//			for (int i = block.getTopLeftCol(); i <= block.getBottomRightCol(); i++) {
//				if (board[a][i] != 0) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
	
	private boolean isBlocked(Block block, int[] newSpot) {
		int[] myDimension = block.getDimension();
		ArrayList<Integer> myCoors = new ArrayList<Integer>();
		myCoors.add(newSpot[0]);
		myCoors.add(newSpot[1]);
		myCoors.add(myDimension[0] - 1 + newSpot[0]);
		myCoors.add(myDimension[1] - 1 + newSpot[1]);
		if (myCoors.get(2) > myHeight - 1 || myCoors.get(3) > myWidth - 1 || newSpot[0] < 0 || newSpot[1] < 0) {
			return true;
		}
		Block newBlock = new Block(myCoors, block.getBlock());
		for (int a = newBlock.getTopLeftRow(); a <= newBlock.getBottomRightRow(); a++) {
			for (int i = newBlock.getTopLeftCol(); i <= newBlock.getBottomRightCol(); i++) {
				if (board[a][i] != 0) { 
					if (board[a][i] != newBlock.getBlock()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	private void putBlock(Block block) {
		if (isBlocked(block, block.getTopLeftCoor())) {
			System.out.println("impossible move, blocked by another block");
			System.exit(6);
		} else {
			for (int a = block.getTopLeftRow(); a <= block.getBottomRightRow(); a++) {
				for (int i = block.getTopLeftCol(); i <= block.getBottomRightCol(); i++) {
					if (a >= myHeight || i >= myWidth) {
						System.out.println("move is out of bound");
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
	
	public void makeMove(int[] oldSpot, int[] newSpot) {
		if (oldSpot[0] > myHeight - 1 || newSpot[0] > myHeight - 1 ||
			oldSpot[1] > myWidth - 1 || newSpot[1] > myWidth - 1) {
			System.out.println("move is out of bound");
			System.exit(6);
		}
		int blockIndicator = board[oldSpot[0]][oldSpot[1]];
		if (blockIndicator == 0) {
			System.out.println("invalid input, unable to make move");
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
		toBeAddedCoors.add(newSpot[0] + toBeRemoved.getDimension()[0] - 1);
		toBeAddedCoors.add(newSpot[1] +toBeRemoved.getDimension()[1] - 1);
		if (toBeAddedCoors.size() != 4) {
			System.out.println("input is incorrectly formatted");
			System.exit(5);
		}
		else {
			toBeAdded = new Block(toBeAddedCoors, blockIndicator);
			this.removeBlock(toBeRemoved);
			this.putBlock(toBeAdded);
		}
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
	
	public ArrayList<Block> getBlocks() {
		return blocks;
	}
	
	
	
//	private boolean isLegalMove(Block old, String direction) {
//		int[] dimensions = old.getDimension();
//		int row;
//		int col;
//		try {
//			if (direction.equals("right")) {
//				col = old.getBottomRightCoor()[1] + 1;
//				row = old.getBottomRightCoor()[0]; 
//				for (int i = 0; i < dimensions[0]; i++) {
//					if (board[row - i][col] != 0) {
//						return false;
//					}
//				}
//				return true;
//			}
//			else if (direction.equals("left")) {
//				col = old.getTopLeftCoor()[1] - 1;
//				row = old.getTopLeftCoor()[0]; 
//				for (int i = 0; i < dimensions[0]; i++) {
//					if (board[row + i][col] != 0) {
//						return false;
//					}
//				}
//				return true;
//			}
//			else if (direction.equals("down")) {
//				col = old.getBottomRightCoor()[1];
//				row = old.getBottomRightCoor()[0] + 1; 
//				for (int i = 0; i < dimensions[1]; i++) {
//					if (board[row][col - i] != 0) {
//						return false;
//					}
//				}
//				return true;
//			} else {
//				col = old.getTopLeftCoor()[1];
//				row = old.getTopLeftCoor()[0] - 1; 
//				for (int i = 0; i < dimensions[1]; i++) {
//					if (board[row][col + i] != 0) {
//						return false;
//					}
//				}
//				return true;
//			}
//		} catch (ArrayIndexOutOfBoundsException e) {
//			return false;	
//		}
//	}
	
	public ArrayList<Board> generateMoves() {
		ArrayList<Board> results = new ArrayList<Board>();
		if (debugging) {
			System.out.println("The current board is ");
			this.printBoard();
		}
		for (Block block : this.blocks) {
			Board moveRight = new Board(myHeight, myWidth);
			moveRight.createBoard(blocks, 4);
			Board moveLeft = new Board(myHeight, myWidth);
			moveLeft.createBoard(blocks, 4);
			Board moveUp = new Board(myHeight, myWidth);
			moveUp.createBoard(blocks, 4);
			Board moveDown = new Board(myHeight, myWidth);
			moveDown.createBoard(blocks, 4);
			Block curr = block;
			int[] toMove = block.getTopLeftCoor();
			if (debugging) {
				System.out.println("Trying to move the block labeled " + curr.getBlockIndicator());
			}
			int[] right = new int[2];
			right[0] = toMove[0];
			right[1] = toMove[1] + 1;
			int[] left = new int[2];
			left[0] = toMove[0];
			left[1] = toMove[1] - 1;
			int[] up = new int[2];
			up[0] = toMove[0] - 1;
			up[1] = toMove[1];
			int[] down = new int[2];
			down[0] = toMove[0] + 1;
			down[1] = toMove[1];			
			if (!isBlocked(curr, right)) {
				moveRight.makeMove(toMove, right);
				results.add(moveRight);
				if (debugging) {
					System.out.println("success, right");
				}
			} 
			if (!isBlocked(curr, left)) {
				moveLeft.makeMove(toMove, left);
				results.add(moveLeft);
				if (debugging) {
					System.out.println("success, left");
				}
			} 
			if (!isBlocked(curr, up)) {
				moveUp.makeMove(toMove, up);
				results.add(moveUp);
				if (debugging) {
					System.out.println("success, up");
				}
			}
			if (!isBlocked(curr, down)) {
				moveDown.makeMove(toMove, down);
				results.add(moveDown);
				if (debugging) {
					System.out.println("success, down");
				}
			}					
		}
		if (debugging) {
			System.out.println("THE RESULTS ARE");
			for (Board element: results) {
				element.printBoard();
				System.out.println("--------");
			}
			System.out.println("DONE");
		}
		return results;
	}
	
	@Override
	public boolean equals(Object otherBoard) {
		Board other = (Board) otherBoard;
		if (other.blocks.size() != this.getBlocks().size()) {
			return false;
		}
		HashSet<Block> check = new HashSet<Block>();
		for (Block element: this.getBlocks()) {
			check.add(element);
		}
		for (Block element: other.getBlocks()) {
			if (!check.contains(element)) {
				return false;
			}
		}
		return true;
	}
		
//		Board other = (Board) otherBoard;
//		boolean check = false;
//		if (this.getBlocks().size() != other.getBlocks().size()) {
//			return false;
//		}
//		for (int i = 0; i < this.getBlocks().size(); i++) {
//			check = false;
//			for (int j = 0; j < other.getBlocks().size(); j++) {
//				if (this.getBlocks().get(i).equals(other.getBlocks().get(j))) {		
//					check = true;
//				}
//			}
//			if (!check) {
//				return false;
//			}
//		}
//		return true;
//	}	
	
	public boolean equalsToGoal(Object otherBoard) {
		Board other = (Board) otherBoard;
		HashSet<Block> check = new HashSet<Block>();
		for (Block element: this.getBlocks()) {
			check.add(element);
		}
		for (Block element: other.getBlocks()) {
			if (!check.contains(element)) {
				return false;
			}
		}
		return true;
	}
}
