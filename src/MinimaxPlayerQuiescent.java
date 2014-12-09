/* Liesbeth Flobbe
 * Hexxagon game
 * file: MinimaxPlayerQuiescent.java
 */

import java.util.*;

public class MinimaxPlayerQuiescent implements Player {
    String me;
    int maxdepth;

    // Assign an evaluation value to a state: higher values are better
    private int evalState(State s) {
	if (me.equals("red"))
	    return s.getnRed() - s.getnBlue();
	
	return s.getnBlue() - s.getnRed();
    }	

    public MinimaxPlayerQuiescent(int md) {
	maxdepth = md;
    }

    /* evalMove returns the minimax-value of a Move m that should be applied
     * to State s, where depth indicates how deep the algorithm should search.
     * A cutoff test (evalState) will be used if depth == 0 or if there are
     * no new moves possible (e.g. at the end of the game).
     */
    public int evalMove(Move m, State s, int depth) {
	State newstate = s.tryMove(m);

	if (depth < -4) { // we need to stop somewhere...
	    return evalState(newstate);
	} else if (depth <= 0) { // cutting off if quiescent

	    // Determine if state is quiescent. We *only* look at the
	    // position that we just left through a jump, not at others.
	    // (Everywhere I say 'I', I mean the player those turn it is,
	    // so everything here applies to opportunities as well as 
	    // to threats.)
	    Hexpos lastpos = m.begin;

	    // if this position is still occupied (no jump), don't worry
	    if (newstate.owner(lastpos) != null)
		return evalState(newstate);

	    // if there are less than 3 of our pieces neigbouring this
	    // empty position, don't worry either
	    MyList neighbours = lastpos.neighbours();
	    int nMine = 0;
	    Iterator it = neighbours.iterator();
	    while (it.hasNext()) {
		Hexpos hp = (Hexpos) it.next();
		if (newstate.owner(hp) != null &&
		    ! newstate.owner(hp).equals(newstate.whoseTurn()))
		    nMine++;
	    }

	    if (nMine < 3)
		return evalState(newstate);

	    // If we got here, it means that in the next move, it might
	    // be possible to jump to the possible that the last player
	    // just left, and take over at least 2 pieces. (Might, 
	    // because we don't know if this position can be reached by
	    // the other player.
	    
	    // Generate moves to this position and do the usual minimax stuff
	    // [I really should do something against all this copy-paste code]
	    MyList moves = newstate.findMovesTo(lastpos);
	    if (moves.size() == 0)
		return evalState(newstate);
	    Collections.shuffle(moves);
	    	    // Iterate over all moves
	    it = moves.iterator();
	    int minimaxvalue = evalMove((Move) it.next(), newstate, depth - 1);
	    while (it.hasNext()) {
		Move newmove = (Move) it.next();
		int eval = evalMove(newmove, newstate, depth - 1);
		if (newstate.whoseTurn().equals(me)) // our turn
		    minimaxvalue = Math.max(minimaxvalue, eval);
		else // opponent's turn
		    minimaxvalue = Math.min(minimaxvalue, eval);
	    }
	    
	    // If we had tried other moves, the appropiate player would
	    // probably have been able to get some points in the next move.
	    // To compare with moves that have been cut off, we compensate
	    // for this.
	    if (newstate.whoseTurn().equals(me))
		minimaxvalue -= 2;
	    else
		minimaxvalue += 2;

	    return minimaxvalue;
	} else { // minimax-search

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
	    int minimaxvalue = evalMove((Move) it.next(), newstate, depth - 1);

	    // now the rest of the moves
	    while (it.hasNext()) {
		Move newmove = (Move) it.next();
		int eval = evalMove(newmove, newstate, depth - 1);

		// if this is our turn, save the maximumvalue, otherwise
		// the mimimumvalue
		if (newstate.whoseTurn().equals(me)) // our turn
		    minimaxvalue = Math.max(minimaxvalue, eval);
		else // opponent's turn
		    minimaxvalue = Math.min(minimaxvalue, eval);
	    }
		
	    return minimaxvalue;
	}

    }

    /* This function is called by Arbiter. It is about the actual state of
     * the game, not some future state we generated.
     */
    public Move chooseMove(State s) {
	// remember who we are so we can correctly evaluate states
	me = s.whoseTurn();

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
	int minimaxvalue = evalMove(bestMove, s, maxdepth);

	// now the rest of the moves
	while (it.hasNext()) {
	    Move move = (Move) it.next();
	    int eval = evalMove(move, s, maxdepth);
	    // if we found a better move, remember it
	    if (eval > minimaxvalue) {
		minimaxvalue = eval;
		bestMove = move;
	    }
	}
		
	return bestMove;
    }
}




