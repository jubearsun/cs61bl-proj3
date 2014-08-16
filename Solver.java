import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;


public class Solver {
	
	private HashMap<Board, Board> boardMap = new HashMap<Board, Board>();
	private Stack<Board> navigableBoards = new Stack<Board>();
	private ArrayList<Board> visitedBoards = new ArrayList<Board>();
	private int[] goalDimensions = new int[2];
	
	// used to initialize board, same as Checker
	private Board myBoard;
	private ArrayList<Block> myBlocks;
	private int currentBlock = 1; 
	public static boolean debugging = true;
	
	public static void main(String [] args) throws FileNotFoundException {
		if (args.length != 2) {
			if (debugging) {
				System.out.println(2);
			}
			System.exit(2);
		}
		Solver solve = new Solver(args[0]);
		Board goal = solve.makeGoalBoard(args[1]);	
		if (debugging) {
			goal.printBoard();
			System.out.println("BREAK");
			solve.myBoard.printBoard();
		}
		solve.printMoves(solve.myBoard, goal);	
	}
	
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
					goalDimensions[0] = boardNBlockHelper.get(0);
					goalDimensions[1] = boardNBlockHelper.get(1);
					boardNBlockHelper.clear();
				}
				else if ((i - 2) % 4 == 0) {
					if (boardNBlockHelper.size() != 4) {
						if (debugging) {
							System.out.println(4);
						}
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
				if (debugging) {
					System.out.println(4);
				}
				System.exit(4);
			}
			myBoard.createBoard(myBlocks, 4);
			s.close();
		}
		catch (FileNotFoundException e) {
			if (debugging) {
				System.out.println(3);
			}
			System.exit(3);
		}
	}
	
	public LinkedList<Board> toGoal(Board initial, Board goal) { 
		Board currBoard = null;
		
		navigableBoards.push(initial);
		visitedBoards.add(initial);
		//ListIterator<Board> navIterator = navigableBoards.listIterator();
		while (!navigableBoards.isEmpty()) {
			currBoard = navigableBoards.pop();
			if (debugging) {
				System.out.println(navigableBoards.size());	
			}
			if (currBoard.equalsToGoal(goal)) {	
				//System.out.println("Got to the goal");
				return buildPath(currBoard);
			}
			//navIterator.remove();
			//navigableBoards.remove(currBoard);
			visitedBoards.add(currBoard);
			for (Board move : currBoard.generateMoves()) {
				if (visitedBoards.contains(move)) {
					if (debugging) {
						System.out.println("These moves have already been visited");
						move.printBoard();
						System.out.println("done");
					}
					continue;
				} else {
					boardMap.put(move, currBoard);					
					visitedBoards.add(move);
					navigableBoards.push(move);
					if (debugging) {
						System.out.println("This move is being added to the stack");
						move.printBoard();						
					}
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
	
	public static String getMove(Board before, Board after) {
		StringBuilder move = new StringBuilder();
		if (before.equalsToGoal(after)) {
			if (debugging) {
				System.out.println("Success!");
			}
			System.exit(0);
		}
		mainLoop:
		for (Block a : before.getBlocks()) {
			for (Block b : after.getBlocks()) {
				if (!a.equals(b) && (a.getBlock() == b.getBlock())) {
					move.append(a.getTopLeftRow() + " ");
					move.append(a.getTopLeftCol() + " ");
					move.append(b.getTopLeftRow() + " ");
					move.append(b.getTopLeftCol());
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
			if (debugging) {
				System.out.println("1hi");
			}
			System.exit(1);
		} else if (moves.size() == 1) {
			board1 = moves.getFirst();
			System.out.println(getMove(board1, board1));
			if (debugging) {
				System.out.println("Success!");
			}
			System.exit(0);
		} else {
			Iterator<Board> movesIter = moves.iterator(); 
			board1 = movesIter.next();
			board2 = board1;
			while (movesIter.hasNext()) {
				board1 = board2;
				board2 = movesIter.next();
				System.out.println(getMove(board1, board2)); 
			}
			if (debugging) {
				System.out.println("Success!");
			}
			System.exit(0);
		}
	}
	
	public Board makeGoalBoard(String goalFile) {
		try {
			Board goalBoard = null;
			File goal = new File(goalFile);
			ArrayList<Block> goalBlocks = new ArrayList<Block>();
			Scanner s = new Scanner(goal);
			int i = 1;
			ArrayList<Integer> boardNBlockHelper = new ArrayList<Integer>();
			goalBoard = new Board(goalDimensions[0], goalDimensions[1]);
			while (s.hasNext()) {
				boardNBlockHelper.add(s.nextInt());
				if (i % 4 == 0) {
					if (boardNBlockHelper.size() != 4) {
						if (debugging) {
							System.out.println(4);
						}
						System.exit(4);
					}
					else {
						Block blockToAdd = new Block(boardNBlockHelper, currentBlock);
						currentBlock++;
						goalBlocks.add(blockToAdd);
						boardNBlockHelper.clear();
					}
				}
				i++;
				}
				if (boardNBlockHelper.size() != 0) {
					if (debugging) {
						System.out.println(4);
					}
					System.exit(4);
				}
				
			goalBoard.createBoard(goalBlocks, 4); 
			s.close();
			return goalBoard;
		}
		catch (FileNotFoundException e) {
			if (debugging) {
				System.out.println(4);
			}
			System.exit(4);
		}
		return null;
	}
}
