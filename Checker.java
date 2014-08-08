import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Checker {
	
	private boolean[][] myBoard;
	private ArrayList<Block> myBlocks;
	private int toBeReturned;
	
	public static void main(String[] args) throws FileNotFoundException {
		Checker checker = new Checker(args[0]);
		checker.printBoard();
	}
	
	public Checker(String init) throws FileNotFoundException {
		File initFile = new File(init);
		myBlocks = new ArrayList<Block>();
		Scanner s = new Scanner(initFile);
		int i = 1;
		ArrayList<Integer> boardNBlockHelper = new ArrayList<Integer>();
		while (s.hasNext()) {
			boardNBlockHelper.add(s.nextInt());
			if (i == 2) {
				createBoardSize(boardNBlockHelper.get(0), boardNBlockHelper.get(1));
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
			throw new IllegalArgumentException();
		}
		this.createBoard();
	}
	
	private void createBoardSize(int height, int width) {
		myBoard = new boolean[height][width];
	}
	
	private void createBoard() {
		for (int i = 0; i < myBlocks.size(); i ++) {
			Block currBlock = myBlocks.get(i); 
			if (isBlocked(currBlock)) {
				toBeReturned = 5; //The board layout is impossible; 5 is returned.
				System.out.println("ERROR");
			} else {
				putBlock(currBlock);
			}
		}
	}
	
	private void putBlock(Block block) {
		for (int a = block.getTopLeftRow(); a <= block.getBottomRightRow(); a++) {
			for (int i = block.getTopLeftCol(); i <= block.getBottomRightCol(); i++) {
				myBoard[a][i] = true;
			}
		}
	}
	
	private boolean isBlocked(Block block) {
		for (int a = block.getBottomRightRow(); a <= block.getTopLeftRow(); a++) {
			for (int i = block.getTopLeftCol(); i <= block.getBottomRightCol(); i++) {
				if (myBoard[a][i] == true) {
					return true;
				}
			}
		}
		return false;
	}
	
	private class Block {
		private int[] topLeftCoor = new int[2];
		private int[] bottomRightCoor = new int[2];
		
		public Block (ArrayList<Integer> myCoors) {
			if (myCoors.size() != 4) {
				throw new IllegalArgumentException();
			} else {
				topLeftCoor[0] = myCoors.get(0);
				topLeftCoor[1] = myCoors.get(1);
				bottomRightCoor[0] = myCoors.get(2);
				bottomRightCoor[1] = myCoors.get(3);
			}
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
		
		public String toString() {
			return topLeftCoor[0] + " " + topLeftCoor[1] + " " + bottomRightCoor[0] + " " + bottomRightCoor[1];
		}
	}
	
	public void printBoard() {
		for (int i = 0; i < myBoard.length; i++) {
			for (int a = 0; a < myBoard[i].length; a++) {
				if (myBoard[i][a] == true) {
					System.out.print(i + ":" + a + "/T ");
				} else {
					System.out.print(i + ":" +a + "/F ");
				}
			}
			System.out.println();
		}
	}

}
