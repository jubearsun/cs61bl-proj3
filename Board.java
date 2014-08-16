import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	private int[][] board;
	private int myHeight;
	private int myWidth;
	private ArrayList<Block> blocks;
	
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
			int[] myTopLeft = new int[2];
			myTopLeft = currBlock.getTopLeftCoor();
			if (isBlocked(currBlock, myTopLeft)) {
				System.out.println("unable to make board due to invalid format");
				System.exit(print);
			} else {
				putBlock(currBlock);
			}
		}
	}
	
	private boolean isBlocked(Block block, int[] newSpot) {
		int[] myDimension = block.getDimension();
		ArrayList<Integer> myCoors = new ArrayList<Integer>();
		myCoors.add(newSpot[0]);
		myCoors.add(newSpot[1]);
		myCoors.add(myDimension[0] - 1 + block.getTopLeftRow());
		myCoors.add(myDimension[1] - 1 + block.getTopLeftCol());
		Block newBlock = new Block(myCoors, block.getBlock());
		for (int a = newBlock.getBottomRightRow(); a <= newBlock.getTopLeftRow(); a++) {
			for (int i = newBlock.getTopLeftCol(); i <= newBlock.getBottomRightCol(); i++) {
				if (a > myHeight - 1 || i > myWidth - 1) {
					return true;
				}
				if (board[a][i] != 0 && board[a][i] != newBlock.getBlock()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void putBlock(Block block) {
		int[] myTopLeft = new int[2];
		myTopLeft = block.getTopLeftCoor();
		if (isBlocked(block, myTopLeft)) {
			System.out.println("impossible move, blocked by another block");
			System.exit(6);
		} else {
			for (int a = block.getTopLeftRow(); a <= block.getBottomRightRow(); a++) {
				for (int i = block.getTopLeftCol(); i <= block.getBottomRightCol(); i++) {
					if (a >= myHeight || i > myWidth) {
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
	
	public void makeMove(int[] oldSpot, int[] newSpot) { // changed from private to public
		if (oldSpot[0] > myHeight - 1 || newSpot[0] > myHeight - 1 ||
			oldSpot[1] > myWidth - 1 || newSpot[1] > myWidth - 1) {
			System.out.println("move is out of bound");
			System.exit(6);
		}
		int blockIndicator = board[oldSpot[0]][oldSpot[1]];
		if (blockIndicator == 0) {
			System.out.println("invalid input, block does not exists");
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
	
	private boolean isLegalMove(Block old, int[] newSpot) {
		int newRightCornerRow = old.getBottomRightRow() + (newSpot[0]+ old.getTopLeftRow());
		int newRightCornerCol = old.getBottomRightCol() + (newSpot[1]- old.getTopLeftCol());
		//System.out.println(old);
		//System.out.println(newRightCornerCol + " " + " " + old.getBottomRightRow() + " " + old.getBottomRightCol());
		boolean middle = true;

		try {
			for (int a = newRightCornerRow; a <= newSpot[0]; a++) {
				for (int b = newSpot[1]; b <= newRightCornerCol; b++) {
					if (board[a][b] != 0) {
						middle = false;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			middle = false;
		}
		
		try {
			boolean legality = middle && !((old.getTopLeftRow() > myHeight - 1 || newSpot[0] > myHeight - 1 ||
				old.getTopLeftCol() > myWidth - 1 || newSpot[1] > myWidth - 1) || 
				board[old.getTopLeftRow()][old.getTopLeftCol()] == 0 ||
				(board[newSpot[0]][newSpot[1]] != 0 && 
				(board[newRightCornerRow][newRightCornerCol] != 0 ||
				board[newRightCornerRow][newRightCornerCol] != old.getBlockIndicator())
				));
			return legality;
		} catch (ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	public ArrayList<Board> generateMoves() {
		ArrayList<Board> results = new ArrayList<Board>();
		Board moveRight = new Board(myHeight, myWidth);
		moveRight.createBoard(blocks, 4);
		Board moveLeft = new Board(myHeight, myWidth);
		moveLeft.createBoard(blocks, 4);
		Board moveUp = new Board(myHeight, myWidth);
		moveUp.createBoard(blocks, 4);
		Board moveDown =new Board(myHeight, myWidth);
		moveDown.createBoard(blocks, 4);
		for (Block block : this.blocks) {
				Block curr = block;
				int[] toMove = block.getTopLeftCoor();
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
				
				if (isLegalMove(curr, right)) {
					moveRight.makeMove(toMove, right);
					results.add(moveRight);
				} 
				if (isLegalMove(curr, left)) {
					moveLeft.makeMove(toMove, left);
					results.add(moveLeft);
				} 
				if (isLegalMove(curr, up)) {
					moveUp.makeMove(toMove, up);
					results.add(moveUp);
				}
				if (isLegalMove(curr, down)) {
					moveDown.makeMove(toMove, down);
					results.add(moveDown);
				}					
		}
		return results;
	}
	
	@Override
	public boolean equals(Object otherBoard) {
		Board other = (Board) otherBoard;
		if (this.getBlocks().size() != other.getBlocks().size()) {
			return false;
		}
		outerloop:
		for (int i = 0; i < this.getBlocks().size(); i++) {
			for (int j = 0; j < other.getBlocks().size(); j++) {
				if (this.getBlocks().get(i).equals(other.getBlocks().get(j)) && !other.getBlocks().get(i).getEqualsMark()) {
					other.getBlocks().get(i).setEqualsMark(true);
					this.getBlocks().get(i).setEqualsMark(true);		
					continue outerloop;
				}
			}
		}
		for (int i = 0; i < this.getBlocks().size(); i++) {
			if (!this.getBlocks().get(i).getEqualsMark() || !other.getBlocks().get(i).getEqualsMark()) {
				return false;
			}
		}
		for (int i = 0; i < other.getBlocks().size(); i++) {
			other.getBlocks().get(i).setEqualsMark(false);
			this.getBlocks().get(i).setEqualsMark(false);
		}
		return true;
	}	
	
	public boolean equalsToGoal(Object otherBoard) {
		Board goal = (Board) otherBoard;
		for (int i = 0; i < goal.getBlocks().size(); i++) {
			if (this.getBlock(goal.getBlocks().get(i).getTopLeftCoor()) == null) {
				return false;
			}
		}
		return true;
	}
}
