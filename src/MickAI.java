import java.util.*;

public class MickAI implements Player {
	String me;
	int maxdepth;
	public int getTotalAdjacentBlank(State s,String color){
		MyList color_list = s.ownees(color);
		int total_adjacent = 0;
		Iterator it = color_list.iterator();
		while (it.hasNext()) {
			Hexpos hp = (Hexpos) it.next();
			total_adjacent += getAdjacentBlank(s,hp);
		}
//		System.out.println(total_adjacent);
		return total_adjacent;
	}
	public int getAdjacentBlank(State s,Hexpos hp){
		
		// iterate over a list of all neighbours
		int adjacent=0;
		MyList neighbours = hp.neighbours();
		Iterator it = neighbours.iterator();
		while (it.hasNext()) {
			Hexpos neighbour = (Hexpos) it.next();
			// only 'change' ownership if the square was not empty
			if (s.owner(neighbour) == null)
				adjacent++;
//				changeOwner(player, neighbour);
		}
		
		return adjacent;
	}
	// Assign an evaluation value to a state: higher values are better
	private int evalState(State s) {
//		int diff = s.getnRed() - s.getnBlue();
		int diff = (int)(((float)s.getnRed() - (float)getTotalAdjacentBlank(s,"red")/9)
				- ((float)s.getnBlue() - (float)getTotalAdjacentBlank(s,"blue")/9));
		if (me.equals("red")) {
			return diff;
		}
		return -diff;
	}

	public MickAI(int md) {
		maxdepth = md;
	}

	/*
	 * evalMove returns the minimax-value of a Move m that should be applied to
	 * State s, where depth indicates how deep the algorithm should search. A
	 * cutoff test (evalState) will be used if depth == 0 or if there are no new
	 * moves possible (e.g. at the end of the game).
	 */
	public int evalMove(Move m, State s, int depth, int boundary) {
		boolean findingMin = !s.whoseTurn().equals(me);
		int myBoundary = findingMin? -10000 : 10000;
		if (depth == 0) { // cutting off search -> evaluate resulting state
			return evalState(s.tryMove(m));
		} else { // minimax-search
			// update the state
			State newstate = s.tryMove(m);

			// get a list of possible moves from newstate
			MyList moves = newstate.findMoves();

			// shuffle so we don't always find the same maximum
			Collections.shuffle(moves);

			// return utility of state if there are no new moves
			if (moves.size() == 0)
				return evalState(newstate);

			// Iterate over all moves
			Iterator it = moves.iterator();

			// We already know there is at least one move, so use it to
			// initialize minimaxvalue
			int minimaxvalue = evalMove((Move) it.next(), newstate, depth - 1,
					myBoundary);
			if ( (findingMin && myBoundary < minimaxvalue) || 
					(!findingMin && myBoundary > minimaxvalue) ) {
				myBoundary = minimaxvalue;
			}

			// now the rest of the moves
			while (it.hasNext()) {
				Move newmove = (Move) it.next();

				if ((findingMin &&  boundary < myBoundary) ||
						(!findingMin && boundary > myBoundary ))
					return myBoundary;

				int eval = evalMove(newmove, newstate, depth - 1, myBoundary);
				if ( (findingMin && myBoundary < minimaxvalue) || 
						(!findingMin && myBoundary > minimaxvalue) ) {
					myBoundary = eval;
				}
				// if this is our turn, save the maximumvalue, otherwise
				// the mimimumvalue
				if (newstate.whoseTurn().equals(me)) // our turn
					minimaxvalue = Math.max(minimaxvalue, eval);
				else
					// opponent's turn
					minimaxvalue = Math.min(minimaxvalue, eval);
			}

			return minimaxvalue;
		}

	}

	/*
	 * This function is called by Arbiter. It is about the actual state of the
	 * game, not some future state we generated.
	 */
	public Move chooseMove(State s) {
		// remember who we are so we can correctly evaluate states
		me = s.whoseTurn();
		int myBoundary = -10000;
		// The rest of this function looks a lot like evalMove, except
		// that it doesn't just track the minimaxvalue, but also the
		// best move itself.

		// get a list of possible moves
		MyList moves = s.findMoves();

		Collections.shuffle(moves);
		// shuffle so we don't always take the same maximum

		// return if there are no moves
		if (moves.size() == 0)
			return null;

		// Iterate over all moves
		Iterator it = moves.iterator();

		// We already know there is at least one move, so use it to
		// initialize minimaxvalue
		Move bestMove = (Move) it.next();
		long startTime = System.currentTimeMillis();
		int minimaxvalue = evalMove(bestMove, s, maxdepth, myBoundary);

		// now the rest of the moves
		while (it.hasNext()) {
			Move move = (Move) it.next();
			int eval = evalMove(move, s, maxdepth, myBoundary);
			if ( eval > myBoundary){
				myBoundary = eval;
			}
			// if we found a better move, remember it
			if (eval > minimaxvalue) {
				minimaxvalue = eval;
				bestMove = move;
			}
		}
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("MickAI:" + estimatedTime);
		return bestMove;
	}
}
