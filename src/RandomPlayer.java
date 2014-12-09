/* Liesbeth Flobbe
 * s1196464
 * Hexxagon game
 * file: RandomPlayer.java
 */

import java.util.*;

public class RandomPlayer implements Player {
    Random r = new Random();

    public Move chooseMove(State s) {
	MyList moves = s.findMoves();
	if (moves.size() == 0) // no moves possible
 	    return null;
	return (Move) moves.get(r.nextInt(moves.size()));
    }

}
