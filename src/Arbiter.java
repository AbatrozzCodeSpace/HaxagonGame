import javax.swing.JOptionPane;

/* Liesbeth Flobbe
 * Hexxagon game
 * file: Arbiter.java
 */

/* The Arbiter let's two players play against each other. */

public class Arbiter {
	Player red;
	Player blue;
	int delayRed;
	int delayBlue;
	State s; // this state is the data structure and painter
	Board board;

	public Arbiter(Player p1, Player p2, int d1, int d2,
			GameLoopState gameLoop, State s, Board b) {
		// create begin state
		this.s = s;
		this.board = b;

		// create players
		red = p1;
		blue = p2;

		// set delays before player
		// (you can set a large delay before a computer player and none
		// before a human player)
		delayRed = d1;
		delayBlue = d2;

		// this.appwin = appwin;
		// appwin.setState(s);
		// appwin.setGameLoopState(gameLoop);
	}

	public void showGame() {
		s.paint(board);
		// Actually play the game

		// as long as there are empty squares
		while (s.getnEmpty() > 0) {
			Move m;

			// ask the right player to make a move
			if (s.whoseTurn().equals("red")) {
				try {
					Thread.sleep(delayRed);
				} catch (Exception e) {
				}
				m = red.chooseMove(s);
			} else {
				try {
					Thread.sleep(delayBlue);
				} catch (Exception e) {
				}
				m = blue.chooseMove(s);
			}

			// fail miserably if the move is illegal or if the player fails te
			// produce a move
			if (m == null || !s.legalMove(m)) {
				System.err.println(s.whoseTurn()
						+ " did not produce a legal move. Ending game.");
				String[] choices = { "Restart Game", "Return to Title" };
				int response = JOptionPane.showOptionDialog(null // Center in
																	// window.
						, "The winner is "+s.otherPlayer(s.whoseTurn())+"!" // Message
						, "Game Ended!" // Title in titlebar
						, JOptionPane.YES_NO_OPTION // Option type
						, JOptionPane.PLAIN_MESSAGE // messageType
						, null // Icon (none)
						, choices // Button text as above.
						, "None of your business" // Default button's label
				);
				if (response == 0){
					Hexxagon.resetMatch();
				}
				else if (response ==1){
					Hexxagon.returnToTitle();
				}
				return;
			}

			// apply move
			s.applyMove(m);

			// paint new situation
			s.paint(board);

		}
	}

	public State evalGame() {
		// don't show anything, just return the final state
		// shameless code copying...
		// Actually play the game

		// as long as there are empty squares
		while (s.getnEmpty() > 0) {
			Move m;

			// ask the right player to make a move
			if (s.whoseTurn().equals("red"))
				m = red.chooseMove(s);
			else
				m = blue.chooseMove(s);

			// fail miserably if the move is illegal or if the player fails te
			// produce a move
			if (m == null || !s.legalMove(m)) {
				System.err.println(s.whoseTurn()
						+ " did not produce a legal move. Ending game.");
				return s;
			}

			// apply move
			s.applyMove(m);

		}
		return s;
	}

	public void reset() {
		s = new State();
	}

}
