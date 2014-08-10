import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;


public class Solver {
	
	PriorityQueue<Checker.Board> fringe = new PriorityQueue<Checker.Board>(); //probably not going to use this in the end
	
	//Going to try to implement A* search
	//http://www.policyalmanac.org/games/aStarTutorial.htm

	Set<Checker.Board> navigableBoards = new HashSet<Checker.Board>();
	Set<Checker.Board> visitedBoards = new HashSet<Checker.Board>();
	
	
	
	public ArrayList<Checker.Board> toGoal(Checker.Board initial, Checker.Board goal) { //return type is not set yet; still debating on best data structure
		Checker.Board currBoard = null;
		
		ArrayList<Checker.Board> rtn = new ArrayList<Checker.Board>(); //data structure is probably going to change
		
		navigableBoards.add(initial);
		while (!navigableBoards.isEmpty()) {
			currBoard = rtn.get(0);
			if (currBoard.equals(goal)) { //need to define a .equals method
				return rtn;
			}
			navigableBoards.remove(currBoard);
			visitedBoards.add(currBoard);
			
			for (Checker.Board move : currBoard.generateMoves()) {
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
}
