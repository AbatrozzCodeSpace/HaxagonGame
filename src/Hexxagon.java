import java.util.HashMap;

/* Liesbeth Flobbe
 * Hexxagon game
 * file: Hexxagon.java
 */

/* The actual game... */


public class Hexxagon {
	public static final int INVALID = -1;
	public static final int BLANK = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	
	public static Player p1, p2;
	
	static HaxagonUI ui;
	static State state;
	static Board board;
	static GameLoopState gameLoop;
	static Thread hexxThread;
	static Thread arbiThread;
	static Arbiter arbi;
	
	
	public static void main(String args[]) {
		if(args.length == 0){
			hexxThread = new Thread(new Runnable() {
				@Override
				
				public void run() {
					state = new State();
					ui = new HaxagonUI();
					board = new Board(5, state, ui);
					synchronized (state) {
						try {
							state.wait();
						} catch (InterruptedException e) {
						}
					}
					board.gotoMain();
					gameLoop = new GameLoopState();
					System.out.println("P1="+ui.getP1()+" P2="+ui.getP2());
					runArbi();
				}
			});
			hexxThread.start();
		}
	/*	else if(args.length == 3){

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


		
		}*/
	}

		


	
	private static Player getPlayer(int choice){
		
		switch (choice) {
		case 1:
			return  new Human();
		case 2:
			return new RandomPlayer();
		case 3:
			return new EagerPlayer();
		case 4:
			return new MinimaxPlayer(1);
		case 5:
			return new MinimaxPlayer(2);
		case 6:
			return new ParkMinimaxPlayer(2, 100, 80, 60, 35);
		case 7:
			return new MonteCarloPlayer(0,1000);
		case 8:
			return new MewPlayer(2);
		default:
			return new Human();
		}

	}
	
	public static void runArbi() {
		if(arbiThread != null) {
			arbiThread.interrupt();
		}
		
		arbiThread = new Thread(new Runnable() {
			@Override
			public void run() {
				p1 = getPlayer(ui.getP1());
				p2 = getPlayer(ui.getP2());
				arbi = new Arbiter(p1, p2, 1000, 1000, gameLoop, state, board);
				arbi.showGame();
			}
		});
		arbiThread.start();
	}
	
	public static void resetMatch(){
		System.out.println("Restart");
		state = new State();
		board.setGameState(state);
		runArbi();
	}
}

