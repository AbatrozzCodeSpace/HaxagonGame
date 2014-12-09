import java.util.Collections;


public class Human implements Player{

	@Override
	public Move chooseMove(State s) {
 	// return if there are no moves available
		if (s.findMoves().size() == 0)
		    return null;
		synchronized (s) {
			try {
				s.wait();
			} catch (InterruptedException e) {}
			return Board.getMove();
		}
	}
}
