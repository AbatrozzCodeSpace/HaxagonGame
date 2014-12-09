/* Liesbeth Flobbe
 * Hexxagon game
 * file: InteractivePlayer.java
 */

import java.io.*;

public class InteractivePlayer implements Player {
    private BufferedReader br;

    public InteractivePlayer() {
	br = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Starting interactive session.");
	System.out.println("Please type only a single integer after every question.");

    }
 
    public Move chooseMove(State s) {

	System.out.println("[" + s.whoseTurn() + "], it's your turn.");

	Move m;

	// try to get a valid move
	do {
	    
	    int startrow = 0;
	    int startcol = 0;
	    int endrow = 0;
	    int endcol = 0;

	    // try to get valid input
	    while (true) {
		try {
		    System.out.print("Start row? ");
		    startrow = Integer.parseInt(br.readLine());
		    
		    System.out.print("Start column? ");
		    startcol = Integer.parseInt(br.readLine());
		    
		    System.out.print("Goal row? ");
		    endrow = Integer.parseInt(br.readLine());
		    
		    System.out.print("Goal column? ");
		    endcol = Integer.parseInt(br.readLine());
		}
		catch (IOException e) {
		    System.out.println("Problem with reading from stdin, giving up:" + e);
		    System.exit(1);
		}
		catch (NumberFormatException e) {
		    System.out.println("Please type only a single integer after every question.");
		    continue;
		}

		// if we got here, input = ok
		break;
	    }
	    
	    m = new Move(new Hexpos(startrow, startcol),
			 new Hexpos(endrow, endcol),
			 s.whoseTurn());
	    
	    if (! s.legalMove(m))
		System.out.println("Not a legal move for [" + s.whoseTurn() + "]");
	    
	} while (! s.legalMove(m));
	
	return m;
    }

}
