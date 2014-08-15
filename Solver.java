import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;


public class Solver {
	
	private HashMap<Board, Board> boardMap = new HashMap<Board, Board>();
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
			myBoard.createBoard(myBlocks);
		}
		catch (FileNotFoundException e) {
			System.out.println(3);
			System.exit(3);
		}
	}
	
	public LinkedList<Board> toGoal(Board initial, Board goal) { 
		Board currBoard = null;
				
		navigableBoards.add(initial);
		Iterator<Board> navIterator = navigableBoards.iterator();
		while (navIterator.hasNext()) {
			currBoard = navIterator.next();
			if (currBoard.equals(goal)) { 
				return buildPath(currBoard);
			}
			navigableBoards.remove(currBoard);
			visitedBoards.add(currBoard);
			for (Board move : currBoard.generateMoves()) {
				if (visitedBoards.contains(move)) {
					continue;
				}
				if (!navigableBoards.contains(move)) {
					boardMap.put(move, currBoard);
					navigableBoards.add(move);
				}
			}
		}
		return null;
	}
	
	private LinkedList<Board> buildPath(Board start) {
		LinkedList<Board> path = new LinkedList<Board>();
		Board current = start;
		Board parent;
		while (current != null) {
			path.addFirst(current);
			parent = boardMap.get(current);
			current = parent; 
		}
		return path;
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
	
	public void printMoves(Board init, Board goal) { 
		LinkedList<Board> moves = toGoal(init, goal);
		Board board1;
		Board board2;
		if (moves == null) {
			System.out.println(1);
			System.exit(1);
		} else {
			Iterator<Board> movesIter = moves.iterator(); 
			board1 = movesIter.next();
			board2 = board1;
			while (movesIter.hasNext()) {
				board1 = board2;
				board2 = movesIter.next();
				System.out.println(getMove(board1, board2)); 
			}
			System.out.println(0);
			System.exit(0);
		}
	}
}
