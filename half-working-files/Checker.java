
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Checker {
	
	private Board myBoard;
	private ArrayList<Block> myBlocks;
	private int currentBlock = 1; 
	
	public static void main(String [] args) throws FileNotFoundException {
		if (args.length != 2) {
			System.out.println("Invalid inputs, needs exactly 2 inputs");
			System.exit(2);
		}
		Checker init = new Checker(args[0]);
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
			}
			catch (NoSuchElementException e) {
				System.out.println("Invalid input, needs 4 integers seperated by spaces");
				System.exit(4);
			}
		}
		init.CheckGoal(goalBlocks);
		s.close();
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
					if (boardNBlockHelper.size() != 4) {
						System.out.println("init file is incorrectly formatted");
						System.exit(5);
					}
					else {
						Block blockToAdd = new Block(boardNBlockHelper, currentBlock);
						currentBlock++;
						myBlocks.add(blockToAdd);
						boardNBlockHelper.clear();
					}
				}
				i++;
			}
			s.close();
			if (boardNBlockHelper.size() != 0) {
				System.out.println("init file is incorrectly formatted");
				System.exit(5);
			}
			myBoard.createBoard(myBlocks, 5);
		}
		catch (FileNotFoundException e) {
			System.out.println("No such file found");
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
			System.out.println("impossible or invalid moves");
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
					rtnBlocks.add(new Block(myCoors, currentBlock));
					currentBlock++;
					myCoors.clear();
					}
			} catch (NoSuchElementException e) {
				System.out.println("goal file is incorrectly formatted");
				System.exit(5);
			}
			gBlocks.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("No such file found");
			System.exit(3);
		}	
		return rtnBlocks;
	}
	
	public void CheckGoal(ArrayList<Block> gBlocks) {
		int blocksMatchedSoFar = 0;
		ArrayList<Block> myBlocks = myBoard.getBlocks();
		for (Block b: gBlocks) {
			for (Block checker: myBlocks) {
				if (checker.equals(b)) {
					blocksMatchedSoFar++;
					break;
				}
			}
		}
		if (gBlocks.size() == blocksMatchedSoFar) {
			System.out.println("successfully solved the puzzle");
			System.exit(0);
		} else {
			System.out.println("input moves do not solve the puzzle");
			System.exit(1);
		}
	}
	
	public void printBoard() {
		myBoard.printBoard();
	}
	
	
}
