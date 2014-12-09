import java.awt.Dimension;
import java.io.*;
import java.util.HashMap;

public class Hexxagon {
	public static void main(String args[]) {

	
		AppWindow appWin = new AppWindow();
		GameLoopState gameLoop = new GameLoopState();
		if(args.length == 0){
			Player p1 = getPlayer("red");
			Player p2 = getPlayer("blue");
			appWin = new AppWindow();
			gameLoop = new GameLoopState();

			// The last two argument to Arbiter are delay times before
			// allowing a player to make a move, so you have time to see
			// what just happened.
			Arbiter a = new Arbiter(p1, p2, 1000, 1000, appWin, gameLoop);

			System.out.println("Starting game. There will be a 1 second delay before each player is allowed to move.");
			System.out.println("End the game by closing the game window.");

			System.out.println(a.showGame());
		}
		else if(args.length == 3){

			int player1 = Integer.parseInt(args[0]);
			int player2 = Integer.parseInt(args[1]);
			int loop = Integer.parseInt(args[2]);
			HashMap winningPos = new HashMap();


			for (int r = 1; r <= 17; r++) { // for every row
				int c = ((r % 2 == 0) ? 2 : 1); // what col to start?
				for (; c <= 9; c += 2) {
					Hexpos h = new Hexpos(r, c);
					if (h.onBoard()){
						winningPos.put(h.hashCode(), 0);
					
					}
				}
			}

			for(int i = 0; i < loop; i++){
				Player p1 = getPlayer(player1);
				Player p2 = getPlayer(player2);
				if(appWin != null) appWin.dispose();
				appWin = new AppWindow();
				gameLoop = new GameLoopState();
				Arbiter a = new Arbiter(p1, p2, 1000, 1000, appWin, gameLoop, false);
				a.showGamePrint(winningPos);
			}


		
		}


	}
	private static Player getPlayer(int choice){
		
		switch (choice) {
		case 1:
			return  new InteractivePlayer();
		case 2:
			return new RandomPlayer();
		case 3:
			return new EagerPlayer();
		case 4:
			return new MinimaxPlayer(1);
		case 5:
			return new MinimaxPlayer(2);
		case 6:
			return new ParkMinimaxPlayer(2, 100, 50, 40, 25);
		case 7:
			return new MonteCarloPlayer(0,1000);
		default:
			return new InteractivePlayer();
		}
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

		} while (!(choice > 0 && choice < 8));

		Player p = getPlayer(choice);



		return p;
	}
}
