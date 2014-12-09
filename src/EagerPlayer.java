/* Liesbeth Flobbe
 * Hexxagon game
 * file: EagerPlayer.java
 */

/* EagerPlayer has a simple function for assigning utility values to a
 * State. It picks the Move that immediately results in the State with
 * the highest utility value (it does not think further ahead than his own
 * move). This has the same result as MinimaxPlayer with depth = 0;
 */ 

import java.util.*;

public class EagerPlayer implements Player {
    String me;

    // Assign an evaluation value to a state: higher values are better
    private int evalState(State s) {
	if (me.equals("red"))
	    return s.getnRed() - s.getnBlue();
	
	return s.getnBlue() - s.getnRed();
    }	

    /* This function is called by Arbiter. */
    public Move chooseMove(State s) {
	// remember who we are (evalState likes to know that)
	me = s.whoseTurn();

	// get a list of possible moves
	MyList moves = s.findMoves();

	// return if there are no moves
	if (moves.size() == 0)
	    return null;
	
	// introduce some randomness
	Collections.shuffle(moves);

	// Iterate over all moves
	Iterator it = moves.iterator();

	// We already know there is at least one move, so use it to
	// initialize max and bestMove.
	Move bestMove = (Move) it.next();
	int max = evalState(s.tryMove(bestMove));

	// now the rest of the moves
	while (it.hasNext()) {
	    Move move = (Move) it.next();
	    int eval = evalState(s.tryMove(move));
	    // if we found a better move, remember it
	    if (eval > max) {
		max = eval;
		bestMove = move;
	    }
	}

	return bestMove;
    }
}




