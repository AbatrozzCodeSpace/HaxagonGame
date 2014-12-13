import java.io.File;
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
	static int p1Type,p2Type;
	
	
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
				//	if(GameLoopState.wav.isReady()){
						
				//	}
				}
			});
			hexxThread.start();
		//	GameLoopState.wav.openSong("music.wav");
		}
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
			return new ParkMinimaxPlayer(2, 100, 67, 47, 30);
		case 7:
			return new MonteCarloPlayer(0,1000);
		case 8:
			return new MewPlayer(2);
		case 9:
			return new MickAI(3);
		case 10:
			return new PruningParkMinimaxPlayer(3, 100, 67, 47, 30);
		default:
			return new Human();
		}

	}
	public static String getPlayerType(int p){
		switch(p){
		case 1:
			return  "(Human)";
		case 2:
			return "(Random player)";
		case 3:
			return "(Eager player)";
		case 4:
			return "(Minimax(depth 1) Player)";
		case 5:
			return "(Minimax(depth 2) Player)";
		case 6:
			return "(ParkKawinSarinNuttapongMinimax Player)";
		case 7:
			return "Monte Carlo Player)";
		case 8:
			return "Mew Player)";
		case 9:
			return "(Mick Player)";
		case 10:
			return "(PruningParkKawinSaMinimax Player)";
		default:
			return "(Human)";
		}
	}
	public static void runArbi() {
		if(arbiThread != null) {
			arbiThread.interrupt();
		}
		
		arbiThread = new Thread(new Runnable() {
			@Override
			public void run() {
				p1Type= ui.getP1();
				p2Type = ui.getP2();
				p1 = getPlayer(p1Type);
				p2 = getPlayer(p2Type);
				
				arbi = new Arbiter(p1, p2, 700, 700, gameLoop, state, board);
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
	public static void restartApplication(){
		board.dispose();
		Arbiter.restart();
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
}

