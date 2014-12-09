
public class Human implements Player{

	@Override
	public Move chooseMove(State s) {
		synchronized (s) {
			try {
				s.wait();
			} catch (InterruptedException e) {}
			return Board.getMove();
		}
	}
}
