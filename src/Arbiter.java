import javax.swing.JOptionPane;

/* Liesbeth Flobbe
 * Hexxagon game
 * file: Arbiter.java
 */

/* The Arbiter let's two players play against each other. */


import java.awt.*;
import java.io.*;
import java.util.HashMap;


public class Arbiter {
	Player red;
	Player blue;
	int delayRed;
	int delayBlue;
	State s; // this state is the data structure and painter

	Board board;
	
	static boolean reset;
	static boolean restart;

	boolean visibility;


	public Arbiter(Player p1, Player p2, int d1, int d2,
			GameLoopState gameLoop, State s, Board b) {
		// create begin state
		this.s = s;
		this.board = b;
		reset = false;
		restart = false;
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
		
	//	this.appwin = appwin;
	//	appwin.setState(s);
	//	appwin.setGameLoopState(gameLoop);
	}
/*	public Arbiter(Player p1, Player p2, int d1, int d2, AppWindow appwin, GameLoopState gameLoop, boolean visibility) {
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

		// this.appwin = appwin;
		// appwin.setState(s);
		// appwin.setGameLoopState(gameLoop);
	}
*/

	public void showGame() {
		s.paint(board);

		// Actually play the game

		// as long as there are empty squares

		int turn = 1;
		int tmp = 0;
		
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
			if (m == null || !s.legalMove(m) || reset) {
				
				endGame();
				return;
			}
			if(restart){
				restart = false;
				return;
			}

			// apply move
			if(m!=null){
				int currentRed=  s.getnRed();
				int currentBlue = s.getnBlue();
				s.applyMove(m);
				if(s.whoseTurn().equals("blue")){
					if(s.getnBlue()<currentBlue)GameLoopState.isChangeOwner=true;
				}
				else{
					if(s.getnRed()<currentRed)GameLoopState.isChangeOwner=true;
				}
				if(GameLoopState.isChangeOwner)	GameLoopState.effector.openEffect("charge.wav");
				else GameLoopState.effector.openEffect("move.wav");
				GameLoopState.isChangeOwner = false;
			}

			// paint new situation
			s.paint(board);

		}
		endGame();
	}
	
	public void endGame(){
		String stillWalking = (s.whoseTurn() == "red")?"blue" : "red";
		MyList blankPos = s.ownees(null);

		s.fill(blankPos,stillWalking,board);
		int redScore =s.getnRed() + ((stillWalking == "red")?s.getnEmpty():0);
		int blueScore = s.getnBlue() + ((stillWalking == "blue")?s.getnEmpty():0);
		board.setScore(redScore,blueScore);
		board.setPlayer(s.whoseTurn().equals("red") ? 1 : 2 );
		String winner = redScore > blueScore ? "Red is the winner!" : (blueScore==redScore)? "Draw!" : "Blue is the winner!";

		System.err.println(s.whoseTurn()
				+ " did not produce a legal move. Ending game.");
		GameLoopState.effector.openEffect("charge.wav");
		String[] choices = { "Restart Game","Return to Title" };
		
		int response = JOptionPane.showOptionDialog(null // Center in
															// window.
				, winner // Message
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
		else if (response==1){
			Hexxagon.restartApplication();
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

	public static void reset() {
		reset = true;
	}
	public static void restart(){
		restart = true;
	}

}
