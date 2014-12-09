/* Liesbeth Flobbe
 * Hexxagon game
 * file: EvalMany.java
 */

/* Play the game, but don't actually watch it, just show the results. 
 * (no human players) 
 */

import java.io.*;

public class EvalMany {
    public static void main(String args[]) {
	Player p1 = getPlayer("red");
	Player p2 = getPlayer("blue");
	
	Arbiter a = new Arbiter(p1, p2, 0, 0);

	int redWins = 0;
	int blueWins = 0;
	int draw = 0;

	for (int i = 0; i < 20; i++) {
	    a.reset();
	    State end = a.evalGame();
	    System.out.println("Red: " + end.getnRed() +
			       "\tBlue: " + end.getnBlue());
	    if (end.getnRed() == end.getnBlue())
		draw++;
	    else if (end.getnRed() > end.getnBlue())
		redWins++;
	    else 
		blueWins++;
	}

	System.out.println("");
	System.out.println("Red wins: " + redWins);
	System.out.println("Blue wins: " + blueWins);
	System.out.println("Draw: " + draw);
	
    }
    
    private static Player getPlayer(String player) {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Pick an implementation for player [" + player + "].");

	System.out.println("2. RandomPlayer (makes random moves)");
	System.out.println("3. EagerPlayer (picks best move, but does not look ahead)");
	System.out.println("4. MinimaxPlayer, look ahead one move");
	System.out.println("5. MinimaxPlayer, look ahead two moves");
	System.out.println("6. MinimaxPlayer, one move, (very incomplete) test for quiescence before cutoff");	
	System.out.println("7. MinimaxPlayer, two moves, (very incomplete) test for quiescence before cutoff");

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
	    
	} while (! (choice > 1 && choice < 8));

	Player p;

	switch (choice) {
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
	case 6:
	    p = new MinimaxPlayerQuiescent(1);
	    break;
	case 7:
	    p = new MinimaxPlayerQuiescent(2);
	    break;
	default:
	    p = new RandomPlayer();
	}

	return p;
    }
}
