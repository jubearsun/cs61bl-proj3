import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;


public class Solver {
	
	PriorityQueue<Board> fringe = new PriorityQueue<Board>(); //probably not going to use this in the end
	
	//Going to try to implement A* search
	//http://www.policyalmanac.org/games/aStarTutorial.htm

	private Set<Board> navigableBoards = new HashSet<Board>();
	private Set<Board> visitedBoards = new HashSet<Board>();
	
	// used to initialize board, same as Checker
	private Board myBoard;
	private ArrayList<Block> myBlocks;
	private int currentBlock = 1; 
	
	public Solver(String init) {
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
						System.out.println(4);
						System.exit(4);
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
			if (boardNBlockHelper.size() != 0) {
				System.out.println(4);
				System.exit(4);
			}
			myBoard.createBoard(); // change to take in myBlocks as an input
		}
		catch (FileNotFoundException e) {
			System.out.println(3);
			System.exit(3);
		}
	}
	
	public ArrayList<Board> toGoal(Board initial, Board goal) { //return type is not set yet; still debating on best data structure
		Board currBoard = null;
		
		ArrayList<Board> rtn = new ArrayList<Board>(); //data structure is probably going to change
		
		navigableBoards.add(initial);
		Iterator<Board> navIterator = navigableBoards.iterator();
		while (navIterator.hasNext()) {
			currBoard = navIterator.next();
			if (currBoard.equals(goal)) { //need to define a .equals method
				return rtn;
			}
			navigableBoards.remove(currBoard);
			visitedBoards.add(currBoard);
			for (Board move : currBoard.generateMoves()) {
				if (visitedBoards.contains(move)) {
					continue;
				}
				
				if (!navigableBoards.contains(move)) {
					rtn.add(move);
					//More needed to construct the path to goal
					navigableBoards.add(move);
				}
			}
		}
		
		
		return rtn;
	}
	
	
	public String getMove(Board before, Board after) {

		StringBuilder move = new StringBuilder();
		mainLoop:
		for (Block a : before.getBlocks()) {
			for (Block b : after.getBlocks()) {
				if (!a.equals(b) && (a.getBlock() == b.getBlock())) {
					move.append(a.getTopLeftCol());
					move.append(a.getTopLeftRow());
					move.append(b.getTopLeftCol());
					move.append(b.getTopLeftRow());
					break mainLoop;
				}
			}
		}
		return move.toString();
	}
}
