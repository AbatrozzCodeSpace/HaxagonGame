/* Liesbeth Flobbe
 * Hexxagon game
 * file: Arbiter.java
 */

/* The Arbiter let's two players play against each other. */

import java.awt.*;

public class Arbiter {
    Player red;
    Player blue;
    int delayRed;
    int delayBlue;
    State s;

    public Arbiter (Player p1, Player p2, int d1, int d2) {
	// create begin state
	s = new State();

	// create players
	red = p1;
	blue = p2;

	// set delays before player
	// (you can set a large delay before a computer player and none
	// before a human player)
	delayRed = d1;
	delayBlue = d2;
    }

    public void showGame() {

	// create AppWindow
	AppWindow appwin = new AppWindow(s);
	appwin.setSize(new Dimension(500, 520));
	appwin.setTitle("Hexxagon");
	appwin.setVisible(true);

	// Actually play the game

	// as long as there are empty squares
	while (s.getnEmpty() > 0) {
	    Move m;

	    // ask the right player to make a move
	    if (s.whoseTurn().equals("red")) {
		try { Thread.sleep(delayRed); } catch (Exception e) {};
		m = red.chooseMove(s);
	    }
	    else {
		try { Thread.sleep(delayBlue); } catch (Exception e) {};
		m = blue.chooseMove(s);
	    }

	    // fail miserably if the move is illegal or if the player fails te
	    // produce a move
	    if (m == null || ! s.legalMove(m)) {
		System.err.println(s.whoseTurn() + " did not produce a legal move. Ending game.");
		return;
	    }

	    // apply move
	    s.applyMove(m);
	    
	    // paint new situation
	    appwin.repaint();
	    
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
	    if (m == null || ! s.legalMove(m)) {
		System.err.println(s.whoseTurn() + " did not produce a legal move. Ending game.");
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
