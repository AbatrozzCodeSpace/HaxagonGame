/* Liesbeth Flobbe
 * Hexxagon game
 * file: Hexxagon.java
 */

/* The actual game... */

import java.io.*;

public class Hexxagon {
    public static void main(String args[]) {
	Player p1 = getPlayer("red");
	Player p2 = getPlayer("blue");
	
	// The last two argument to Arbiter are delay times before
	// allowing a player to make a move, so you have time to see
	// what just happened.
	Arbiter a = new Arbiter(p1, p2, 1000, 1000);
	
	System.out.println("Starting game. There will be a 1 second delay before each player is allowed to move.");
	System.out.println("End the game by closing the game window.");

	a.showGame();

    }

    private static Player getPlayer(String player) {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Pick an implementation for player [" + player + "].");

	System.out.println("1. Human (interactive) player");
	System.out.println("2. RandomPlayer (makes random moves)");
	System.out.println("3. EagerPlayer (picks best move, but does not look ahead");
	System.out.println("4. MinimaxPlayer, look ahead one move");
	System.out.println("5. MinimaxPlayer, look ahead two moves");

	int choice = 0;
	// try to get valid input
	do {
	    try {
		System.out.print("Enter your choice: ");
		choice = Integer.parseInt(br.readLine());
	    }
	    catch (IOException e) {
		System.out.println("Problem with reading from stdin, giving up:" + e);
		System.exit(1);
	    }
	    catch (NumberFormatException e) {
		System.out.println("Please type only a single integer after every question.");
		continue;
	    }
	    
	} while (! (choice > 0 && choice < 6));

	Player p;

	switch (choice) {
	case 1:
	    p = new InteractivePlayer();
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
	    p = new InteractivePlayer();
	}

	return p;
    }
}
