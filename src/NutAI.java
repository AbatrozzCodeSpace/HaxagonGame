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

import javax.swing.JOptionPane;

public class NutAI implements Player {
    String me;
    String opponent;

    // Assign an evaluation value to a state: higher values are better
    private int evalState(State s) {

	if (me.equals("red"))
	    return s.getnRed();
	
	return s.getnBlue();
    }	

    /* This function is called by Arbiter. */
    public Move chooseMove(State s) {
	// remember who we are (evalState likes to know that)
	me = s.whoseTurn();
	opponent = s.otherPlayer(me);

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
	System.out.println(bestMove.toString());
	MyList enemymoves = s.tryMove(bestMove).findMovesByString(opponent);

	Iterator enemyIt = enemymoves.iterator();
	Move enemyAtk = (Move) enemyIt.next();
	System.out.println(enemyAtk.toString());
	int minBestMove = evalState(s.tryDoubleMove(bestMove,enemyAtk));
	System.out.println("minBestMove = "+minBestMove);
	int eval;
	while(enemyIt.hasNext()){
		enemyAtk = (Move) enemyIt.next();
		System.out.println(enemyAtk.toString());
		eval = (s.tryDoubleMove(bestMove,enemyAtk))!= null ? evalState(s.tryDoubleMove(bestMove,enemyAtk)):1000;
		System.out.println("eval = "+eval);
		if (eval > minBestMove) {
			  minBestMove = eval;
			
		  }
		
	}
	
//	int max = evalState(s.tryMove(bestMove));
	// now the rest of the moves
	while (it.hasNext()) {
		Move move = (Move) it.next();
		System.out.println(move.toString());
		enemymoves = s.tryMove(move).findMovesByString(opponent);
		enemyIt = enemymoves.iterator();
		enemyAtk = (Move) enemyIt.next();
		int min = (s.tryDoubleMove(bestMove,enemyAtk))!= null ? evalState(s.tryDoubleMove(bestMove,enemyAtk)):1000;
		System.out.println("Eval = "+min);
		while(enemyIt.hasNext()){
			enemyAtk = (Move) enemyIt.next();
			System.out.println(enemyAtk.toString());
			eval = (s.tryDoubleMove(bestMove,enemyAtk))!= null ? evalState(s.tryDoubleMove(bestMove,enemyAtk)):1000;
			System.out.println("Eval = "+eval);
			if (eval < min) {
				  min = eval;
			  }
		}
		if(min>minBestMove){
			minBestMove = min;
			bestMove = move;
		}
	
	}
	JOptionPane.showMessageDialog(null,"Selected eval = "+minBestMove);
	System.out.println("EVARUTO");
	return bestMove;
    }
}




