import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;


public class Solver {
	
	PriorityQueue<Board> fringe = new PriorityQueue<Board>(); //probably not going to use this in the end
	
	//Going to try to implement A* search
	//http://www.policyalmanac.org/games/aStarTutorial.htm

	Set<Board> navigableBoards = new HashSet<Board>();
	Set<Board> visitedBoards = new HashSet<Board>();
	
	
	
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
		for (Checker.Block a : before.getBlocks()) {
			for (Checker.Block b : after.getBlocks()) {
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
