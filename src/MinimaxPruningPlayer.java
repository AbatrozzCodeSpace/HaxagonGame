/* Liesbeth Flobbe
 * Hexxagon game
 * file: MinimaxPlayer.java
 */

import java.util.*;

public class MinimaxPruningPlayer implements Player {
    String me;
    int maxdepth;
    private static int MAX_TOP = 5;

    // Assign an evaluation value to a state: higher values are better
    private int evalState(State s) {
	if (me.equals("red"))
	    return s.getnRed() - s.getnBlue();
	
	return s.getnBlue() - s.getnRed();
    }	

    public MinimaxPruningPlayer(int md) {
	maxdepth = md;
    }

    /* evalMove returns the minimax-value of a Move m that should be applied
     * to State s, where depth indicates how deep the algorithm should search.
     * A cutoff test (evalState) will be used if depth == 0 or if there are
     * no new moves possible (e.g. at the end of the game).
     */
    public int evalMove(Move m, State s, int depth) {

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
	    
	    int[] topScores = new int[MAX_TOP];
	    Move[] topMoves = new Move[MAX_TOP];
	    for(int i=0;i<MAX_TOP;i++){
	    	if(newstate.whoseTurn().equals(me))
	    		topScores[i] = Integer.MIN_VALUE;
	    	else
	    		topScores[i] = Integer.MAX_VALUE;
	    }
	    int i=0;
	    while(it.hasNext()){
	    	Move newmove = (Move) it.next();
	    	boolean change = false;
			int score = evalState(newstate.tryMove(newmove));
			if(depth==maxdepth){
				System.out.print(score+ " ");
			}
			if(newstate.whoseTurn().equals(me)){
				for(i=0;i<MAX_TOP;i++){
					if(score>topScores[i]){
						change = true;
						break;
					}
				}
			}
			else{
				for(i=0;i<MAX_TOP;i++){
					if(score<topScores[i]){
						change = true;
						break;
					}
				}
			}
			if(change){
				for(int j=MAX_TOP-2;j>=i;j--){
					topScores[j+1] = topScores[j];
					topMoves[j+1] = topMoves[j];
				}
				topMoves[i] = newmove;
				topScores[i] = score;
			}
	    }
	    
//	    if(depth==maxdepth){
//	    	System.out.println("\ntop ");
//	    for(i=0;i<MAX_TOP;i++){
//	    	System.out.println(topScores[i]);
//	    }
//	    System.out.println("----------------------------");
//	    }

	    // We already know there is at least one move, so use it to
	    // initialize minimaxvalue
	    int minimaxvalue = evalMove(topMoves[0], newstate, depth - 1);
	    
	    i=1;
	    // now the rest of the moves
	    while (i<MAX_TOP && topMoves[i]!=null) {
		Move newmove = topMoves[i];
		int eval = evalMove(newmove, newstate, depth - 1);

		// if this is our turn, save the maximumvalue, otherwise
		// the mimimumvalue
		if (newstate.whoseTurn().equals(me)) // our turn
		    minimaxvalue = Math.max(minimaxvalue, eval);
		else // opponent's turn
		    minimaxvalue = Math.min(minimaxvalue, eval);
		i++;
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




