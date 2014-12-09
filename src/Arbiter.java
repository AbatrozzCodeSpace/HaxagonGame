/* Liesbeth Flobbe
 * Hexxagon game
 * file: Arbiter.java
 */

/* The Arbiter let's two players play against each other. */

import java.awt.*;
import java.io.*;

public class Arbiter {
	Player red;
	Player blue;
	int delayRed;
	int delayBlue;
	State s; // this state is the data structure and painter
	boolean visibility;

	AppWindow appwin;

	public Arbiter(Player p1, Player p2, int d1, int d2, AppWindow appwin, GameLoopState gameLoop) {
		// create begin state
		s = new State();
		visibility = true;
		
		// create players
		red = p1;
		blue = p2;

		// set delays before player
		// (you can set a large delay before a computer player and none
		// before a human player)
		delayRed = d1;
		delayBlue = d2;
		
		this.appwin = appwin;
		appwin.setState(s);
		appwin.setGameLoopState(gameLoop);
	}
	public Arbiter(Player p1, Player p2, int d1, int d2, AppWindow appwin, GameLoopState gameLoop, boolean visibility) {
		// create begin state
		s = new State();
		this.visibility = visibility;
		
		// create players
		red = p1;
		blue = p2;

		// set delays before player
		// (you can set a large delay before a computer player and none
		// before a human player)
		delayRed = d1;
		delayBlue = d2;
		
		this.appwin = appwin;
		appwin.setState(s);
		appwin.setGameLoopState(gameLoop);
	}

	public String showGame() {

		// create AppWindow
		//AppWindow appwin = new AppWindow(s);
		appwin.setSize(new Dimension(500, 520));
		appwin.setTitle("Hexxagon");
		appwin.setVisible(this.visibility);

		// Actually play the game

		// as long as there are empty squares

		int turn = 1;
		int tmp = 0;
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)))){
			out.println("--");
			out.close();
		
		
		} catch(IOException e){}
		while (s.getnEmpty() > 0) {
			Move m;

			// ask the right player to make a move
			if (s.whoseTurn().equals("red")) {
				try {
					Thread.sleep(delayRed);
				} catch (Exception e) {
				}
				;
				m = red.chooseMove(s);
			} else {
				try {
					Thread.sleep(delayBlue);
				} catch (Exception e) {
				}
				;
				m = blue.chooseMove(s);
			}

			// fail miserably if the move is illegal or if the player fails te
			// produce a move
			if (m == null || !s.legalMove(m)) {
				System.err.println(s.whoseTurn()
						+ " did not produce a legal move. Ending game.");
				break;
			}

			// apply move
			s.applyMove(m);

			// paint new situation
			appwin.repaint();
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)))){
				out.println("Turn " + turn + " : " + s);
				out.close();
			
			
			} catch(IOException e){}

			turn++;

		}
		tmp = s.getnRed() - s.getnBlue();
		if(s.whoseTurn().equals("red"))
			tmp += s.getnEmpty();
		else 
			tmp -= s.getnEmpty();
		
		String winner;
		if(tmp == 0)
			winner = "draws";
		else if(tmp < 0)
			winner = "red";
		else 
			winner = "blue";
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)))){
			out.println("(" + winner + ") : " + s);
			out.close();
		} catch(IOException e){}
		
		return winner;
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
