/* Liesbeth Flobbe
 * Hexxagon game
 * file: Hexxagon.java
 */

/* The actual game... */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hexxagon {
	public static final int INVALID = -1;
	public static final int BLANK = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	GameLoopState gameLoop;
	
	public static Player p1, p2;
	
	static Thread hexxThread;
	
	public static void main(String args[]) {
		hexxThread = new Thread(new Runnable() {
			@Override
			public void run() {
				State s = new State();
				HaxagonUI ui = new HaxagonUI();
				Board board = new Board(5, s, ui);
				synchronized (s) {
					try {
						s.wait();
					} catch (InterruptedException e) {
					}
				}
				board.gotoMain();
				GameLoopState gameLoop = new GameLoopState();
			
				System.out.println("P1="+ui.getP1()+" P2="+ui.getP2());
				
				Player p1 = getPlayer(ui.getP1());
				Player p2 = getPlayer(ui.getP2());

				Arbiter arbi = new Arbiter(p1, p2, 1000, 1000, gameLoop, s, board);

				System.out.println("Starting game. There will be a 1 second delay before each player is allowed to move.");
				System.out.println("End the game by closing the game window.");
				arbi.showGame();
			}
		});
		hexxThread.start();
	}
	public static Player getPlayer(int player) {
		Player p;
		switch (player) {
		case 1:
			p = new Human();
			break;
		case 2:
			p = new RandomPlayer();
			break;
		case 3:
			p = new EagerPlayer();
			break;
		case 4:
			p = new MinimaxPlayer(1);
			break;
		case 5:
			p = new MinimaxPlayer(2);
			break;
		default:
			p = new Human();
		}
		return p;
	}
	private static Player getPlayer(String player) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Pick an implementation for player [" + player
				+ "].");

		System.out.println("1. Human (interactive) player");
		System.out.println("2. RandomPlayer (makes random moves)");
		System.out
				.println("3. EagerPlayer (picks best move, but does not look ahead");
		System.out.println("4. MinimaxPlayer, look ahead one move");
		System.out.println("5. MinimaxPlayer, look ahead two moves");

		int choice = 0;
		// try to get valid input
		do {
			try {
				System.out.print("Enter your choice: ");
				choice = Integer.parseInt(br.readLine());
			} catch (IOException e) {
				System.out
						.println("Problem with reading from stdin, giving up:"
								+ e);
				System.exit(1);
			} catch (NumberFormatException e) {
				System.out
						.println("Please type only a single integer after every question.");
				continue;
			}

		} while (!(choice > 0 && choice < 6));
		Player p = getPlayer(choice);
		return p;
		
	}
}
