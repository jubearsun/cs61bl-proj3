

import java.util.ArrayList;

public class Board {
	private int[][] board;
	private int myHeight;
	private int myWidth;
	private ArrayList<Block> blocks;
	private ArrayList<Block> myBlocks;
	
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
	
	public void createBoard() { // this was changed from private
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
	
	public void makeMove(int[] oldSpot, int[] newSpot) { // changed from private to public
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
		toBeAddedCoors.add(newSpot[0] + toBeRemoved.getDimension()[0] - 1);
		toBeAddedCoors.add(newSpot[1] +toBeRemoved.getDimension()[1] - 1);
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
	
	public ArrayList<Block> getBlocks() {
		return blocks;
	}
	
	private boolean isLegalMove(Block old, int[] newSpot) {
		int newRightCornerRow = old.getBottomRightRow() + (newSpot[0]-old.getTopLeftRow());
		int newRightCornerCol = old.getBottomRightCol() + (newSpot[1]-old.getTopLeftCol());
		return !((old.getTopLeftRow() > myHeight - 1 || newSpot[0] > myHeight - 1 ||
				old.getTopLeftCol() > myWidth - 1 || newSpot[1] > myWidth - 1) || 
				board[old.getTopLeftRow()][old.getTopLeftCol()] == 0 ||
				(board[newSpot[0]][newSpot[1]] != 0 && 
				(board[newRightCornerRow][newRightCornerCol] != 0 ||
				board[newRightCornerRow][newRightCornerCol] != old.getBlockIndicator())
				));
	}
	
	public ArrayList<Board> generateMoves() {
		ArrayList<Board> results = new ArrayList<Board>();
		Board moveRight = this;
		Board moveLeft = this;
		Board moveUp = this;
		Board moveDown = this;
		for (Block block : this.blocks) {
			if (isBlocked(block)) {
				continue;
			} else {
				Block curr = block;
				int[] toMove = block.getTopLeftCoor();
				int[] right = new int[2];
				right[0] = toMove[0];
				right[1] = toMove[1] + 1;
				int[] left = new int[2];
				left[0] = toMove[0];
				left[1] = toMove[1] - 1;
				int[] up = new int[2];
				right[0] = toMove[0] - 1;
				right[1] = toMove[1];
				int[] down = new int[2];
				right[0] = toMove[0] + 1;
				right[1] = toMove[1];
				
				if (isLegalMove(curr, right)) {
					moveRight.makeMove(toMove, right);
					results.add(moveRight);
				} else if (isLegalMove(curr, left)) {
					moveLeft.makeMove(toMove, left);
					results.add(moveLeft);
				} else if (isLegalMove(curr, up)) {
					moveUp.makeMove(toMove, up);
					results.add(moveUp);
				} else if (isLegalMove(curr, down)) {
					moveDown.makeMove(toMove, down);
					results.add(moveDown);
				}					
			}
		}
		return results;
	}
}
